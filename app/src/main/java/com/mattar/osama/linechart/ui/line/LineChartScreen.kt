package com.mattar.osama.linechart.ui.line

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mattar.osama.linechart.ui.theme.Margins
import com.mattar.osama.lineplot.line.LineChart

@Composable
fun LineChartScreen() {
    Surface {
        LineChartContent()
    }
}

@Composable
private fun LineChartContent() {
    val lineChartData = LineChartDataModel()
    Column(
        modifier = Modifier.padding(horizontal = Margins.horizontal, vertical = Margins.vertical),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlotChartRow(lineChartDataModel = lineChartData)
    }
}

@Composable
private fun PlotChartRow(
    lineChartDataModel: LineChartDataModel
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        LineChart(
            lineChartData = lineChartDataModel.lineChartData
        )
    }
}