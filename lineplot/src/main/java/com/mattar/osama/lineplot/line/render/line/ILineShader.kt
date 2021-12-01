package com.mattar.osama.lineplot.line.render.line

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope

interface ILineShader {
    fun fillLine(drawScope: DrawScope, canvas: Canvas, fillPath: Path)
}