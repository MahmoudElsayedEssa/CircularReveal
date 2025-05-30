package com.example.circularreveal.composereveal.animation

import androidx.compose.ui.geometry.Offset

/**
 * Defines the origin position for circular reveal animations.
 */
enum class RevealOrigin {
    TopStart,
    TopCenter,
    TopEnd,
    CenterStart,
    Center,
    CenterEnd,
    BottomStart,
    BottomCenter,
    BottomEnd;

    /**
     * Converts the origin to normalized offset coordinates (0f to 1f).
     */
    internal fun toOffset(): Offset = when (this) {
        TopStart -> Offset(0f, 0f)
        TopCenter -> Offset(0.5f, 0f)
        TopEnd -> Offset(1f, 0f)
        CenterStart -> Offset(0f, 0.5f)
        Center -> Offset(0.5f, 0.5f)
        CenterEnd -> Offset(1f, 0.5f)
        BottomStart -> Offset(0f, 1f)
        BottomCenter -> Offset(0.5f, 1f)
        BottomEnd -> Offset(1f, 1f)
    }
}
