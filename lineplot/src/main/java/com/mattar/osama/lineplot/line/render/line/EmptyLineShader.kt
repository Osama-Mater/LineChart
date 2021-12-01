package com.mattar.osama.lineplot.line.render.line

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope

object EmptyLineShader : ILineShader {
    override fun fillLine(drawScope: DrawScope, canvas: Canvas, fillPath: Path) {
        // do nothing here
    }
}