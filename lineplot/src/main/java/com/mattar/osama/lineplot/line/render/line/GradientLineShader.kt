package com.mattar.osama.lineplot.line.render.line

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope

data class GradientLineShader(val colors: List<Color> = listOf(Color.Blue, Color.Transparent)) :
    ILineShader {

    private val mBrush = Brush.verticalGradient(colors)

    override fun fillLine(drawScope: DrawScope, canvas: Canvas, fillPath: Path) {
        drawScope.drawPath(path = fillPath, brush = mBrush)
    }
}
