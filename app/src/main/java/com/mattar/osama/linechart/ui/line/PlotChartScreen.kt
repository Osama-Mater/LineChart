package com.mattar.osama.linechart.ui.line

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PlotChartScreen() {
    Surface {
        PlotChartContent(lines = LineChartDataModel.DataPoints.dataPoints3, modifier = Modifier)
    }
}