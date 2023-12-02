package presentation.home.details

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.aakira.napier.Napier
import presentation.root.TestRootScreen

class TransactionDetailsScreen: Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Column {
            Text("Transaction Details")

            Button(onClick = {
                Napier.d(tag = "KMPWithJetbrainsCompose", message = "parent level ${navigator.parent?.level}")
                Napier.d(tag = "KMPWithJetbrainsCompose", message = "parent parent level ${navigator.parent?.parent?.level}")
                navigator.parent?.parent?.push(TestRootScreen())
            }) {
                Text("Go to test root")
            }
        }
    }
}