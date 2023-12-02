package presentation.root

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class TestRootScreen(): Screen {

    @Composable
    override fun Content() {
        Column {
            Text("Test Root")
        }
    }
}