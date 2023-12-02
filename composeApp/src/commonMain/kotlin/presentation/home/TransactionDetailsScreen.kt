package presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class TransactionDetailsScreen: Screen {

    @Composable
    override fun Content() {
        Column {
            Text("Transaction Details")
        }
    }
}