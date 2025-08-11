package com.varun.quickconvert

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Define our final colors
val ScreenBackgroundColor = Color(0xFFF0F0F3) // A soft, light gray background
val OperatorButtonColor = Color(0xFFFF9F0A)   // A vibrant orange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(navController: NavController) {
    val equationText = "22,000 + 510"
    val resultText = "22,510"

    Scaffold(
        containerColor = ScreenBackgroundColor,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // --- DISPLAY AREA ---
            // This Box will take up all the available flexible space at the top
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.BottomEnd // Align text to the bottom-right
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = equationText, fontSize = 32.sp, color = Color.Gray)
                    Text(text = resultText, fontSize = 72.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }

            // --- BUTTON GRID ---
            // This Column will sit at the bottom and size itself based on its content
            val buttonSpacing = 12.dp
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                // NOTE: The modifier = Modifier.weight(1f) has been REMOVED from the Rows
                Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                    CalculatorButton(symbol = "AC", color = MaterialTheme.colorScheme.secondaryContainer, textColor = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = "%", color = MaterialTheme.colorScheme.secondaryContainer, textColor = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = "⌫", color = MaterialTheme.colorScheme.secondaryContainer, textColor = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = "÷", color = OperatorButtonColor, textColor = Color.White, modifier = Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                    CalculatorButton(symbol = "7", modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = "8", modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = "9", modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = "×", color = OperatorButtonColor, textColor = Color.White, modifier = Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                    CalculatorButton(symbol = "4", modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = "5", modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = "6", modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = "-", color = OperatorButtonColor, textColor = Color.White, modifier = Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                    CalculatorButton(symbol = "1", modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = "2", modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = "3", modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = "+", color = OperatorButtonColor, textColor = Color.White, modifier = Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                    CalculatorButton(symbol = "0", modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = ".", modifier = Modifier.weight(1f))
                    CalculatorButton(
                        symbol = "=",
                        modifier = Modifier.weight(2f),
                        color = OperatorButtonColor,
                        textColor = Color.White,
                        shape = RoundedCornerShape(50.dp)
                    )
                }
            }
        }
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
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.95f else 1f, label = "scale")

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