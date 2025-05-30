package com.example.circularreveal

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.circularreveal.composereveal.animation.ItemPhysicsSpec
import com.example.circularreveal.composereveal.animation.RevealOrigin
import com.example.circularreveal.composereveal.modifiers.animateItemReveal
import com.example.circularreveal.composereveal.modifiers.circularReveal
import com.example.circularreveal.composereveal.modifiers.circularRevealFromTouch

@Composable
fun CircularRevealDemo() {
    var selectedDemo by remember { mutableStateOf("Physics") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Enhanced Circular Reveal",
            style = MaterialTheme.typography.headlineMedium
        )

        // Demo selector
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Physics", "Touch", "Elements", "Origins").forEach { demo ->
                FilterChip(
                    onClick = { selectedDemo = demo },
                    label = { Text(demo) },
                    selected = selectedDemo == demo
                )
            }
        }

        when (selectedDemo) {
            "Physics" -> PhysicsDemo()
            "Touch" -> TouchRevealDemo()
            "Elements" -> ElementRevealDemo()
            "Origins" -> OriginsDemo()
        }
    }
}

@Composable
private fun PhysicsDemo() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Physics-Based Item Animations",
            style = MaterialTheme.typography.bodyLarge
        )

        // Different physics effects
        PhysicsCard(
            title = "Strong Bounce",
            physicsSpec = ItemPhysicsSpec.StrongBounce, // Using the new predefined spec
            backgroundColor = Color(0xFF4CAF50)
        )

        PhysicsCard(
            title = "Gentle Entrance",
            physicsSpec = ItemPhysicsSpec.GentleEntrance, // Using the new predefined spec
            backgroundColor = Color(0xFF2196F3)
        )

        PhysicsCard(
            title = "Subtle Shake & Fade",
            physicsSpec = ItemPhysicsSpec( // Custom spec for a more "shake" feel
                initialScale = 1f, initialAlpha = 0.2f, initialTranslationMagnitude = 0f,
                rotationDegrees = 10f, // Small rotation for shake
                springAnimationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessHigh)
            ),
            backgroundColor = Color(0xFFFF5722)
        )
    }
}

@Composable
private fun PhysicsCard(
    title: String,
    physicsSpec: ItemPhysicsSpec,
    backgroundColor: Color
) {
    var isRevealed by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable { isRevealed = !isRevealed },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Tap to reveal: $title",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            val parentRevealedState = rememberUpdatedState(isRevealed)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .circularReveal(
                        visible = isRevealed,
                        origin = RevealOrigin.Center, // Fixed origin for this demo
                        animationSpec = spring(dampingRatio = 0.8f)
                    )
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Multiple items with staggered physics
                    repeat(4) { index ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .animateItemReveal( // Using the simplified animateItemReveal
                                    parentRevealed = parentRevealedState,
                                    itemIndex = index,
                                    physicsSpec = physicsSpec,
                                    staggerDelayMs = 50L, // Added stagger
                                    revealOrigin = RevealOrigin.Center // Items will bounce towards center
                                )
                                .background(Color.White.copy(alpha = 0.9f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                color = backgroundColor,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TouchRevealDemo() {
    var isRevealed by remember { mutableStateOf(false) }
    var touchPosition by remember { mutableStateOf(Offset.Zero) }
    var containerSize by remember { mutableStateOf(Size.Zero) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Touch-Based Reveal",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "Tap anywhere on the card to reveal from that position",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .onGloballyPositioned { coordinates ->
                    containerSize = coordinates.size.toSize()
                }
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        touchPosition = offset
                        isRevealed = !isRevealed
                    }
                },
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "👆",
                        style = MaterialTheme.typography.displayMedium
                    )
                    Text("Tap anywhere!")
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .circularRevealFromTouch(
                            visible = isRevealed,
                            touchPosition = touchPosition,
                            containerSize = containerSize,
                            animationSpec = spring(dampingRatio = 0.7f)
                        )
                        .background(Color(0xFF9C27B0)),
                    contentAlignment = Alignment.Center
                ) {
                    val parentRevealedState = rememberUpdatedState(isRevealed)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "✨",
                            style = MaterialTheme.typography.displayLarge,
                            color = Color.White,
                            modifier = Modifier.animateItemReveal( // Using the refined animateItemReveal
                                parentRevealed = parentRevealedState,
                                itemIndex = 0,
                                physicsSpec = ItemPhysicsSpec.Default, // You can customize this
                                staggerDelayMs = 50L,
                                revealOrigin = RevealOrigin.Center // Assuming items bounce from center of touch
                            )
                        )
                        Text(
                            text = "Revealed from touch!",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.animateItemReveal( // Using the refined animateItemReveal
                                parentRevealed = parentRevealedState,
                                itemIndex = 1,
                                physicsSpec = ItemPhysicsSpec.GentleEntrance, // Example of different spec
                                staggerDelayMs = 50L,
                                revealOrigin = RevealOrigin.Center
                            )
                        )
                    }
                }
            }
        }

        Button(
            onClick = { isRevealed = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reset")
        }
    }
}

