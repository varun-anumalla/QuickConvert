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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.varun.quickconvert.ui.theme.QuickConvertTheme

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
            TemperatureScreen() // Placeholder screen
        }
        // Defines the "speed" screen route
        composable("speed") {
            SpeedScreen() // Placeholder screen
        }
        // Defines the "currency" screen route
        composable("currency") {
            CurrencyScreen() // Placeholder screen
        }
        /* this Defines the "calculator" screen route */
        composable("calculator") { CalculatorScreen(navController = navController) }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quick Convert") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CategoryCard(
                    title = "Currency",
                    icon = Icons.Default.CurrencyExchange,
                    // This now navigates to the "currency" screen
                    onClick = { navController.navigate("currency") },
                    modifier = Modifier.weight(1f)
                )
                CategoryCard(
                    title = "Temperature",
                    icon = Icons.Default.Thermostat,
                    // This now navigates to the "temperature" screen
                    onClick = { navController.navigate("temperature") },
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CategoryCard(
                    title = "Speed",
                    icon = Icons.Default.Speed,
                    onClick = { navController.navigate("speed") },
                    modifier = Modifier.weight(1f)
                )
                CategoryCard(
                    title = "Calculator",
                    icon = Icons.Default.Calculate,
                    onClick = { navController.navigate("calculator") },
                    modifier = Modifier.weight(1f)
                )
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
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
// --- ALL PLACEHOLDER SCREENS ---
@Composable
fun TemperatureScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Temperature Screen", fontSize = 24.sp, textAlign = TextAlign.Center)
    }
}
@Composable
fun SpeedScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Speed Screen", fontSize = 24.sp, textAlign = TextAlign.Center)
    }
}
@Composable
fun CurrencyScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Currency Screen", fontSize = 24.sp, textAlign = TextAlign.Center)
    }
}
@Composable
fun CalculatorScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Calculator Screen", fontSize = 24.sp, textAlign = TextAlign.Center)
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    QuickConvertTheme {
        HomeScreen(navController = rememberNavController())
    }
}