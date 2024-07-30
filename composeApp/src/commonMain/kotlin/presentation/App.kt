package presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorContent
import io.github.aakira.napier.Napier
import presentation.candlechart.MarketChartPreview

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
    Column() {
        val loadedData = listOf(0.85691719,0.85685734,0.83926574,0.83918992,0.84550658,0.84776159,0.8476,0.84773333,0.8476,0.8476,0.84768068,0.8476,0.84771857,0.8476,0.84719424,0.84768068,0.84773333,0.846848,0.84777472,0.84776874,0.8476804,0.8476804,0.84612693,0.85049127,0.85063194,0.84769391,0.84769391,0.84780335,0.84746327,0.84746327,0.84756224,0.84632137,0.84608,0.8462,0.84616533,0.85071785,0.8463289,0.84615881,0.84618431,0.85044602,0.84611777,0.84583584,0.8462297,0.84704561,0.84733783,0.8472854,0.84702845,0.84718117,0.84791771,0.84795143,0.84789617,0.84789617,0.84792433,0.84785074,0.84778883,0.84775135,0.84768068,0.84768068,0.84768536,0.84768536,0.85207589,0.85195116,0.85201474,0.84741818,0.84749474,0.84772531,0.84768068,0.84747463,0.84747463,0.85201473,0.847456,0.84732622,0.84556418,0.84556418,0.84518209,0.84540157,0.84538371,0.84948583,0.84529819,0.84528477,0.84301545,0.84737933,0.83581305,0.83537881,0.83482597,0.83401726,0.833979,0.83376253,0.83356657,0.83353079,0.83222722,0.83224459,0.83222833,0.83192695,0.83137343,0.83178882,0.83167513,0.83167513,0.83230048,0.83645894,0.83104218,0.83104218,0.83571937,0.83143363,0.83137495,0.83137495,0.83131554,0.83131044,0.83117559,0.83119279,0.83115754,0.83134,0.83097011,0.83115754,0.83114667,0.83114939,0.83091778,0.8309,0.83092945,0.83540528,0.83034847,0.8295068,0.82941826,0.82958491,0.82624001,0.82460136,0.82443378,0.82441783,0.82441783,0.82440828,0.82323831,0.8192,0.8192,0.8192,0.8192,0.82350046,0.81948573,0.8171435,0.81703497,0.81704648,0.81703556,0.81708138,0.82135367,0.81718005,0.81680849,0.78797508,0.7872086,0.79006709,0.77813513,0.77761939,0.77721432,0.7771648,0.77419725,0.77310913,0.77310913,0.77712343,0.77323274,0.77310679,0.77686739,0.77285053,0.77686991,0.77296941,0.77291313,0.77310824,0.771638,0.77557567,0.75541409,0.75540248,0.75390816,0.75457864,0.7554929,0.75593376,0.75585488,0.75510791,0.75536464,0.75514364,0.7482311,0.74767219,0.74728186,0.73847082,0.73806248,0.73963581,0.74756094,0.742684,0.742675,0.74245435,0.74262232,0.742056,0.74111171,0.74064281,0.74058334,0.74058334)
        val data = remember { mutableStateOf<List<Double>?>(null) }

        data.value?.let {
            MarketChartPreview()
        }

        Button(onClick = {
            data.value = loadedData
        }) {
            Text("Load data")
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
