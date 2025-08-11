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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
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

val ScreenBackgroundColor = Color(0xFFF0F0F3) // A soft, light gray background
val OperatorButtonColor = Color(0xFFFF9F0A)   // A vibrant orange
val DisplayCardBackgroundColor = Color(0xFFE8E8E8) // A slightly darker gray for the display

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    navController: NavController,
    viewModel: CalculatorViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = ScreenBackgroundColor,
        topBar = {
            TopAppBar(
                title = { Text("Calculator", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ScreenBackgroundColor,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Display Area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                // Equation display
                DisplayCard(height = 72.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AutoResizeText(
                            text = uiState.equation,
                            style = TextStyle(
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.DarkGray,
                                textAlign = TextAlign.End
                            )
                        )
                        if (!uiState.isCalculationDone) {
                            Spacer(modifier = Modifier.width(2.dp))
                            BlinkingCursor(height = 36.dp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                // Result display
                DisplayCard(height = 96.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AutoResizeText(
                            text = uiState.result,
                            style = TextStyle(
                                fontSize = 60.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                textAlign = TextAlign.End
                            )
                        )
                    }
                }
            }

            // Buttons Grid
            val buttonSpacing = 12.dp
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                    CalculatorButton(symbol = "AC", color = MaterialTheme.colorScheme.secondaryContainer, textColor = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.weight(1f), onClick = { viewModel.onEvent(CalculatorEvent.Clear) })
                    CalculatorButton(symbol = "%", color = MaterialTheme.colorScheme.secondaryContainer, textColor = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.weight(1f), onClick = { viewModel.onEvent(CalculatorEvent.Percentage) })
                    CalculatorButton(symbol = "⌫", color = MaterialTheme.colorScheme.secondaryContainer, textColor = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.weight(1f), onClick = { viewModel.onEvent(CalculatorEvent.Backspace) })
                    CalculatorButton(symbol = "÷", color = OperatorButtonColor, textColor = Color.White, modifier = Modifier.weight(1f), onClick = { viewModel.onEvent(CalculatorEvent.Operator("÷")) })
                }
                Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                    CalculatorButton(symbol = "7", modifier = Modifier.weight(1f), onClick = { viewModel.onEvent(CalculatorEvent.Number("7")) })
                    CalculatorButton(symbol = "8", modifier = Modifier.weight(1f), onClick = { viewModel.onEvent(CalculatorEvent.Number("8")) })
                    CalculatorButton(symbol = "9", modifier = Modifier.weight(1f), onClick = { viewModel.onEvent(CalculatorEvent.Number("9")) })
                    CalculatorButton(symbol = "×", color = OperatorButtonColor, textColor = Color.White, modifier = Modifier.weight(1f), onClick = { viewModel.onEvent(CalculatorEvent.Operator("×")) })
                }
                Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                    CalculatorButton(symbol = "4", modifier = Modifier.weight(1f), onClick = { viewModel.onEvent(CalculatorEvent.Number("4")) })
                    CalculatorButton(symbol = "5", modifier = Modifier.weight(1f), onClick = { viewModel.onEvent(CalculatorEvent.Number("5")) })
                    CalculatorButton(symbol = "6", modifier = Modifier.weight(1f), onClick = { viewModel.onEvent(CalculatorEvent.Number("6")) })
                    CalculatorButton(symbol = "-", color = OperatorButtonColor, textColor = Color.White, modifier = Modifier.weight(1f), onClick = { viewModel.onEvent(CalculatorEvent.Operator("-")) })
                }
                Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                    CalculatorButton(symbol = "1", modifier = Modifier.weight(1f), onClick = { viewModel.onEvent(CalculatorEvent.Number("1")) })
                    CalculatorButton(symbol = "2", modifier = Modifier.weight(1f), onClick = { viewModel.onEvent(CalculatorEvent.Number("2")) })
                    CalculatorButton(symbol = "3", modifier = Modifier.weight(1f), onClick = { viewModel.onEvent(CalculatorEvent.Number("3")) })
                    CalculatorButton(symbol = "+", color = OperatorButtonColor, textColor = Color.White, modifier = Modifier.weight(1f), onClick = { viewModel.onEvent(CalculatorEvent.Operator("+")) })
                }
                Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                    CalculatorButton(symbol = "0", modifier = Modifier.weight(1f), onClick = { viewModel.onEvent(CalculatorEvent.Number("0")) })
                    CalculatorButton(symbol = ".", modifier = Modifier.weight(1f), onClick = { viewModel.onEvent(CalculatorEvent.Decimal) })
                    CalculatorButton(
                        symbol = "=",
                        modifier = Modifier.weight(2f),
                        color = OperatorButtonColor,
                        textColor = Color.White,
                        shape = RoundedCornerShape(50.dp),
                        onClick = { viewModel.onEvent(CalculatorEvent.Calculate) }
                    )
                }
            }
        }
    }
}

@Composable
fun DisplayCard(height: Dp, content: @Composable BoxScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DisplayCardBackgroundColor),
        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterEnd,
            content = content
        )
    }
}

@Composable
fun BlinkingCursor(height: Dp) {
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
fun AutoResizeText(
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


@Composable
fun CalculatorButton(
    symbol: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    textColor: Color = Color.Black,
    shape: Shape = CircleShape,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.70f else 1f, label = "scale")

    Card(
        modifier = modifier
            .scale(scale)
            .aspectRatio(if (shape == CircleShape) 1f else 2f)
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
            Text(text = symbol, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = textColor)
        }
    }
}