@Composable
private fun ElementRevealDemo() {
    var isMenuOpen by remember { mutableStateOf(false) }
    var fabPosition by remember { mutableStateOf(Offset.Zero) }
    var containerSize by remember { mutableStateOf(Size.Zero) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Element Position Reveal",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "Menu reveals from the FAB position",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .onGloballyPositioned { coordinates ->
                    containerSize = coordinates.size.toSize()
                }
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                // Content behind menu
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Content behind menu", style = MaterialTheme.typography.titleMedium)
                    repeat(8) { index ->
                        Text(
                            text = "Content item ${index + 1}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Menu overlay
                val parentRevealedState = rememberUpdatedState(isMenuOpen)

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .circularReveal(
                            visible = isMenuOpen,
                            elementPosition = fabPosition,
                            containerSize = containerSize,
                            animationSpec = spring(dampingRatio = 0.8f)
                        )
                        .background(Color.Black.copy(alpha = 0.95f))
                        .clickable { isMenuOpen = false },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Text(
                            text = "Quick Actions",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.animateItemReveal( // Using the refined animateItemReveal
                                parentRevealed = parentRevealedState,
                                itemIndex = 0,
                                revealOrigin = RevealOrigin.BottomEnd, // Assuming FAB is bottom-end
                                physicsSpec = ItemPhysicsSpec.StrongBounce
                            )
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(32.dp)
                        ) {
                            val actions = listOf(
                                Icons.Default.Favorite to "Like",
                                Icons.Default.Share to "Share",
                                Icons.Default.Star to "Star"
                            )

                            actions.forEachIndexed { index, (icon, label) ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.animateItemReveal( // Using the refined animateItemReveal
                                        parentRevealed = parentRevealedState,
                                        itemIndex = index + 1,
                                        revealOrigin = RevealOrigin.BottomEnd,
                                        physicsSpec = ItemPhysicsSpec.Default, // Default for these items
                                        staggerDelayMs = 50L
                                    )
                                ) {
                                    FloatingActionButton(
                                        onClick = { isMenuOpen = false },
                                        modifier = Modifier.size(48.dp),
                                        containerColor = Color.White
                                    ) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = label,
                                            tint = Color.Black
                                        )
                                    }
                                    Text(
                                        text = label,
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }
                    }
                }

                // FAB
                FloatingActionButton(
                    onClick = { isMenuOpen = !isMenuOpen },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .onGloballyPositioned { coordinates ->
                            // Get center of FAB relative to container
                            val fabCenter = coordinates.positionInParent() + Offset(
                                coordinates.size.width / 2f,
                                coordinates.size.height / 2f
                            )
                            fabPosition = fabCenter
                        }
                ) {
                    Icon(
                        imageVector = if (isMenuOpen) Icons.Default.Add else Icons.Default.Add,
                        contentDescription = "Menu",
                        modifier = Modifier.graphicsLayer {
                            rotationZ = if (isMenuOpen) 45f else 0f
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun OriginsDemo() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Reveal Origins with Item Physics",
            style = MaterialTheme.typography.bodyLarge
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(400.dp)
        ) {
            itemsIndexed(RevealOrigin.entries) { index, origin ->
                OriginCardWithPhysics(origin = origin, cardIndex = index)
            }
        }
    }
}

@Composable
private fun OriginCardWithPhysics(origin: RevealOrigin, cardIndex: Int) {
    var isRevealed by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable { isRevealed = !isRevealed },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = origin.name,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            val parentRevealedState = rememberUpdatedState(isRevealed)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .circularReveal(
                        visible = isRevealed,
                        origin = origin,
                        animationSpec = spring(dampingRatio = 0.7f)
                    )
                    .background(getColorForOrigin(origin))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "✨",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.animateItemReveal( // Using the refined animateItemReveal
                            parentRevealed = parentRevealedState,
                            itemIndex = 0,
                            revealOrigin = origin,
                            physicsSpec = ItemPhysicsSpec.StrongBounce, // Strong bounce for emoji
                            staggerDelayMs = 50L
                        )
                    )
                    Text(
                        text = "Physics!",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.animateItemReveal( // Using the refined animateItemReveal
                            parentRevealed = parentRevealedState,
                            itemIndex = 1,
                            revealOrigin = origin,
                            physicsSpec = ItemPhysicsSpec.GentleEntrance, // Gentle for text
                            staggerDelayMs = 50L
                        )
                    )
                }
            }
        }
    }
}

private fun getColorForOrigin(origin: RevealOrigin): Color {
    return when (origin) {
        RevealOrigin.TopStart -> Color(0xFFE91E63)
        RevealOrigin.TopCenter -> Color(0xFF9C27B0)
        RevealOrigin.TopEnd -> Color(0xFF673AB7)
        RevealOrigin.CenterStart -> Color(0xFF3F51B5)
        RevealOrigin.Center -> Color(0xFF2196F3)
        RevealOrigin.CenterEnd -> Color(0xFF03A9F4)
        RevealOrigin.BottomStart -> Color(0xFF00BCD4)
        RevealOrigin.BottomCenter -> Color(0xFF009688)
        RevealOrigin.BottomEnd -> Color(0xFF4CAF50)
    }
}

@Preview(showBackground = true)
@Composable
fun CircularRevealDemoPreview() {
    MaterialTheme {
        CircularRevealDemo()
    }
}