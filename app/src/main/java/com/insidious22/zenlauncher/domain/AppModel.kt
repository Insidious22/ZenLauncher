package com.insidious22.zenlauncher.domain

import androidx.compose.ui.graphics.ImageBitmap

data class AppModel(
    val packageName: String,
    val label: String,
    val icon: ImageBitmap?
)
