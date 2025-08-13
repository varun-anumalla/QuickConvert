package com.varun.quickconvert

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

// Colors specific to this screen
private val SpeedScreenBackgroundColor = Color(0xFFF0F0F3)
private val SpeedOperatorButtonColor = Color(0xFFFF9F0A)
private val SpeedDisplayCardBackgroundColor = Color(0xFFE8E8E8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeedScreen(
    navController: NavController,
    viewModel: SpeedViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showFromMenu by remember { mutableStateOf(false) }
    var showToMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        containerColor = SpeedScreenBackgroundColor,
        topBar = {
            TopAppBar(
                title = { Text("Speed Conversion", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SpeedScreenBackgroundColor)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // --- DISPLAY SECTION ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                Box {
                    SpeedConversionDisplay(
                        unit = uiState.fromUnit,
                        value = uiState.fromValue,
                        isActive = uiState.isFromFieldActive,
                        onFieldClick = { viewModel.onEvent(SpeedEvent.SetFromFieldActive) },
                        onUnitClick = { showFromMenu = true }
                    )
                    SpeedUnitSelectionMenu(
                        expanded = showFromMenu,
                        onDismiss = { showFromMenu = false },
                        onUnitSelected = { unit ->
                            viewModel.onEvent(SpeedEvent.ChangeFromUnit(unit))
                            showFromMenu = false
                        }
                    )
                }
                Spacer(Modifier.height(16.dp))
                Box {
                    SpeedConversionDisplay(
                        unit = uiState.toUnit,
                        value = uiState.toValue,
                        isActive = !uiState.isFromFieldActive,
                        onFieldClick = { viewModel.onEvent(SpeedEvent.SetToFieldActive) },
                        onUnitClick = { showToMenu = true }
                    )
                    SpeedUnitSelectionMenu(
                        expanded = showToMenu,
                        onDismiss = { showToMenu = false },
                        onUnitSelected = { unit ->
                            viewModel.onEvent(SpeedEvent.ChangeToUnit(unit))
                            showToMenu = false
                        }
                    )
                }
            }

            // Flexible spacer to push keypad to the bottom
            Spacer(Modifier.weight(1f))

            // --- KEYPAD SECTION ---
            val buttonSpacing = 12.dp
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                // Number grid on the left
                Column(
                    modifier = Modifier.weight(3f),
                    verticalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                        SpeedKeypadButton(symbol = "7", modifier = Modifier.weight(1f)) { viewModel.onEvent(SpeedEvent.NumberPressed("7", context)) }
                        SpeedKeypadButton(symbol = "8", modifier = Modifier.weight(1f)) { viewModel.onEvent(SpeedEvent.NumberPressed("8", context)) }
                        SpeedKeypadButton(symbol = "9", modifier = Modifier.weight(1f)) { viewModel.onEvent(SpeedEvent.NumberPressed("9", context)) }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                        SpeedKeypadButton(symbol = "4", modifier = Modifier.weight(1f)) { viewModel.onEvent(SpeedEvent.NumberPressed("4", context)) }
                        SpeedKeypadButton(symbol = "5", modifier = Modifier.weight(1f)) { viewModel.onEvent(SpeedEvent.NumberPressed("5", context)) }
                        SpeedKeypadButton(symbol = "6", modifier = Modifier.weight(1f)) { viewModel.onEvent(SpeedEvent.NumberPressed("6", context)) }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                        SpeedKeypadButton(symbol = "1", modifier = Modifier.weight(1f)) { viewModel.onEvent(SpeedEvent.NumberPressed("1", context)) }
                        SpeedKeypadButton(symbol = "2", modifier = Modifier.weight(1f)) { viewModel.onEvent(SpeedEvent.NumberPressed("2", context)) }
                        SpeedKeypadButton(symbol = "3", modifier = Modifier.weight(1f)) { viewModel.onEvent(SpeedEvent.NumberPressed("3", context)) }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                        SpeedKeypadButton(symbol = "0", modifier = Modifier.weight(2f), shape = RoundedCornerShape(50.dp)) { viewModel.onEvent(SpeedEvent.NumberPressed("0", context)) }
                        SpeedKeypadButton(symbol = ".", modifier = Modifier.weight(1f)) { viewModel.onEvent(SpeedEvent.DecimalPressed) }
                    }
                }
                // Action buttons on the right
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    SpeedKeypadButton(symbol = "AC", modifier = Modifier.weight(1f), color = SpeedOperatorButtonColor, textColor = Color.White, shape = RoundedCornerShape(50.dp)) { viewModel.onEvent(SpeedEvent.ClearPressed) }
                    SpeedKeypadIconButton(icon = Icons.AutoMirrored.Filled.Backspace, modifier = Modifier.weight(1f), color = SpeedOperatorButtonColor, tint = Color.White, shape = RoundedCornerShape(50.dp)) { viewModel.onEvent(SpeedEvent.BackspacePressed) }
                }
            }
            // Fixed spacer for bottom padding
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SpeedConversionDisplay(
    unit: SpeedUnit,
    value: String,
    isActive: Boolean,
    onFieldClick: () -> Unit,
    onUnitClick: () -> Unit
) {
    val borderColor = if (isActive) MaterialTheme.colorScheme.primary else Color.Black.copy(alpha = 0.5f)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(onClick = onFieldClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SpeedDisplayCardBackgroundColor),
        border = BorderStroke(if (isActive) 2.dp else 1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onUnitClick),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "${unit.displayName} (${unit.symbol})",
                    style = MaterialTheme.typography.bodyMedium
                )
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Select unit")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                SpeedAutoResizeText(
                    text = if (value.isEmpty()) "0" else value,
                    style = TextStyle(
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    )
                )
                if (isActive) {
                    Spacer(Modifier.width(4.dp))
                    SpeedBlinkingCursor(height = 40.dp)
                }
            }
        }
    }
}

