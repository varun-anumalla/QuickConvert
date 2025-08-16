package com.varun.quickconvert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.varun.quickconvert.ui.theme.QuickConvertTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.scale
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

private val StaticPrimaryColor = Color(0xFF6650a4)
private val StaticBackgroundColor = Color(0xFFF0F0F3)
private val StaticTextColor = Color.DarkGray

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuickConvertTheme {
                AppNavigation()
            }
        }
    }
}

// -- This composable will manage all screens ---
@Composable
fun AppNavigation() {
    val navController = rememberNavController() // Creates a controller to manage navigation
    NavHost(navController = navController, startDestination = "home") {
        // Defines the "home" screen route
        composable("home") {
            HomeScreen(navController = navController)
        }
        // Defines the "temperature" screen route
        composable("temperature") {
            TemperatureScreen(navController = navController)
        }
        // Defines the "speed" screen route
        composable("speed") {
            SpeedScreen(navController = navController)
        }
        // Defines the "currency" screen route
        composable("currency") {
            CurrencyScreen(navController = navController)
        }
        /* this Defines the "calculator" screen route */
        composable("calculator") { CalculatorScreen(navController = navController) }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    // 1. Get a coroutine scope. This allows us to launch a coroutine from a composable.
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quick Convert") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = StaticPrimaryColor,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = StaticBackgroundColor
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CategoryCard(
                        title = "Currency",
                        icon = Icons.Default.CurrencyExchange,
                        // 2. Updated onClick logic
                        onClick = {
                            scope.launch {
                                // A 150ms delay is just enough to see the animation
                                kotlinx.coroutines.delay(150)
                                navController.navigate("currency")
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    CategoryCard(
                        title = "Temperature",
                        icon = Icons.Default.Thermostat,
                        // 2. Updated onClick logic
                        onClick = {
                            scope.launch {
                                kotlinx.coroutines.delay(150)
                                navController.navigate("temperature")
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CategoryCard(
                        title = "Speed",
                        icon = Icons.Default.Speed,
                        // 2. Updated onClick logic
                        onClick = {
                            scope.launch {
                                kotlinx.coroutines.delay(150)
                                navController.navigate("speed")
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    CategoryCard(
                        title = "Calculator",
                        icon = Icons.Default.Calculate,
                        // 2. Updated onClick logic
                        onClick = {
                            scope.launch {
                                kotlinx.coroutines.delay(150)
                                navController.navigate("calculator")
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
@Composable
fun CategoryCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // --- Animation Logic Start ---
    // This is the same logic used in the CalculatorButton
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    // We'll scale down to 90% when pressed for a subtle but noticeable effect
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.8f else 1f, label = "scale")
    // --- Animation Logic End ---

    Card(
        modifier = modifier
            .aspectRatio(1f)
            // Apply the animated scale value to the card
            .scale(scale)
            // Updated clickable modifier to track press interactions
            .clickable(
                interactionSource = interactionSource,
                indication = null, // Disable the default ripple effect
                onClick = onClick
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(48.dp),
                tint = StaticTextColor.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = StaticTextColor.copy(alpha = 0.8f)
            )
        }
    }
}
// --- ALL PLACEHOLDER SCREENS ---




@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    QuickConvertTheme {
        HomeScreen(navController = rememberNavController())
    }
}