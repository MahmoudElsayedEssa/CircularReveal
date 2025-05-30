package com.example.circularreveal.composereveal.modifiers

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import com.example.circularreveal.composereveal.animation.ItemPhysicsSpec
import com.example.circularreveal.composereveal.animation.RevealOrigin
import kotlinx.coroutines.delay

/**
 * Animates individual items within a circular reveal with physics-based directional entrance.
 * Items slide in from the reveal origin direction with scale, alpha, and optional rotation.
 * This replaces and enhances the previous `animateItemReveal` and `animateItemRevealDirectional`.
 *
 * @param parentRevealed State indicating if the parent reveal is active
 * @param itemIndex Index of this item for staggered animation
 * @param revealOrigin The origin direction of the parent reveal, used for directional movement
 * @param physicsSpec Configuration for physics effects like initial scale, translation, and spring properties
 * @param staggerDelayMs Delay offset per item index
 */
fun Modifier.animateItemReveal( //
    parentRevealed: State<Boolean>,
    itemIndex: Int = 0,
    revealOrigin: RevealOrigin = RevealOrigin.Center,
    physicsSpec: ItemPhysicsSpec = ItemPhysicsSpec.Default,
    staggerDelayMs: Long = 0L
): Modifier = composed {
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(parentRevealed.value) {
        if (parentRevealed.value) {
            val startDelay = itemIndex * staggerDelayMs
            if (startDelay > 0) delay(startDelay)
            animatedProgress.animateTo(1f, physicsSpec.springAnimationSpec)
        } else {
            // When hiding, immediately snap back to initial state
            animatedProgress.snapTo(0f)
        }
    }

    this.graphicsLayer {
        val progress = animatedProgress.value

        // Alpha animation: fades in from initialAlpha to 1f
        alpha = physicsSpec.initialAlpha + (1f - physicsSpec.initialAlpha) * progress

        // Scale animation: scales from initialScale to 1f with bounce
        val currentScale = physicsSpec.initialScale + (1f - physicsSpec.initialScale) * progress
        scaleX = currentScale
        scaleY = currentScale

        // Directional Translation (for entrance effect)
        // Progress (0 to 1) means it moves from initial offset to 0 offset.
        // So, (1f - progress) indicates how much of the initial offset is still "left" to move.
        val remainingProgressForTranslation = 1f - progress

        translationX = when (revealOrigin) {
            RevealOrigin.TopStart, RevealOrigin.CenterStart, RevealOrigin.BottomStart -> -physicsSpec.initialTranslationMagnitude * remainingProgressForTranslation

            RevealOrigin.TopEnd, RevealOrigin.CenterEnd, RevealOrigin.BottomEnd -> physicsSpec.initialTranslationMagnitude * remainingProgressForTranslation

            else -> 0f // No horizontal translation for Center or Top/Bottom Center
        }

        translationY = when (revealOrigin) {
            RevealOrigin.TopStart, RevealOrigin.TopCenter, RevealOrigin.TopEnd -> -physicsSpec.initialTranslationMagnitude * remainingProgressForTranslation

            RevealOrigin.BottomStart, RevealOrigin.BottomCenter, RevealOrigin.BottomEnd -> physicsSpec.initialTranslationMagnitude * remainingProgressForTranslation

            else -> 0f // No vertical translation for Center or Center Start/End
        }

    }
}
