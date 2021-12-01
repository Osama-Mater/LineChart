package com.mattar.osama.linechart.ui.line

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.mattar.osama.linechart.ui.theme.GreyCustom
import com.mattar.osama.linechart.ui.theme.Margins
import com.mattar.osama.linechart.ui.theme.RoundRectangle
import com.mattar.osama.linechart.ui.theme.toPx
import com.mattar.osama.plotchart.line.LineGraph
import com.mattar.osama.plotchart.line.LinePlot
import com.mattar.osama.plotchart.line.Point
import java.text.DecimalFormat

@Composable
fun PlotChartScreen() {
    Surface {
        PlotChartContent()
    }
}

@Composable
private fun PlotChartContent() {
    val xOffset = remember { mutableStateOf(0f) }
    val cardWidth = remember { mutableStateOf(0) }
    val visibility = remember { mutableStateOf(false) }
    val points = remember { mutableStateOf(listOf<Point>()) }
    val totalWidth = remember { mutableStateOf(0) }
    val padding = 16.dp
    val density = LocalDensity.current


    Column(
        modifier = Modifier
            .padding(horizontal = Margins.horizontal, vertical = Margins.vertical)
            .onGloballyPositioned {
                totalWidth.value = it.size.width
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.height(150.dp)) {
            if (visibility.value) {
                Surface(
                    modifier = Modifier
                        .width(200.dp)
                        .align(Alignment.BottomCenter)
                        .onGloballyPositioned {
                            cardWidth.value = it.size.width
                        }
                        .graphicsLayer(translationX = xOffset.value),
                    shape = RoundRectangle,
                    color = GreyCustom
                ) {
                    Column(Modifier.padding(horizontal = 8.dp)) {
                        val value = points.value
                        if (value.isNotEmpty()) {
                            val formatted = DecimalFormat("#.#").format(value[0].value)
                            val day = value[0].label
                            Text(
                                modifier = Modifier.padding(vertical = 8.dp),
                                text = "Score at $day : $formatted",
                                style = MaterialTheme.typography.subtitle1,
                                color = Color.Blue
                            )
                        }
                    }
                }

            }
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {

            LineGraph(
                plot = LinePlot(
                    listOf(
                        LinePlot.Line(
                            LineChartDataModel.DataPoints.dataPoints3,
                            LinePlot.Connection(),
                            LinePlot.Intersection(),
                            LinePlot.Highlight { center ->
                                val color = Color.Red
                                drawCircle(color, 9.dp.toPx(), center, alpha = 0.3f)
                                drawCircle(color, 6.dp.toPx(), center)
                                drawCircle(Color.White, 3.dp.toPx(), center)
                            }
                        ),
                    ),
                ),

                onSelectionStart = { visibility.value = true },
                onSelectionEnd = { visibility.value = false }
            ) { x, pts ->
                val cWidth = cardWidth.value.toFloat()
                var xCenter = x + padding.toPx(density)
                xCenter = when {
                    xCenter + cWidth / 2f > totalWidth.value -> totalWidth.value - cWidth
                    xCenter - cWidth / 2f < 0f -> 0f
                    else -> xCenter - cWidth / 2f
                }
                xOffset.value = xCenter
                points.value = pts
            }

        }
    }
}

@Composable
private fun PlotChartRow(
    cardWidth: MutableState<Int>, xOffset: MutableState<Float>, visibility: MutableState<Boolean>
) {
}