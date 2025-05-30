package com.example.circularreveal.ui.chat

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import com.example.circularreveal.composereveal.animation.ItemPhysicsSpec
import com.example.circularreveal.composereveal.animation.RevealOrigin
import com.example.circularreveal.composereveal.modifiers.animateItemReveal
import com.example.circularreveal.composereveal.modifiers.circularReveal
import com.example.circularreveal.icons.*

// Design System
object ChatDesignSystem {

    object Spacing {
        val ExtraSmall = 4.dp
        val Small = 8.dp
        val Medium = 16.dp
        val Large = 24.dp
        val ExtraLarge = 32.dp
    }

    object Radius {
        val Small = 8.dp
        val Medium = 16.dp
        val Large = 24.dp
        val ExtraLarge = 32.dp
    }

    object Elevation {
        val Small = 2.dp
        val Medium = 4.dp
        val Large = 8.dp
        val ExtraLarge = 12.dp
    }

    object Sizes {
        val MessageBubbleMaxWidth = 280.dp
        val InputBarHeight = 72.dp
        val AttachmentIconSize = 48.dp
        val AttachmentIconInnerSize = 24.dp
        val ActionIconSize = 20.dp
        val SendButtonSize = 48.dp
        val AttachmentOverlayHeight = 340.dp
        val ActionMenuWidth = 200.dp
        val ActionMenuApproxHeight = 220.dp
    }
}

// Data Models
data class ChatMessage(
    val id: Int,
    val text: String,
    val isSentByUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class AttachmentOption(
    val icon: ImageVector,
    val label: String,
    val color: Color,
    val contentDescription: String
)

data class MessageAction(
    val icon: ImageVector,
    val label: String,
    val isDestructive: Boolean = false
)

// UI State
@Stable
data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isAttachmentMenuVisible: Boolean = false,
    val isMessageActionsVisible: Boolean = false,
    val selectedMessageId: Int? = null,
    val inputText: String = ""
)

// Main Chat Screen
@Composable
fun ChatScreenDemo(
    modifier: Modifier = Modifier
) {
    var uiState by remember {
        mutableStateOf(
            ChatUiState(
                messages = getSampleMessages()
            )
        )
    }

    var rootLayoutSize by remember { mutableStateOf(Size.Zero) }
    var selectedMessageGeometry by remember { mutableStateOf(MessageGeometry()) }
    var attachmentGeometry by remember { mutableStateOf(AttachmentGeometry()) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .onGloballyPositioned { coordinates ->
                rootLayoutSize = coordinates.size.toSize()
            }
    ) {
        // Messages List
        ChatMessagesList(
            messages = uiState.messages,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ChatDesignSystem.Spacing.Medium)
                .padding(
                    bottom = ChatDesignSystem.Sizes.InputBarHeight + ChatDesignSystem.Spacing.Small,
                    top = ChatDesignSystem.Spacing.Medium
                ),
            onMessageLongPress = { message, geometry ->
                selectedMessageGeometry = geometry
                uiState = uiState.copy(
                    selectedMessageId = message.id,
                    isMessageActionsVisible = true
                )
            }
        )

        // Input Bar
        ChatInputBar(
            value = uiState.inputText,
            onValueChange = { uiState = uiState.copy(inputText = it) },
            onSendMessage = { text ->
                if (text.isNotBlank()) {
                    val newMessage = ChatMessage(
                        id = uiState.messages.size + 1,
                        text = text,
                        isSentByUser = true
                    )
                    uiState = uiState.copy(
                        messages = uiState.messages + newMessage,
                        inputText = ""
                    )
                }
            },
            onAttachmentClick = { geometry ->
                attachmentGeometry = geometry
                uiState = uiState.copy(isAttachmentMenuVisible = !uiState.isAttachmentMenuVisible)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .zIndex(1f)
        )

        // Attachment Menu Overlay
        AttachmentMenuOverlay(
            isVisible = uiState.isAttachmentMenuVisible,
            geometry = attachmentGeometry,
            onOptionSelected = {
                // Handle attachment option selection
                uiState = uiState.copy(isAttachmentMenuVisible = false)
            },
            onDismiss = {
                uiState = uiState.copy(isAttachmentMenuVisible = false)
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // Message Actions Menu
        MessageActionsMenu(
            isVisible = uiState.isMessageActionsVisible,
            selectedMessage = uiState.messages.find { it.id == uiState.selectedMessageId },
            messageGeometry = selectedMessageGeometry,
            rootLayoutSize = rootLayoutSize,
            onActionSelected = { action ->
                // Handle message action
                uiState = uiState.copy(isMessageActionsVisible = false)
            },
            onDismiss = {
                uiState = uiState.copy(isMessageActionsVisible = false)
            }
        )
    }
}

// Geometry Data Classes
@Stable
data class MessageGeometry(
    val globalPosition: Offset = Offset.Zero,
    val size: Size = Size.Zero
)

@Stable
data class AttachmentGeometry(
    val iconPosition: Offset = Offset.Zero,
    val containerSize: Size = Size.Zero
)

// Messages List Component
@Composable
private fun ChatMessagesList(
    messages: List<ChatMessage>,
    onMessageLongPress: (ChatMessage, MessageGeometry) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(ChatDesignSystem.Spacing.Small),
        reverseLayout = true
    ) {
        items(
            items = messages.reversed(),
            key = { it.id }
        ) { message ->
            MessageBubble(
                message = message,
                onLongPress = onMessageLongPress
            )
        }
    }
}

// Message Bubble Component
@Composable
private fun MessageBubble(
    message: ChatMessage,
    onLongPress: (ChatMessage, MessageGeometry) -> Unit,
    modifier: Modifier = Modifier
) {
    var messageGeometry by remember { mutableStateOf(MessageGeometry()) }

    val bubbleAlignment = if (message.isSentByUser) {
        Alignment.CenterEnd
    } else {
        Alignment.CenterStart
    }

    val bubbleColors = if (message.isSentByUser) {
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    } else {
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }

    val textColor = if (message.isSentByUser) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = bubbleAlignment
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = ChatDesignSystem.Sizes.MessageBubbleMaxWidth)
                .onGloballyPositioned { coordinates ->
                    messageGeometry = MessageGeometry(
                        globalPosition = coordinates.positionInWindow(),
                        size = coordinates.size.toSize()
                    )
                }
                .combinedClickable(
                    onClick = { /* Handle tap */ },
                    onLongClick = {
                        onLongPress(message, messageGeometry)
                    }
                )
                .semantics {
                    contentDescription = if (message.isSentByUser) {
                        "Sent message: ${message.text}"
                    } else {
                        "Received message: ${message.text}"
                    }
                    role = Role.Button
                },
            shape = RoundedCornerShape(ChatDesignSystem.Radius.Medium),
            colors = bubbleColors,
            elevation = CardDefaults.cardElevation(
                defaultElevation = ChatDesignSystem.Elevation.Small
            )
        ) {
            Text(
                text = message.text,
                color = textColor,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(ChatDesignSystem.Spacing.Medium)
            )
        }
    }
}

