package com.mattar.osama.lineplot.line.render.point

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.DrawScope

interface IPointDrawer {
    fun drawPoint(drawScope: DrawScope, canvas: Canvas, center: Offset)
}