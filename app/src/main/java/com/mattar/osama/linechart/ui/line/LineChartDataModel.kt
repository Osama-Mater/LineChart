package com.mattar.osama.linechart.ui.line

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mattar.osama.lineplot.line.LineChartData
import com.mattar.osama.lineplot.line.LineChartData.Point
import com.mattar.osama.lineplot.line.render.point.EmptyPointDrawer
import com.mattar.osama.lineplot.line.render.point.FilledCircularPointDrawer
import com.mattar.osama.lineplot.line.render.point.HollowCircularPointDrawer
import com.mattar.osama.lineplot.line.render.point.IPointDrawer
import kotlin.random.Random

class LineChartDataModel {
    object DataPoints {
        val dataPoints3 = listOf(
            com.mattar.osama.plotchart.line.Point(20f, "Mon"),
            com.mattar.osama.plotchart.line.Point(40f, "Tue"),
            com.mattar.osama.plotchart.line.Point(75f, "Wed"),
            com.mattar.osama.plotchart.line.Point(50f, "Thu"),
            com.mattar.osama.plotchart.line.Point(35f, "Today")
        )
    }

    var lineChartData by mutableStateOf(
        LineChartData(
            points = listOf(
                Point(20f, "Mon"),
                Point(40f, "Tue"),
                Point(75f, "Wed"),
                Point(50.0f, "Thu"),
                Point(35.0f, "Today"),
            )
        )
    )

    var pointDrawerType by mutableStateOf(PointDrawerType.Hollow)
    val pointDrawer: IPointDrawer
        get() {
            return when (pointDrawerType) {
                PointDrawerType.None -> EmptyPointDrawer
                PointDrawerType.Filled -> FilledCircularPointDrawer()
                PointDrawerType.Hollow -> HollowCircularPointDrawer()
            }
        }


    private fun randomYValue(): Float = Random.Default.nextInt(45, 145).toFloat()
}