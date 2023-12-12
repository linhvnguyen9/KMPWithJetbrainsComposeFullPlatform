package presentation.home.list

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.aakira.napier.Napier
import presentation.home.details.TransactionDetailsScreen

class TransactionsListScreen: Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = rememberScreenModel { TransactionsListScreenModel() }
        val state = screenModel.stateFlow.collectAsState()

        Column {
            Text("Transactions List")

            LifecycleEffect(
                onStarted = {
                    println("LifecycleEffect started")
                    Napier.d(tag = "SideEffect", message = "LifecycleEffect started")
                },
                onDisposed = {
                    println("LifecycleEffect disposed")
                    Napier.d(tag = "SideEffect", message = "LifecycleEffect disposed")
                }
            )

            LaunchedEffect(Unit) {
                println("LaunchedEffect")
                Napier.d(tag = "SideEffect", message = "LaunchedEffect")
            }

            LaunchedEffect(state.value) {
                Napier.d(tag = "KMPWithJetbrainsCompose", message = "TransactionsListScreen state changed to ${state.value}")
                println("TransactionsListScreen state changed to ${state.value}")
            }

            Button(onClick = {
                navigator.push(TransactionDetailsScreen())
            }) {
                Text("Go to transaction details")
            }

            DisposableEffect(Unit) {
                onDispose {
                    Napier.d(tag = "SideEffect", message = "DisposableEffect")
                    println("DisposableEffect")
                }
            }
        }
    }
}