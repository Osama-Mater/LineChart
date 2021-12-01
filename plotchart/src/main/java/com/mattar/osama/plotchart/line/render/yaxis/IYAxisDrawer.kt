package com.mattar.osama.plotchart.line.render.yaxis

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.DrawScope

interface IYAxisDrawer {
    fun drawAxisLine(drawScope: DrawScope, canvas: Canvas, drawableArea: Rect)

    fun drawAxisLabels(
        drawScope: DrawScope,
        canvas: Canvas,
        drawableArea: Rect,
        minValue: Float,
        maxValue: Float
    )
}