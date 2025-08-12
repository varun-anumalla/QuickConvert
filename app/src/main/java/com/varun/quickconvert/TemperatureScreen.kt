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

private val TempScreenBackgroundColor = Color(0xFFF0F0F3)
private val TempOperatorButtonColor = Color(0xFFFF9F0A)
private val TempDisplayCardBackgroundColor = Color(0xFFE8E8E8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemperatureScreen(
    navController: NavController,
    viewModel: TemperatureViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showFromMenu by remember { mutableStateOf(false) }
    var showToMenu by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = TempScreenBackgroundColor,
        topBar = {
            TopAppBar(
                title = { Text("Temperature Conversion", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TempScreenBackgroundColor)
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
                    TempConversionDisplay(
                        unit = uiState.fromUnit,
                        value = uiState.fromValue,
                        isActive = uiState.isFromFieldActive,
                        onFieldClick = { viewModel.onEvent(TemperatureEvent.SetFromFieldActive) },
                        onUnitClick = { showFromMenu = true }
                    )
                    TempUnitSelectionMenu(
                        expanded = showFromMenu,
                        onDismiss = { showFromMenu = false },
                        onUnitSelected = { unit ->
                            viewModel.onEvent(TemperatureEvent.ChangeFromUnit(unit))
                            showFromMenu = false
                        }
                    )
                }
                Spacer(Modifier.height(16.dp))
                Box {
                    TempConversionDisplay(
                        unit = uiState.toUnit,
                        value = uiState.toValue,
                        isActive = !uiState.isFromFieldActive,
                        onFieldClick = { viewModel.onEvent(TemperatureEvent.SetToFieldActive) },
                        onUnitClick = { showToMenu = true }
                    )
                    TempUnitSelectionMenu(
                        expanded = showToMenu,
                        onDismiss = { showToMenu = false },
                        onUnitSelected = { unit ->
                            viewModel.onEvent(TemperatureEvent.ChangeToUnit(unit))
                            showToMenu = false
                        }
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            // --- KEYPAD SECTION ---
            val buttonSpacing = 12.dp
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                Column(
                    modifier = Modifier.weight(3f),
                    verticalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                        TempKeypadButton(symbol = "7", modifier = Modifier.weight(1f)) { viewModel.onEvent(TemperatureEvent.NumberPressed("7")) }
                        TempKeypadButton(symbol = "8", modifier = Modifier.weight(1f)) { viewModel.onEvent(TemperatureEvent.NumberPressed("8")) }
                        TempKeypadButton(symbol = "9", modifier = Modifier.weight(1f)) { viewModel.onEvent(TemperatureEvent.NumberPressed("9")) }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                        TempKeypadButton(symbol = "4", modifier = Modifier.weight(1f)) { viewModel.onEvent(TemperatureEvent.NumberPressed("4")) }
                        TempKeypadButton(symbol = "5", modifier = Modifier.weight(1f)) { viewModel.onEvent(TemperatureEvent.NumberPressed("5")) }
                        TempKeypadButton(symbol = "6", modifier = Modifier.weight(1f)) { viewModel.onEvent(TemperatureEvent.NumberPressed("6")) }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                        TempKeypadButton(symbol = "1", modifier = Modifier.weight(1f)) { viewModel.onEvent(TemperatureEvent.NumberPressed("1")) }
                        TempKeypadButton(symbol = "2", modifier = Modifier.weight(1f)) { viewModel.onEvent(TemperatureEvent.NumberPressed("2")) }
                        TempKeypadButton(symbol = "3", modifier = Modifier.weight(1f)) { viewModel.onEvent(TemperatureEvent.NumberPressed("3")) }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                        TempKeypadButton(symbol = "0", modifier = Modifier.weight(1f)) { viewModel.onEvent(TemperatureEvent.NumberPressed("0")) }
                        TempKeypadButton(symbol = ".", modifier = Modifier.weight(1f)) { viewModel.onEvent(TemperatureEvent.DecimalPressed) }
                        TempKeypadButton(symbol = "+/-", modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.secondaryContainer) { viewModel.onEvent(TemperatureEvent.ToggleSignPressed) }
                    }
                }
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    TempKeypadButton(symbol = "AC", modifier = Modifier.weight(1f), color = TempOperatorButtonColor, textColor = Color.White, shape = RoundedCornerShape(50.dp)) { viewModel.onEvent(TemperatureEvent.ClearPressed) }
                    TempKeypadIconButton(icon = Icons.AutoMirrored.Filled.Backspace, modifier = Modifier.weight(1f), color = TempOperatorButtonColor, tint = Color.White, shape = RoundedCornerShape(50.dp)) { viewModel.onEvent(TemperatureEvent.BackspacePressed) }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Composable
private fun TempConversionDisplay(
    unit: TemperatureUnit,
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
        colors = CardDefaults.cardColors(containerColor = TempDisplayCardBackgroundColor),
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
                TempAutoResizeText(
                    text = if (value.isEmpty()) "0" else value,
                    style = TextStyle(
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    )
                )
                if (isActive) {
                    Spacer(Modifier.width(4.dp))
                    TempBlinkingCursor(height = 40.dp)
                }
            }
        }
    }
}

@Composable
private fun TempUnitSelectionMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onUnitSelected: (TemperatureUnit) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = Modifier.background(TempDisplayCardBackgroundColor, RoundedCornerShape(16.dp))
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
        TemperatureUnit.entries.forEach { unit ->
            DropdownMenuItem(
                text = { Text("${unit.displayName} (${unit.symbol})") },
                onClick = { onUnitSelected(unit) }
            )
        }
    }
}

@Composable
private fun TempKeypadButton(
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
private fun TempKeypadIconButton(
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
private fun TempBlinkingCursor(height: Dp) {
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
private fun TempAutoResizeText(
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
