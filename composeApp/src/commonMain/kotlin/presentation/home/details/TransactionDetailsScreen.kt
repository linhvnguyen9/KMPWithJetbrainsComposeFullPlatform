package presentation.home.details

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
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import io.github.aakira.napier.Napier
import presentation.home.list.TransactionsListScreenModel
import presentation.root.TestRootScreen

class TransactionDetailsScreen: Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
//        val tabNavigator = LocalTabNavigator.current
        val screenModel = rememberScreenModel { TransactionDetailsScreenModel() }
        val state = screenModel.stateFlow.collectAsState()

        Column {
            Text("Transaction Details")

            LifecycleEffect(
                onStarted = {
                    println("LifecycleEffect started Details")
                    Napier.d(tag = "SideEffect", message = "LifecycleEffect started Details")
                },
                onDisposed = {
                    println("LifecycleEffect disposed Details")
                    Napier.d(tag = "SideEffect", message = "LifecycleEffect disposed Details")
                }
            )

            LaunchedEffect(Unit) {
                println("LaunchedEffect Details")
                Napier.d(tag = "SideEffect", message = "LaunchedEffect Details")
            }

            LaunchedEffect(state.value) {
                Napier.d(tag = "KMPWithJetbrainsCompose", message = "TransactionDetailsScreen state changed to ${state.value}")
                println("TransactionDetailsScreen state changed to ${state.value}")
            }

            Button(onClick = {
                Napier.d(tag = "KMPWithJetbrainsCompose", message = "parent level ${navigator.parent?.level}")
                Napier.d(tag = "KMPWithJetbrainsCompose", message = "parent parent level ${navigator.parent?.parent?.level}")
                navigator.parent?.parent?.push(TestRootScreen()) // Can use tailrec to go to root
            }) {
                Text("Go to test root")
            }

            Button(onClick = {
                navigator.pop()
            }) {
                Text("Nav back")
            }

            DisposableEffect(Unit) {
                onDispose {
                    Napier.d(tag = "SideEffect", message = "DisposableEffect Details")
                    println("DisposableEffect Details")
                }
            }

        }
    }
}