// Input Bar Component
@Composable
private fun ChatInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSendMessage: (String) -> Unit,
    onAttachmentClick: (AttachmentGeometry) -> Unit,
    modifier: Modifier = Modifier
) {
    var attachmentGeometry by remember { mutableStateOf(AttachmentGeometry()) }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = ChatDesignSystem.Elevation.Large,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ChatDesignSystem.Spacing.Small)
                .onGloballyPositioned { coordinates ->
                    attachmentGeometry = attachmentGeometry.copy(
                        containerSize = coordinates.size.toSize()
                    )
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ChatDesignSystem.Spacing.Small)
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .semantics {
                        contentDescription = "Message input field"
                    },
                placeholder = {
                    Text(
                        text = "Type a message...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                shape = RoundedCornerShape(ChatDesignSystem.Radius.Large),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            IconButton(
                onClick = {
                    onAttachmentClick(attachmentGeometry)
                },
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        val iconPosition = coordinates.positionInParent()
                        attachmentGeometry = attachmentGeometry.copy(
                            iconPosition = Offset(
                                x = iconPosition.x + coordinates.size.width / 2,
                                y = iconPosition.y
                            )
                        )
                    }
                    .semantics {
                        contentDescription = "Attach file"
                        role = Role.Button
                    }
            ) {
                Icon(
                    imageVector = AttachFile,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            FilledIconButton(
                onClick = {
                    onSendMessage(value)
                },
                modifier = Modifier
                    .size(ChatDesignSystem.Sizes.SendButtonSize)
                    .semantics {
                        contentDescription = "Send message"
                        role = Role.Button
                    },
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

// Attachment Menu Overlay
@Composable
private fun AttachmentMenuOverlay(
    isVisible: Boolean,
    geometry: AttachmentGeometry,
    onOptionSelected: (AttachmentOption) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val bottomPadding = with(density) {
        ChatDesignSystem.Sizes.InputBarHeight.toPx() + ChatDesignSystem.Spacing.Small.toPx()
    }
    val horizontalPadding = with(density) { ChatDesignSystem.Spacing.Medium.toPx() }

    val adjustedIconPosition = Offset(
        x = geometry.iconPosition.x + horizontalPadding,
        y = geometry.iconPosition.y + bottomPadding
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(ChatDesignSystem.Sizes.AttachmentOverlayHeight)
            .padding(horizontal = ChatDesignSystem.Spacing.Medium)
            .padding(bottom = ChatDesignSystem.Sizes.InputBarHeight)
    ) {
        Box(
            modifier = Modifier
                .circularReveal(
                    visible = isVisible,
                    elementPosition = adjustedIconPosition,
                    containerSize = geometry.containerSize,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessVeryLow
                    )
                )
                .background(
                    Color.Black.copy(alpha = 0.94f),
                    RoundedCornerShape(ChatDesignSystem.Radius.Large)
                )
                .clickable { onDismiss() }
                ,
            contentAlignment = Alignment.Center
        ) {
            AttachmentOptionsGrid(
                parentRevealed = rememberUpdatedState(isVisible),
                onOptionSelected = onOptionSelected
            )
        }
    }
}

// Attachment Options Grid
@Composable
private fun AttachmentOptionsGrid(
    parentRevealed: State<Boolean>,
    onOptionSelected: (AttachmentOption) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = remember {
        listOf(
            AttachmentOption(
                Image, "Gallery", Color(0xFF4CAF50), "Select from gallery"
            ),
            AttachmentOption(
                Camera, "Camera", Color(0xFFE91E63), "Take photo"
            ),
            AttachmentOption(
                Icons.Default.LocationOn, "Location", Color(0xFF2196F3), "Share location"
            ),
            AttachmentOption(
                Icons.Default.Person, "Contact", Color(0xFF9C27B0), "Share contact"
            ),
            AttachmentOption(
                Description, "Document", Color(0xFF795548), "Attach document"
            ),
            AttachmentOption(
                Audiotrack, "Audio", Color(0xFFFF5722), "Record audio"
            )
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(ChatDesignSystem.Spacing.Large),
        verticalArrangement = Arrangement.spacedBy(ChatDesignSystem.Spacing.Large),
        modifier = modifier
            .fillMaxSize()
            .padding(ChatDesignSystem.Spacing.ExtraLarge)
    ) {
        itemsIndexed(options) { index, option ->
            AttachmentOptionItem(
                option = option,
                onClick = { onOptionSelected(option) },
                modifier = Modifier.animateItemReveal(
                    parentRevealed = parentRevealed,
                    itemIndex = index,
                    revealOrigin = RevealOrigin.BottomCenter,
                    physicsSpec = ItemPhysicsSpec.StrongBounce,
                )
            )
        }
    }
}

// Attachment Option Item
@Composable
private fun AttachmentOptionItem(
    option: AttachmentOption,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable { onClick() }
            .padding(ChatDesignSystem.Spacing.Small)
            .semantics {
                contentDescription = option.contentDescription
                role = Role.Button
            }
    ) {
        Box(
            modifier = Modifier
                .size(ChatDesignSystem.Sizes.AttachmentIconSize)
                .background(
                    color = option.color.copy(alpha = 0.15f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = option.icon,
                contentDescription = null,
                modifier = Modifier.size(ChatDesignSystem.Sizes.AttachmentIconInnerSize),
                tint = option.color
            )
        }
        Spacer(modifier = Modifier.height(ChatDesignSystem.Spacing.Small))
        Text(
            text = option.label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.White
        )
    }
}

// Message Actions Menu
@Composable
private fun MessageActionsMenu(
    isVisible: Boolean,
    selectedMessage: ChatMessage?,
    messageGeometry: MessageGeometry,
    rootLayoutSize: Size,
    onActionSelected: (MessageAction) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Always render the composable to preserve state, but use the reveal animation to show/hide
    val density = LocalDensity.current
    val menuPosition = if (selectedMessage != null) {
        calculateMenuPosition(
            messageGeometry = messageGeometry,
            rootLayoutSize = rootLayoutSize,
            isSentByUser = selectedMessage.isSentByUser,
            density = density
        )
    } else {
        androidx.compose.ui.unit.DpOffset(0.dp, 0.dp)
    }

    var actualMenuSize by remember { mutableStateOf(Size.Zero) }

    val revealOrigin = if (selectedMessage?.isSentByUser == true) {
        Offset(x = actualMenuSize.width, y = 0f)
    } else {
        Offset(x = 0f, y = 0f)
    }

    if (selectedMessage != null) {
        Box(
            modifier = modifier
                .offset(x = menuPosition.x, y = menuPosition.y)
                .onGloballyPositioned { coordinates ->
                    actualMenuSize = coordinates.size.toSize()
                }
                .circularReveal(
                    visible = isVisible,
                    elementPosition = revealOrigin,
                    containerSize = actualMenuSize,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                )
        ) {
            MessageActionsCard(
                parentRevealed = rememberUpdatedState(isVisible),
                onActionSelected = onActionSelected
            )
        }
    }
}

// Message Actions Card
@Composable
private fun MessageActionsCard(
    parentRevealed: State<Boolean>,
    onActionSelected: (MessageAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val actions = remember {
        listOf(
            MessageAction(Reply, "Reply"),
            MessageAction(ContentCopy, "Copy"),
            MessageAction(Description, "Forward"),
            MessageAction(Icons.Default.Delete, "Delete", isDestructive = true)
        )
    }

    Card(
        modifier = modifier.widthIn(
            min = 160.dp,
            max = ChatDesignSystem.Sizes.ActionMenuWidth
        ),
        shape = RoundedCornerShape(ChatDesignSystem.Radius.Medium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = ChatDesignSystem.Elevation.ExtraLarge
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = ChatDesignSystem.Spacing.Small)
        ) {
            actions.forEachIndexed { index, action ->
                MessageActionItem(
                    action = action,
                    onClick = { onActionSelected(action) },
                    modifier = Modifier.animateItemReveal(
                        parentRevealed = parentRevealed,
                        itemIndex = index,
                        revealOrigin = RevealOrigin.Center,
                        physicsSpec = ItemPhysicsSpec.GentleEntrance,
                        staggerDelayMs = 50L
                    )
                )
            }
        }
    }
}

// Message Action Item
@Composable
private fun MessageActionItem(
    action: MessageAction,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconColor = if (action.isDestructive) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val textColor = if (action.isDestructive) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(
                horizontal = ChatDesignSystem.Spacing.Medium,
                vertical = ChatDesignSystem.Spacing.Small
            )
            .semantics {
                contentDescription = action.label
                role = Role.Button
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ChatDesignSystem.Spacing.Small)
    ) {
        Icon(
            imageVector = action.icon,
            contentDescription = null,
            modifier = Modifier.size(ChatDesignSystem.Sizes.ActionIconSize),
            tint = iconColor
        )
        Text(
            text = action.label,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor
        )
    }
}

// Helper Functions
private fun calculateMenuPosition(
    messageGeometry: MessageGeometry,
    rootLayoutSize: Size,
    isSentByUser: Boolean,
    density: androidx.compose.ui.unit.Density
): androidx.compose.ui.unit.DpOffset {
    with(density) {
        val bubbleX = messageGeometry.globalPosition.x.toDp()
        val bubbleY = messageGeometry.globalPosition.y.toDp()
        val bubbleWidth = messageGeometry.size.width.toDp()
        val bubbleHeight = messageGeometry.size.height.toDp()
        val bubbleCenterY = bubbleY + bubbleHeight / 2

        val screenWidth = rootLayoutSize.width.toDp()
        val screenHeight = rootLayoutSize.height.toDp()

        val menuWidth = ChatDesignSystem.Sizes.ActionMenuWidth
        val menuHeight = ChatDesignSystem.Sizes.ActionMenuApproxHeight
        val padding = ChatDesignSystem.Spacing.Medium

        val menuX = if (isSentByUser) {
            (bubbleX - menuWidth - padding / 2)
        } else {
            (bubbleX + bubbleWidth + padding / 2)
        }

        val menuY = bubbleCenterY - ChatDesignSystem.Spacing.Large

        val coercedX = menuX.coerceIn(padding, screenWidth - menuWidth - padding)
        val coercedY = menuY.coerceIn(padding, screenHeight - menuHeight - padding)

        return androidx.compose.ui.unit.DpOffset(coercedX, coercedY)
    }
}

private fun getSampleMessages(): List<ChatMessage> {
    return listOf(
        ChatMessage(1, "Hey! How's it going?", false),
        ChatMessage(2, "Great! Just testing some new animations", true),
        ChatMessage(3, "They look really smooth! Love the circular reveals", false),
        ChatMessage(4, "Thanks! The physics feel natural too", true),
        ChatMessage(5, "Can you show me the attachment menu?", false),
    )
}

// Dark Theme
@Composable
private fun ChatDarkTheme(
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
        colorScheme = darkColorScheme,
        content = content
    )
}

// Preview
@Preview(showBackground = true)
@Composable
private fun ChatScreenDemoPreview() {
    ChatDarkTheme {
        ChatScreenDemo()
    }
}