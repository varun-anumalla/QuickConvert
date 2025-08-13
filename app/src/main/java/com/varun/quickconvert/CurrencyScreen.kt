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
import androidx.navigation.NavController

// Consistent color scheme with other screens
private val CurrencyScreenBackgroundColor = Color(0xFFF0F0F3)
private val CurrencyOperatorButtonColor = Color(0xFFFF9F0A)
private val CurrencyDisplayCardBackgroundColor = Color(0xFFE8E8E8)

// Placeholder Data - This would be replaced by your ViewModel
data class Currency(val code: String, val name: String)
val sampleCurrencies = listOf(
    Currency("USD", "United States Dollar"),
    Currency("EUR", "Euro"),
    Currency("JPY", "Japanese Yen"),
    Currency("GBP", "British Pound"),
    Currency("INR", "Indian Rupee")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyScreen(
    navController: NavController,
    // viewModel: CurrencyViewModel = viewModel() // This would be your actual ViewModel
) {
    // val uiState by viewModel.uiState.collectAsState() // Placeholder for state
    var fromValue by remember { mutableStateOf("") }
    var toValue by remember { mutableStateOf("") }
    var fromCurrency by remember { mutableStateOf(sampleCurrencies[0]) }
    var toCurrency by remember { mutableStateOf(sampleCurrencies[4]) }
    var isFromFieldActive by remember { mutableStateOf(true) }
    var showFromMenu by remember { mutableStateOf(false) }
    var showToMenu by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = CurrencyScreenBackgroundColor,
        topBar = {
            TopAppBar(
                title = { Text("Currency Conversion", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CurrencyScreenBackgroundColor)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            //--- DISPLAY SECTION ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                Box {
                    CurrencyConversionDisplay(
                        currency = fromCurrency,
                        value = fromValue,
                        isActive = isFromFieldActive,
                        onFieldClick = { isFromFieldActive = true },
                        onUnitClick = { showFromMenu = true }
                    )
                    CurrencySelectionMenu(
                        expanded = showFromMenu,
                        onDismiss = { showFromMenu = false },
                        onCurrencySelected = { currency ->
                            fromCurrency = currency
                            showFromMenu = false
                            // Trigger recalculation
                        },
                        currencies = sampleCurrencies
                    )
                }
                Spacer(Modifier.height(16.dp))
                Box {
                    CurrencyConversionDisplay(
                        currency = toCurrency,
                        value = toValue,
                        isActive = !isFromFieldActive,
                        onFieldClick = { isFromFieldActive = false },
                        onUnitClick = { showToMenu = true }
                    )
                    CurrencySelectionMenu(
                        expanded = showToMenu,
                        onDismiss = { showToMenu = false },
                        onCurrencySelected = { currency ->
                            toCurrency = currency
                            showToMenu = false
                            // Trigger recalculation
                        },
                        currencies = sampleCurrencies
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            //--- KEYPAD SECTION ---
            val buttonSpacing = 12.dp
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                // Number grid
                Column(
                    modifier = Modifier.weight(3f),
                    verticalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                        CurrencyKeypadButton(symbol = "7", modifier = Modifier.weight(1f)) { /* Append Number */ }
                        CurrencyKeypadButton(symbol = "8", modifier = Modifier.weight(1f)) { /* Append Number */ }
                        CurrencyKeypadButton(symbol = "9", modifier = Modifier.weight(1f)) { /* Append Number */ }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                        CurrencyKeypadButton(symbol = "4", modifier = Modifier.weight(1f)) { /* Append Number */ }
                        CurrencyKeypadButton(symbol = "5", modifier = Modifier.weight(1f)) { /* Append Number */ }
                        CurrencyKeypadButton(symbol = "6", modifier = Modifier.weight(1f)) { /* Append Number */ }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                        CurrencyKeypadButton(symbol = "1", modifier = Modifier.weight(1f)) { /* Append Number */ }
                        CurrencyKeypadButton(symbol = "2", modifier = Modifier.weight(1f)) { /* Append Number */ }
                        CurrencyKeypadButton(symbol = "3", modifier = Modifier.weight(1f)) { /* Append Number */ }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                        CurrencyKeypadButton(symbol = "0", modifier = Modifier.weight(2f), shape = RoundedCornerShape(50.dp)) { /* Append Number */ }
                        CurrencyKeypadButton(symbol = ".", modifier = Modifier.weight(1f)) { /* Append Decimal */ }
                    }
                }
                // Action buttons
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    CurrencyKeypadButton(symbol = "AC", modifier = Modifier.weight(1f), color = CurrencyOperatorButtonColor,
                        textColor = Color.White, shape = RoundedCornerShape(50.dp)) { /* Clear */ }
                    CurrencyKeypadIconButton(icon = Icons.AutoMirrored.Filled.Backspace, modifier = Modifier.weight(1f),
                        color = CurrencyOperatorButtonColor, tint = Color.White, shape = RoundedCornerShape(50.dp)) { /* Backspace */ }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CurrencyConversionDisplay(
    currency: Currency,
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
        colors = CardDefaults.cardColors(containerColor = CurrencyDisplayCardBackgroundColor),
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${currency.code} - ${currency.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Currency")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                // You can reuse the AutoResizeText from your other screens
                Text(
                    text = if (value.isEmpty()) "0" else value,
                    style = TextStyle(
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    )
                )
                if (isActive) {
                    Spacer(Modifier.width(4.dp))
                    // You can reuse the BlinkingCursor from your other screens
                    Box(modifier = Modifier.width(2.dp).height(40.dp).background(Color.DarkGray))
                }
            }
        }
    }
}

@Composable
private fun CurrencySelectionMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onCurrencySelected: (Currency) -> Unit,
    currencies: List<Currency>
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = Modifier.background(CurrencyDisplayCardBackgroundColor, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Select Currency", style = MaterialTheme.typography.titleSmall)
            IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }
        HorizontalDivider()
        currencies.forEach { currency ->
            DropdownMenuItem(
                text = { Text("${currency.code} - ${currency.name}") },
                onClick = { onCurrencySelected(currency) }
            )
        }
    }
}

@Composable
private fun CurrencyKeypadButton(
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
private fun CurrencyKeypadIconButton(
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