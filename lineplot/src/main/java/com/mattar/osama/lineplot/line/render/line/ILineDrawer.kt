package com.mattar.osama.lineplot.line.render.line

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope

interface ILineDrawer {
    fun drawLine(drawScope: DrawScope, canvas: Canvas, linePath: Path)
}