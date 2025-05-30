package com.example.circularreveal

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.circularreveal.composereveal.animation.ItemPhysicsSpec
import com.example.circularreveal.composereveal.animation.RevealOrigin
import com.example.circularreveal.composereveal.modifiers.animateItemReveal
import com.example.circularreveal.composereveal.modifiers.circularReveal
import com.example.circularreveal.composereveal.modifiers.circularRevealFromTouch

// Showcase Data Models
data class RevealDemo(
    val title: String, val origin: RevealOrigin, val description: String
)

data class PhysicsDemo(
    val title: String, val spec: ItemPhysicsSpec, val description: String
)


// Main Showcase Screen
@Composable
fun CircularRevealShowcase() {
    var selectedTab by remember { mutableIntStateOf(0) }

    ShowcaseDarkTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Header
            ShowcaseHeader()

            // Tab Bar
            ShowcaseTabBar(
                selectedTab = selectedTab, onTabSelected = { selectedTab = it })

            // Content
            when (selectedTab) {
                0 -> RevealOriginsShowcase()
                1 -> PhysicsSpecsShowcase()
                2 -> InteractiveShowcase()
                3 -> AdvancedFeaturesShowcase()
            }
        }
    }
}

// Header Component
@Composable
private fun ShowcaseHeader() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Circular Reveal Library",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Complete showcase of all features and animations",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

// Tab Bar
@Composable
private fun ShowcaseTabBar(
    selectedTab: Int, onTabSelected: (Int) -> Unit
) {
    val tabs = listOf("Origins", "Physics", "Interactive", "Advanced")

    ScrollableTabRow(
        selectedTabIndex = selectedTab,
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                text = { Text(title) },
                selected = selectedTab == index,
                onClick = { onTabSelected(index) })
        }
    }
}

