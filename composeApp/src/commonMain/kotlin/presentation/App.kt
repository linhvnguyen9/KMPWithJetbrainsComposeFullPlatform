package presentation

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.navigator.Navigator
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

@Composable
fun App() {
    MaterialTheme {
        Napier.d(tag = "KMPWithJetbrainsCompose", message = "App")
        Navigator(MainScreen())
    }
}
