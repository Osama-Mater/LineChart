package com.mattar.osama.plotchart

import androidx.compose.ui.graphics.Color

fun Color.toLegacyInt(): Int {
    return android.graphics.Color.argb(
        (alpha * 255F + .5F).toInt(),
        (red * 255F + .5F).toInt(),
        (green * 255F + .5F).toInt(),
        (blue * 255F + .5F).toInt()
    )
}