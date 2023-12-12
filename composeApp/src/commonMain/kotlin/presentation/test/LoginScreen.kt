package presentation.test

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onLoginSubmit: () -> Unit,
    loginStatus: String
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login to eTicket", fontSize = 28.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            placeholder = { Text("Email address") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            placeholder = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onLoginSubmit,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF5B5E)),
            modifier = Modifier.height(48.dp).fillMaxWidth()
        ) {
            Text("Login", color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(loginStatus, fontSize = 28.sp, color = Color.Red)
        Spacer(modifier = Modifier.height(24.dp))
        Text("Don't have an account?")
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onLoginSubmit,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF5B5E)),
            modifier = Modifier.height(48.dp).fillMaxWidth()
        ) {
            Text("Register", color = Color.White)
        }
    }
}
@Composable
fun LoginViewModel() {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var loginStatus by rememberSaveable { mutableStateOf("") }

    LoginScreen(
        email = email,
        onEmailChange = { email = it },
        password = password,
        onPasswordChange = { password = it },
        onLoginSubmit = {
            loginStatus = when {
                !isValidEmail(email) -> "Invalid email address"
                !isValidPassword(password) -> "Incorrect password"
                else -> "Logging in"
            }
        },
        loginStatus = loginStatus
    )
}

fun isValidEmail(email: String): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    return email.matches(emailPattern.toRegex())
}

fun isValidPassword(password: String): Boolean {
    val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$"
    return password.matches(passwordPattern.toRegex())
}