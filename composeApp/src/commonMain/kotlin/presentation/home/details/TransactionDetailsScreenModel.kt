package presentation.home.details

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class TransactionDetailsScreenModel: ScreenModel {
    val stateFlow = MutableStateFlow(0)

    init {
//        screenModelScope.launch {
//            while (isActive) {
//                Napier.d(tag = "Lifecycle") { "TransactionDetailsScreenModel staying alive, staying alive" }
//                delay(2000)
//            }
//        }
        println("TransactionDetailsScreenModel init")
        screenModelScope.launch {
            while (isActive) {
                val nextValue = stateFlow.value + 1
                Napier.d(tag = "Lifecycle") { "TransactionDetailsScreenModel staying alive, staying alive $nextValue" }
                println("TransactionDetailsScreenModel staying alive, staying alive $nextValue")
                stateFlow.value = nextValue
                delay(5000)
            }
        }
    }

    override fun onDispose() {
        super.onDispose()

        Napier.d(tag = "Lifecycle") { "TransactionDetailsScreenModel onDispose" }
    }
}