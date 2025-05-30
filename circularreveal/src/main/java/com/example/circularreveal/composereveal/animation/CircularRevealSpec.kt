package com.example.circularreveal.composereveal.animation

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring

/**
 * Configuration for circular reveal animations.
 *
 * @param origin The position from which the reveal animation originates
 * @param animationSpec The animation specification for the reveal transition
 * @param hideWhenInvisible Whether to remove content from layout when fully hidden
 * @param itemAnimationSpec Animation spec for individual items within the reveal
 * @param itemStaggerDelayMs Delay between item animations in milliseconds
 */
data class CircularRevealSpec(
    val origin: RevealOrigin = RevealOrigin.Center,
    val animationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    ),
    val hideWhenInvisible: Boolean = true,
    val itemAnimationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessHigh
    ),
    val itemStaggerDelayMs: Long = 50L
)
