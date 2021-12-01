package com.mattar.osama.lineplot

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class AxisLine(val thickness: Dp = 1.5.dp, val color: Color = Color.Gray) {
    private val mPaint by lazy {
        Paint().apply {
            color = this@AxisLine.color
            style = PaintingStyle.Stroke
        }
    }

    fun paint(density: Density) {
        mPaint.strokeWidth = thickness.value * density.density
    }
}