// 1. Reveal Origins Showcase
@Composable
private fun RevealOriginsShowcase() {
    val revealDemos = remember {
        listOf(
            RevealDemo("Top Start", RevealOrigin.TopStart, "Reveals from top-left corner"),
            RevealDemo("Top Center", RevealOrigin.TopCenter, "Reveals from top center"),
            RevealDemo("Top End", RevealOrigin.TopEnd, "Reveals from top-right corner"),
            RevealDemo("Center Start", RevealOrigin.CenterStart, "Reveals from left center"),
            RevealDemo("Center", RevealOrigin.Center, "Reveals from center"),
            RevealDemo("Center End", RevealOrigin.CenterEnd, "Reveals from right center"),
            RevealDemo("Bottom Start", RevealOrigin.BottomStart, "Reveals from bottom-left"),
            RevealDemo("Bottom Center", RevealOrigin.BottomCenter, "Reveals from bottom center"),
            RevealDemo("Bottom End", RevealOrigin.BottomEnd, "Reveals from bottom-right")
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(revealDemos) { index, demo ->
            RevealOriginDemoCard(
                demo = demo, animationDelay = index * 100L
            )
        }
    }
}

@Composable
private fun RevealOriginDemoCard(
    demo: RevealDemo, animationDelay: Long = 0L
) {
    var isVisible by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable { isVisible = !isVisible },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Demo content that reveals
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .circularReveal(
                        visible = isVisible, origin = demo.origin, animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    )
                    .background(
                        MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)
                    ), contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "Revealed!",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Info overlay
            if (!isVisible) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = demo.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = demo.description,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Tap to reveal",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// 2. Physics Specs Showcase
@Composable
private fun PhysicsSpecsShowcase() {
    val physicsDemos = remember {
        listOf(
            PhysicsDemo(
                "Default", ItemPhysicsSpec.Default, "Standard entrance with subtle bounce"
            ), PhysicsDemo(
                "Strong Bounce",
                ItemPhysicsSpec.StrongBounce,
                "Dramatic entrance with strong physics"
            ), PhysicsDemo(
                "Gentle Entrance",
                ItemPhysicsSpec.GentleEntrance,
                "Smooth, minimal bounce animation"
            ), PhysicsDemo(
                "Shake Only", ItemPhysicsSpec.ShakeOnly, "Pure rotation without translation"
            )
        )
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(physicsDemos) { demo ->
            PhysicsSpecDemoCard(demo)
        }
    }
}

@Composable
private fun PhysicsSpecDemoCard(demo: PhysicsDemo) {
    var isRevealed by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = demo.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = demo.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = { isRevealed = !isRevealed }) {
                    Text(if (isRevealed) "Reset" else "Demo")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Grid of animated items
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.height(120.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(8) { index ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .animateItemReveal(
                                parentRevealed = rememberUpdatedState(isRevealed),
                                itemIndex = index,
                                revealOrigin = RevealOrigin.Center,
                                physicsSpec = demo.spec,
                                staggerDelayMs = 100L
                            )
                            .background(
                                MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp)
                            ), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${index + 1}",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// 3. Interactive Showcase
@Composable
private fun InteractiveShowcase() {
    LazyColumn(
        contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            TouchBasedRevealDemo()
        }
        item {
            ElementBasedRevealDemo()
        }
        item {
            CustomAnimationDemo()
        }
        item {
            StaggeredGridDemo()
        }
    }
}

@Composable
private fun TouchBasedRevealDemo() {
    var isVisible by remember { mutableStateOf(false) }
    var touchPosition by remember { mutableStateOf(Offset.Zero) }
    var containerSize by remember { mutableStateOf(Size.Zero) }

    DemoCard(
        title = "Touch-Based Reveal",
        description = "Tap anywhere on the area to reveal from that position"
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .onGloballyPositioned { coordinates ->
                containerSize = coordinates.size.toSize()
            }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    touchPosition = offset
                    isVisible = !isVisible
                }
            }
            .border(
                2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)
            ), contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .circularRevealFromTouch(
                        visible = isVisible,
                        touchPosition = touchPosition,
                        containerSize = containerSize,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                    .background(
                        MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)
                    ), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Touch revealed!",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            if (!isVisible) {
                Text(
                    text = "Tap anywhere to reveal",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun ElementBasedRevealDemo() {
    var isVisible by remember { mutableStateOf(false) }
    var elementPosition by remember { mutableStateOf(Offset.Zero) }
    var containerSize by remember { mutableStateOf(Size.Zero) }

    DemoCard(
        title = "Element-Based Reveal",
        description = "Reveal animation starts from the button position"
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .onGloballyPositioned { coordinates ->
                    containerSize = coordinates.size.toSize()
                }) {
            // Reveal content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .circularReveal(
                        visible = isVisible,
                        elementPosition = elementPosition,
                        containerSize = containerSize,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                    .background(
                        MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp)
                    ), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Element revealed!",
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            // Trigger button
            FloatingActionButton(
                onClick = { isVisible = !isVisible },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .onGloballyPositioned { coordinates ->
                        val center = coordinates.positionInParent()
                        elementPosition = Offset(
                            center.x + coordinates.size.width / 2,
                            center.y + coordinates.size.height / 2
                        )
                    }) {
                Icon(
                    imageVector = if (isVisible) Icons.Default.Close else Icons.Default.PlayArrow,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun CustomAnimationDemo() {
    var currentSpec by remember { mutableIntStateOf(0) }
    var isVisible by remember { mutableStateOf(false) }

    val animationSpecs = listOf<Pair<String, AnimationSpec<Float>>>(
        "Bouncy" to spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow),
        "Medium" to spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium),
        "Stiff" to spring(Spring.DampingRatioNoBouncy, Spring.StiffnessHigh),
        "Very Slow" to spring(Spring.DampingRatioNoBouncy, Spring.StiffnessVeryLow)
    )

    DemoCard(
        title = "Custom Animation Specs",
        description = "Different spring configurations for varied feel"
    ) {
        Column {
            // Animation selector
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(animationSpecs) { index, (name, _) ->
                    FilterChip(
                        onClick = {
                        currentSpec = index
                        if (isVisible) {
                            isVisible = false
                            // Small delay to reset
                        }
                    }, label = { Text(name) }, selected = currentSpec == index
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Demo area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clickable { isVisible = !isVisible }
                    .border(
                        2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)
                    ), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .circularReveal(
                            visible = isVisible,
                            origin = RevealOrigin.Center,
                            animationSpec = animationSpecs[currentSpec].second
                        )
                        .background(
                            MaterialTheme.colorScheme.tertiary, RoundedCornerShape(12.dp)
                        ), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = animationSpecs[currentSpec].first,
                        color = MaterialTheme.colorScheme.onTertiary,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (!isVisible) {
                    Text(
                        text = "Tap to test ${animationSpecs[currentSpec].first}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun StaggeredGridDemo() {
    var isRevealed by remember { mutableStateOf(false) }
    var currentOrigin by remember { mutableStateOf(RevealOrigin.Center) }

    val origins = listOf(
        RevealOrigin.TopStart,
        RevealOrigin.TopCenter,
        RevealOrigin.TopEnd,
        RevealOrigin.CenterStart,
        RevealOrigin.Center,
        RevealOrigin.CenterEnd,
        RevealOrigin.BottomStart,
        RevealOrigin.BottomCenter,
        RevealOrigin.BottomEnd
    )

    DemoCard(
        title = "Staggered Grid Animation",
        description = "Items animate with directional physics based on reveal origin"
    ) {
        Column {
            // Origin selector
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(origins) { origin ->
                    FilterChip(
                        onClick = {
                        currentOrigin = origin
                        if (isRevealed) {
                            isRevealed = false
                        }
                    }, label = {
                        Text(
                            text = origin.name.replace("([A-Z])".toRegex(), " $1").trim(),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }, selected = currentOrigin == origin
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { isRevealed = !isRevealed }) {
                    Text(if (isRevealed) "Reset" else "Animate")
                }

                Text(
                    text = "Origin: ${currentOrigin.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Animated grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                modifier = Modifier.height(200.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(24) { index ->
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .animateItemReveal(
                                parentRevealed = rememberUpdatedState(isRevealed),
                                itemIndex = index,
                                revealOrigin = currentOrigin,
                                physicsSpec = ItemPhysicsSpec.StrongBounce,
                                staggerDelayMs = 50L
                            )
                            .background(
                                MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp)
                            )
                    )
                }
            }
        }
    }
}

// 4. Advanced Features Showcase
@Composable
private fun AdvancedFeaturesShowcase() {
    LazyColumn(
        contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            NestedRevealDemo()
        }
        item {
            CustomPhysicsDemo()
        }
        item {
            ComplexLayoutDemo()
        }
    }
}

@Composable
private fun NestedRevealDemo() {
    var outerVisible by remember { mutableStateOf(false) }
    var innerVisible by remember { mutableStateOf(false) }

    DemoCard(
        title = "Nested Reveals",
        description = "Multiple reveal layers with different origins and timing"
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = { outerVisible = !outerVisible }) {
                    Text("Outer")
                }
                Button(onClick = { innerVisible = !innerVisible }) {
                    Text("Inner")
                }
                Button(onClick = {
                    outerVisible = false
                    innerVisible = false
                }) {
                    Text("Reset")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)
                    ), contentAlignment = Alignment.Center
            ) {
                // Outer reveal
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .circularReveal(
                            visible = outerVisible,
                            origin = RevealOrigin.TopStart,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessMedium
                            )
                        )
                        .background(
                            MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)
                        ), contentAlignment = Alignment.Center
                ) {
                    // Inner reveal
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .circularReveal(
                                visible = innerVisible,
                                origin = RevealOrigin.BottomEnd,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                            .background(
                                MaterialTheme.colorScheme.secondary, RoundedCornerShape(8.dp)
                            ), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Inner",
                            color = MaterialTheme.colorScheme.onSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomPhysicsDemo() {
    var isRevealed by remember { mutableStateOf(false) }

    val customPhysics = ItemPhysicsSpec(
        initialScale = 0.3f,
        initialAlpha = 0f,
        initialTranslationMagnitude = 200f,
        rotationDegrees = 180f,
        springAnimationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessVeryLow
        )
    )

    DemoCard(
        title = "Custom Physics Spec",
        description = "Extreme custom physics: 0.3x scale, 200px translation, 180° rotation"
    ) {
        Column {
            Button(
                onClick = { isRevealed = !isRevealed }) {
                Text(if (isRevealed) "Reset" else "Demo Custom Physics")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.height(180.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(9) { index ->
                    Card(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .animateItemReveal(
                                parentRevealed = rememberUpdatedState(isRevealed),
                                itemIndex = index,
                                revealOrigin = RevealOrigin.Center,
                                physicsSpec = customPhysics,
                                staggerDelayMs = 150L
                            ), colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ComplexLayoutDemo() {
    var isRevealed by remember { mutableStateOf(false) }

    DemoCard(
        title = "Complex Layout",
        description = "Real-world example with mixed content and reveal animations"
    ) {
        Column {
            Button(
                onClick = { isRevealed = !isRevealed }) {
                Text("Toggle Dashboard")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dashboard-style layout
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.height(300.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header cards
                item {
                    DashboardCard(
                        title = "Revenue",
                        value = "$12.5K",
                        icon = Icons.Default.Star,
                        isRevealed = isRevealed,
                        index = 0
                    )
                }
                item {
                    DashboardCard(
                        title = "Users",
                        value = "1,240",
                        icon = Icons.Default.Person,
                        isRevealed = isRevealed,
                        index = 1
                    )
                }
                item {
                    DashboardCard(
                        title = "Orders",
                        value = "89",
                        icon = Icons.Default.ShoppingCart,
                        isRevealed = isRevealed,
                        index = 2
                    )
                }
                item {
                    DashboardCard(
                        title = "Growth",
                        value = "+23%",
                        icon = Icons.Default.Check,
                        isRevealed = isRevealed,
                        index = 3
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardCard(
    title: String, value: String, icon: ImageVector, isRevealed: Boolean, index: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .animateItemReveal(
                parentRevealed = rememberUpdatedState(isRevealed),
                itemIndex = index,
                revealOrigin = RevealOrigin.TopStart,
                physicsSpec = ItemPhysicsSpec.GentleEntrance,
                staggerDelayMs = 100L
            ), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

// Helper Components
@Composable
private fun DemoCard(
    title: String, description: String, content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )
            content()
        }
    }
}

// Dark Theme
@Composable
private fun ShowcaseDarkTheme(
    content: @Composable () -> Unit
) {
    val darkColorScheme = darkColorScheme(
        primary = Color(0xFF90CAF9),
        onPrimary = Color(0xFF003C71),
        primaryContainer = Color(0xFF004B8C),
        onPrimaryContainer = Color(0xFFD6E3FF),
        secondary = Color(0xFF80CBC4),
        onSecondary = Color(0xFF003833),
        secondaryContainer = Color(0xFF00504A),
        onSecondaryContainer = Color(0xFF9CF2E9),
        tertiary = Color(0xFFF8BBD0),
        onTertiary = Color(0xFF4A0E42),
        tertiaryContainer = Color(0xFF633B5C),
        onTertiaryContainer = Color(0xFFFFDAF1),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),
        background = Color(0xFF0F1419),
        onBackground = Color(0xFFE4E2E6),
        surface = Color(0xFF1F1F23),
        onSurface = Color(0xFFE4E2E6),
        surfaceVariant = Color(0xFF2F2F37),
        onSurfaceVariant = Color(0xFFC7C5D0),
        outline = Color(0xFF918F9A),
        outlineVariant = Color(0xFF46464F),
        scrim = Color(0xFF000000),
        inverseSurface = Color(0xFFE4E2E6),
        inverseOnSurface = Color(0xFF313033),
        inversePrimary = Color(0xFF0061A4),
        surfaceDim = Color(0xFF131316),
        surfaceBright = Color(0xFF39393C),
        surfaceContainerLowest = Color(0xFF0E0E11),
        surfaceContainerLow = Color(0xFF1B1B1E),
        surfaceContainer = Color(0xFF1F1F23),
        surfaceContainerHigh = Color(0xFF2A2A2D),
        surfaceContainerHighest = Color(0xFF353538)
    )

    MaterialTheme(
        colorScheme = darkColorScheme, content = content
    )
}

// Preview
@Preview(showBackground = true)
@Composable
private fun CircularRevealShowcasePreview() {
    CircularRevealShowcase()
}