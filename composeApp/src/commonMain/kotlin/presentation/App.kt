package presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorContent
import io.github.aakira.napier.Napier

@Composable
fun App() {
    MaterialTheme {
        Napier.d(tag = "KMPWithJetbrainsCompose", message = "App")
        Content()
//        Navigator(MainScreen())
    }
}

@Composable
private fun Content() {
    Navigator(
        screen = BasicNavigationScreen(index = 0, wrapContent = true)
    ) { navigator ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
//                .padding(16.dp)
                .background(Color.Gray)
        ) {
            Box {
                if (navigator.items.size <= 10) {
                    Text(
                        text = "Level #${navigator.level}",
                        modifier = Modifier.padding(8.dp)
                    )
                    CurrentScreen()
                } else {
                    NestedNavigation(backgroundColor = Color.LightGray) {
                        CurrentScreen()
                    }
                }
            }
        }
    }
}

@Composable
private fun NestedNavigation(
    backgroundColor: Color,
    content: NavigatorContent
) {
    Navigator(
        screen = BasicNavigationScreen(index = 0, wrapContent = true)
    ) { navigator ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
//                .padding(16.dp)
                .background(backgroundColor)
        ) {
            Text(
                text = "Level #${navigator.level}",
                modifier = Modifier.padding(8.dp)
            )
            content(navigator)
        }
    }
}
