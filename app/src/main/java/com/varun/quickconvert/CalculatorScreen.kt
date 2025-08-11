package com.varun.quickconvert

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Define our Neumorphic colors
val NeumorphicBackgroundColor = Color(0xFFE0E5EC)
val NeumorphicLightShadow = Color.White.copy(alpha = 0.8f)
val NeumorphicDarkShadow = Color(0xFFA3B1C6).copy(alpha = 0.4f)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(navController: NavController) {
    val equationText = "4,900+15,910"
    val resultText = "20,810"

    Scaffold(
        // Set the background of the whole screen to our base color
        containerColor = NeumorphicBackgroundColor,
        topBar = {
            TopAppBar(
                title = { Text("Calculator") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                // Make TopAppBar transparent to show the screen background
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.End
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(text = equationText, fontSize = 32.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = resultText, fontSize = 64.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(16.dp))

            val buttonSpacing = 12.dp // Increased spacing for a better look
            Column(verticalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                val operatorColor = Color(0xFFFF9500) // A nice orange for operators

                Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                    CalculatorButton(symbol = "AC", modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = "%", modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = "⌫", modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = "÷", color = operatorColor, modifier = Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                    CalculatorButton(symbol = "7", modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = "8", modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = "9", modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = "×", color = operatorColor, modifier = Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                    CalculatorButton(symbol = "4", modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = "5", modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = "6", modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = "-", color = operatorColor, modifier = Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                    CalculatorButton(symbol = "1", modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = "2", modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = "3", modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = "+", color = operatorColor, modifier = Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                    CalculatorButton(symbol = "0", modifier = Modifier.weight(1f))
                    CalculatorButton(symbol = ".", modifier = Modifier.weight(1f))
                    CalculatorButton(
                        symbol = "=",
                        modifier = Modifier.weight(2f),
                        color = operatorColor,
                        shape = RoundedCornerShape(50.dp)
                    )
                }
            }
        }
    }
}


// --- This composable will create a 3D NEUMORPHIC BUTTON ---
@Composable
fun CalculatorButton(
    symbol: String,
    modifier: Modifier = Modifier,
    color: Color = NeumorphicBackgroundColor, // Button color matches background
    shape: Shape = CircleShape,
    onClick: () -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            // The Dark Shadow (bottom-right)
            .shadow(
                elevation = 8.dp,
                shape = shape,
                spotColor = NeumorphicDarkShadow
            )
            // The Light Shadow (top-left)
            .shadow(
                elevation = 8.dp,
                shape = shape,
                spotColor = NeumorphicLightShadow
            )
            .clip(shape)
            .background(color)
            .clickable(onClick = onClick)
            .aspectRatio(if (shape == CircleShape) 1f else 2f)
            .padding(8.dp)
    ) {
        Text(
            text = symbol,
            fontSize = 32.sp,
            fontWeight = FontWeight.Medium,
            // Use a dark color for text on light buttons
            color = if (color == NeumorphicBackgroundColor) Color.DarkGray else Color.White
        )
    }
}