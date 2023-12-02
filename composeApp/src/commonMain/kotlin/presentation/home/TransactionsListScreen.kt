package presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class TransactionsListScreen(): Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Column {
            Text("Transactions List")

            Button(onClick = {
                navigator.push(TransactionDetailsScreen())
            }) {
                Text("Go to transaction details")
            }
        }
    }
}