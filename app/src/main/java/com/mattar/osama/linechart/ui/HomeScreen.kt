package com.mattar.osama.linechart.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mattar.osama.linechart.ui.line.PlotChartScreen
import com.mattar.osama.linechart.ui.theme.LineChartTheme

@Composable
fun HomeScreen() {
    LineChartTheme {
        Scaffold(topBar = {
            TopAppBar(title = { Text(text = "Compose Charts") })
        }) {
            HomeScreenContent()
        }
    }
}

@Composable
private fun HomeScreenContent() {
    Surface(color = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PlotChartScreen()
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() = HomeScreen()