@Composable
private fun SpeedUnitSelectionMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onUnitSelected: (SpeedUnit) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = Modifier.background(SpeedDisplayCardBackgroundColor, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Select unit", style = MaterialTheme.typography.titleSmall)
            IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }
        HorizontalDivider()
        SpeedUnit.entries.forEach { unit ->
            DropdownMenuItem(
                text = { Text("${unit.displayName} (${unit.symbol})") },
                onClick = { onUnitSelected(unit) }
            )
        }
    }
}

@Composable
private fun SpeedKeypadButton(
    symbol: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    textColor: Color = Color.Black,
    shape: Shape = CircleShape,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.70f else 1f, label = "scale")

    Card(
        modifier = modifier
            .scale(scale)
            .then(if (shape == CircleShape) Modifier.aspectRatio(1f) else Modifier)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = symbol, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = textColor)
        }
    }
}

@Composable
private fun SpeedKeypadIconButton(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    tint: Color = Color.Black,
    shape: Shape = CircleShape,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.70f else 1f, label = "scale")

    Card(
        modifier = modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun SpeedBlinkingCursor(height: Dp) {
    val infiniteTransition = rememberInfiniteTransition(label = "cursor_blink")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse
        ), label = "cursor_alpha"
    )
    Box(
        modifier = Modifier
            .width(2.dp)
            .height(height)
            .background(Color.DarkGray.copy(alpha = alpha))
    )
}

@Composable
private fun SpeedAutoResizeText(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    minFontSize: TextUnit = 12.sp
) {
    val textMeasurer = rememberTextMeasurer()
    var resizedFontSize by remember(text) { mutableStateOf(style.fontSize) }

    BoxWithConstraints(modifier = modifier) {
        LaunchedEffect(text, constraints.maxWidth) {
            var currentFontSize = style.fontSize
            var textLayoutResult = textMeasurer.measure(text, style.copy(fontSize = currentFontSize))
            while (textLayoutResult.size.width > constraints.maxWidth && currentFontSize > minFontSize) {
                currentFontSize *= 0.95f
                textLayoutResult = textMeasurer.measure(text, style.copy(fontSize = currentFontSize))
            }
            resizedFontSize = currentFontSize
        }
        Text(
            text = text,
            style = style.copy(fontSize = resizedFontSize),
            maxLines = 1,
            softWrap = false
        )
    }
}
