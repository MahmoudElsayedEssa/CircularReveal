package com.example.circularreveal.composereveal.modifiers

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.debugInspectorInfo
import com.example.circularreveal.composereveal.animation.CircularRevealSpec
import com.example.circularreveal.composereveal.animation.RevealOrigin
import com.example.circularreveal.composereveal.util.calculateMaxRadius
import com.example.circularreveal.composereveal.util.mapToSize

/**
 * Clips content using an animated circular reveal effect.
 *
 * The circle expands when [visible] is true and contracts when false.
 * The animation originates from the specified position.
 *
 * @param visible Controls the visibility state of the content
 * @param origin The position from which the reveal animation starts
 * @param animationSpec The animation specification for the transition
 * @param hideWhenInvisible Whether to completely hide content when invisible
 */
fun Modifier.circularReveal(
    visible: Boolean,
    origin: RevealOrigin = RevealOrigin.Center,
    animationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium
    ),
    hideWhenInvisible: Boolean = true
): Modifier = composed(factory = {
    val transition = updateTransition(visible, label = "CircularReveal")
    val progress = transition.animateFloat(
        label = "RevealProgress",
        transitionSpec = { animationSpec as FiniteAnimationSpec<Float> }) { isVisible -> if (isVisible) 1f else 0f }

    circularRevealWithProgress(
        progress = progress, origin = origin.toOffset(), hideWhenInvisible = hideWhenInvisible
    )
}, inspectorInfo = debugInspectorInfo {
    name = "circularReveal"
    properties["visible"] = visible
    properties["origin"] = origin
    properties["hideWhenInvisible"] = hideWhenInvisible
})

/**
 * Clips content using an animated circular reveal effect with custom configuration.
 *
 * @param visible Controls the visibility state of the content
 * @param spec Configuration for the reveal animation
 */
fun Modifier.circularReveal(
    visible: Boolean, spec: CircularRevealSpec
): Modifier = circularReveal(
    visible = visible,
    origin = spec.origin,
    animationSpec = spec.animationSpec,
    hideWhenInvisible = spec.hideWhenInvisible
)

/**
 * Clips content using a circular reveal effect based on a custom progress state.
 *
 * @param progress Animation progress state (0f = hidden, 1f = fully revealed)
 * @param origin The position from which the reveal originates
 * @param hideWhenInvisible Whether to completely hide content when progress is 0f
 */
fun Modifier.circularReveal(
    progress: State<Float>,
    origin: RevealOrigin = RevealOrigin.Center,
    hideWhenInvisible: Boolean = true
): Modifier = circularRevealWithProgress(
    progress = progress, origin = origin.toOffset(), hideWhenInvisible = hideWhenInvisible
)


/**
 * Clips content using a circular reveal effect with custom origin coordinates.
 *
 * @param visible Controls the visibility state of the content
 * @param origin Custom origin point in normalized coordinates (0f to 1f)
 * @param animationSpec The animation specification for the transition
 * @param hideWhenInvisible Whether to completely hide content when invisible
 */
fun Modifier.circularReveal(
    visible: Boolean, origin: Offset, animationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium
    ), hideWhenInvisible: Boolean = true
): Modifier = composed(factory = {
    val transition = updateTransition(visible, label = "CircularReveal")
    val progress = transition.animateFloat(
        label = "RevealProgress",
        transitionSpec = { animationSpec as FiniteAnimationSpec<Float> }) { isVisible -> if (isVisible) 1f else 0f }

    circularRevealWithProgress(
        progress = progress, origin = origin, hideWhenInvisible = hideWhenInvisible
    )
}, inspectorInfo = debugInspectorInfo {
    name = "circularReveal"
    properties["visible"] = visible
    properties["origin"] = origin
    properties["hideWhenInvisible"] = hideWhenInvisible
})


/**
 * Internal implementation for circular reveal with progress state.
 */
private fun Modifier.circularRevealWithProgress(
    progress: State<Float>, origin: Offset, hideWhenInvisible: Boolean
): Modifier {
    return if (hideWhenInvisible && progress.value == 0f) {
        // Completely remove content from layout when hidden
        layout { measurable, constraints ->
            val placeable = measurable.measure(constraints)
            layout(placeable.width, placeable.height) { }
        }
    } else {
        drawWithCache {
            val path = Path()
            val center = origin.mapToSize(size)
            val maxRadius = calculateMaxRadius(origin, size)
            val currentRadius = maxRadius * progress.value

            path.addOval(Rect(center, currentRadius))

            onDrawWithContent {
                clipPath(path) {
                    this@onDrawWithContent.drawContent()
                }
            }
        }
    }
}


/**
 * Reveals content from a touch position or UI element position.
 *
 * @param visible Controls the visibility state of the content
 * @param touchPosition The touch position in screen coordinates (will be normalized)
 * @param containerSize The size of the container for normalization
 * @param animationSpec The animation specification for the transition
 */
fun Modifier.circularRevealFromTouch(
    visible: Boolean,
    touchPosition: Offset,
    containerSize: Size,
    animationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )
): Modifier {
    val normalizedOrigin = Offset(
        x = touchPosition.x / containerSize.width,
        y = touchPosition.y / containerSize.height
    )

    return circularReveal(
        visible = visible,
        origin = normalizedOrigin,
        animationSpec = animationSpec
    )
}


/**
 * Reveals content from a specific UI element's position.
 * Use this with `onGloballyPositioned` to get element positions.
 *
 * @param visible Controls the visibility state of the content
 * @param elementPosition The position of the triggering element
 * @param containerSize The size of the reveal container
 * @param animationSpec The animation specification for the transition
 */
fun Modifier.circularReveal(
    visible: Boolean,
    elementPosition: Offset,
    containerSize: Size,
    animationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )
): Modifier = circularRevealFromTouch(visible, elementPosition, containerSize, animationSpec)
