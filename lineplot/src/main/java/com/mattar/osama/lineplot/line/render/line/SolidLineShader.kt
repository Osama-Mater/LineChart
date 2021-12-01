package com.mattar.osama.lineplot.line.render.line

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope

data class SolidLineShader(val color: Color = Color.Blue) : ILineShader {
    override fun fillLine(drawScope: DrawScope, canvas: Canvas, fillPath: Path) {
        drawScope.drawPath(path = fillPath, color = color)
    }
}
