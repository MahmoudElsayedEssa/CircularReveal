# CircularRevealCompose

**A physics-based circular reveal animation system for Jetpack Compose**

*An experimental Android project exploring advanced animation techniques with custom modifiers and spring-driven physics*

---

## Screenshots



https://github.com/user-attachments/assets/69f624b4-ce44-4c72-81d7-b54a4e2b210b

https://github.com/user-attachments/assets/7329d589-a1f0-4387-84e3-814e9de7aa83


## Project Overview

This project demonstrates a sophisticated circular reveal animation system built from scratch for Jetpack Compose. UI elements can reveal with fluid physics-based animations from any origin point, with directional item animations and customizable spring configurations.

**Built as a personal exploration of:**
- Advanced Jetpack Compose animation APIs and custom modifier creation
- Physics-based UI transitions with spring dynamics
- Complex geometry calculations for position-based animations  
- State management in animated UI systems
- Material Design motion principles and implementation

## Features

### Core Functionality
- **9 Reveal Origins** - Animate from any corner, center, or edge position with precise control
- **Physics-Based Item Animations** - Spring-driven entrance effects with scale, translation, and rotation
- **Touch-Based Reveals** - Dynamic reveal animations starting from exact touch coordinates
- **Element-Based Reveals** - Animations triggered from specific UI element positions  
- **Staggered Animation System** - Directional item entrance with configurable delays and physics
- **Custom Animation Specs** - Full control over spring physics, damping, and stiffness

### Technical Implementation
- **Custom Compose Modifiers** - `Modifier.circularReveal()` and `Modifier.animateItemReveal()` for reusable animations
- **Geometry Math System** - Coordinate transformations and radius calculations for precise reveals
- **Spring Physics Engine** - Custom physics configurations with realistic bounce and damping
- **State-Driven Animations** - Reactive animation system with proper state management
- **Performance Optimization** - Layout removal strategies and memory-efficient rendering

## Project Structure

```
└── composereveal/                                # Core animation system
    └── src/main/java/com/example/circularreveal/composereveal/
        ├── animation/
        │   ├── RevealOrigin.kt                   # 9 reveal position definitions
        │   ├── ItemPhysicsSpec.kt                # Physics presets and configuration
        │   └── CircularRevealSpec.kt             # Animation specification
        ├── modifiers/
        │   ├── circularReveal.kt                 # Core reveal modifier implementation
        │   ├── circularRevealFromTouch.kt        # Touch-based reveal extensions
        │   └── animateItemReveal.kt              # Item physics animation system
        └── util/
            └── AnimationUtils.kt                 # Geometry calculations and utilities
```

## How It Works

### Basic Circular Reveal

```kotlin
@Composable
fun MyScreen() {
    var isVisible by remember { mutableStateOf(false) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .circularReveal(
                visible = isVisible,
                origin = RevealOrigin.TopStart,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Text("Content revealed with physics!")
    }
}
```

### Touch-Based Interactive Reveals

```kotlin
@Composable
fun TouchRevealExample() {
    var isVisible by remember { mutableStateOf(false) }
    var touchPosition by remember { mutableStateOf(Offset.Zero) }
    var containerSize by remember { mutableStateOf(Size.Zero) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { containerSize = it.size.toSize() }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    touchPosition = offset
                    isVisible = !isVisible
                }
            }
            .circularRevealFromTouch(
                visible = isVisible,
                touchPosition = touchPosition,
                containerSize = containerSize
            )
            .background(MaterialTheme.colorScheme.secondary)
    )
}
```

### Staggered Item Animations

```kotlin
@Composable
fun PhysicsGridExample() {
    var isRevealed by remember { mutableStateOf(false) }
    
    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        items(9) { index ->
            Card(
                modifier = Modifier
                    .animateItemReveal(
                        parentRevealed = rememberUpdatedState(isRevealed),
                        itemIndex = index,
                        revealOrigin = RevealOrigin.BottomCenter,
                        physicsSpec = ItemPhysicsSpec.StrongBounce,
                        staggerDelayMs = 100L
                    )
            ) {
                Text("Item ${index + 1}")
            }
        }
    }
}
```

## Key Technical Challenges Solved

### 1. Physics-Based Animation System
- Custom spring configurations with realistic damping and stiffness values
- Directional translation calculations based on reveal origin
- Coordinated scale, alpha, translation, and rotation animations
- Non-linear interpolation curves for natural movement

### 2. Geometry and Coordinate Systems  
```kotlin
// Complex coordinate transformations for position-based reveals
internal fun Offset.mapToSize(size: Size): Offset {
    return Offset(
        x = this.x * size.width,
        y = this.y * size.height
    )
}

internal fun calculateMaxRadius(normalizedOrigin: Offset, size: Size): Float {
    val x = if (normalizedOrigin.x > 0.5f) normalizedOrigin.x else (1f - normalizedOrigin.x)
    val y = if (normalizedOrigin.y > 0.5f) normalizedOrigin.y else (1f - normalizedOrigin.y)
    return sqrt((x * size.width).pow(2) + (y * size.height).pow(2))
}
```

### 3. Advanced State Management
- Reactive animation states with proper lifecycle management
- Complex UI state coordination across multiple animated elements
- Performance optimization with conditional layout removal
- Thread-safe state updates with proper synchronization

### 4. Custom Modifier Architecture
```kotlin
// Reusable modifier system with clean API design
fun Modifier.circularReveal(
    visible: Boolean,
    origin: RevealOrigin = RevealOrigin.Center,
    animationSpec: AnimationSpec<Float> = defaultSpring(),
    hideWhenInvisible: Boolean = true
): Modifier = composed {
    val transition = updateTransition(visible, label = "CircularReveal")
    val progress = transition.animateFloat(
        label = "RevealProgress",
        transitionSpec = { animationSpec }
    ) { isVisible -> if (isVisible) 1f else 0f }
    
    circularRevealWithProgress(progress, origin.toOffset(), hideWhenInvisible)
}
```
