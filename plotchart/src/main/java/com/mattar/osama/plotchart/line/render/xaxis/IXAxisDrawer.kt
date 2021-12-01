package com.mattar.osama.plotchart.line.render.xaxis

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.DrawScope

interface IXAxisDrawer {
    fun requireHeight(drawScope: DrawScope): Float

    fun drawXAxisLine(drawScope: DrawScope, canvas: Canvas, drawableArea: Rect)

    fun drawXAxisLabels(
        drawScope: DrawScope,
        canvas: Canvas,
        drawableArea: Rect,
        labels: List<String>
    )
}