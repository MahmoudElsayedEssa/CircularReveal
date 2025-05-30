package com.example.circularreveal.composereveal.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import kotlin.math.sqrt

/**
 * Maps normalized offset coordinates to actual pixel coordinates within the given size.
 */
internal fun Offset.mapToSize(size: Size): Offset {
    return Offset(
        x = this.x * size.width,
        y = this.y * size.height
    )
}

/**
 * Calculates the maximum radius needed to fully reveal content from the given origin.
 */
internal fun calculateMaxRadius(normalizedOrigin: Offset, size: Size): Float {
    val x = if (normalizedOrigin.x > 0.5f) normalizedOrigin.x else (1f - normalizedOrigin.x)
    val y = if (normalizedOrigin.y > 0.5f) normalizedOrigin.y else (1f - normalizedOrigin.y)

    val maxX = x * size.width
    val maxY = y * size.height

    return sqrt(maxX * maxX + maxY * maxY)
}
