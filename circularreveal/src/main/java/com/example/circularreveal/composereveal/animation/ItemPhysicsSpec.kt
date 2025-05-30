package com.example.circularreveal.composereveal.animation

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring

/**
 * Revised configuration for physics-based item animations within reveals.
 * Provides smoother, coordinated spring-based entrance.
 */
data class ItemPhysicsSpec(
    val initialScale: Float = 0.8f, // Item starts smaller (0.8 = 80% of normal size)
    val initialAlpha: Float = 0f,    // Item starts invisible
    val initialTranslationMagnitude: Float = 80f, // How far it starts off-center before bouncing
    val rotationDegrees: Float = 0f, // Total degrees to rotate during entrance
    val springAnimationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy, // Controls oscillation
        stiffness = Spring.StiffnessMedium // Controls speed
    )
) {
    companion object {
        val Default = ItemPhysicsSpec()
        val StrongBounce = ItemPhysicsSpec(
            initialScale = 0.5f,
            initialTranslationMagnitude = 150f,
            rotationDegrees = 30f,
            springAnimationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        val GentleEntrance = ItemPhysicsSpec(
            initialScale = 0.9f,
            initialTranslationMagnitude = 20f,
            rotationDegrees = 5f,
            springAnimationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessVeryLow
            )
        )
        val ShakeOnly = ItemPhysicsSpec( // For a shake-like effect without much translation
            initialScale = 1f, initialAlpha = 1f, initialTranslationMagnitude = 0f,
            rotationDegrees = 15f,
            springAnimationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessHigh
            )
        )
    }
}
