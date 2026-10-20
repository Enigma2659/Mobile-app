package com.example.shopsmart.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopsmart.model.CartItem
import com.example.shopsmart.model.DELIVERY_LOCATIONS
import com.example.shopsmart.model.DeliveryLocation
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    cartItems: List<CartItem>,
    onSubmitOrder: (firstName: String, lastName: String, location: DeliveryLocation, airtelNumber: String) -> Unit,
    onBack: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf(DELIVERY_LOCATIONS.first()) }
    var airtelNumber by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val itemsTotal = cartItems.sumOf { it.totalPrice }
    val deliveryFee = selectedLocation.deliveryFee
    val grandTotal = itemsTotal + deliveryFee

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout & Delivery Details") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back", color = MaterialTheme.colorScheme.onSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Items Subtotal:")
                        Text("K${String.format(Locale.getDefault(), "%.2f", itemsTotal)}")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Delivery Fee (${selectedLocation.distanceKm}km):")
                        Text("K${String.format(Locale.getDefault(), "%.2f", deliveryFee)}")
                    }
                    HorizontalDivider()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Checkout:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(
                            "K${String.format(Locale.getDefault(), "%.2f", grandTotal)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp
                        )
                    }

                    Button(
                        onClick = {
                            // Validation
                            if (firstName.isBlank() || lastName.isBlank()) {
                                errorMessage = "Please enter both First Name and Last Name."
                                return@Button
                            }
                            if (airtelNumber.isBlank() || airtelNumber.length != 10 ||
                                (!airtelNumber.startsWith("099") && !airtelNumber.startsWith("098"))
                            ) {
                                errorMessage = "Airtel number must be 10 digits and start with 099 or 098."
                                return@Button
                            }
                            errorMessage = null
                            onSubmitOrder(firstName, lastName, selectedLocation, airtelNumber)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(text = "Place Order & Pay via Airtel", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Customer Information (Compulsory)", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name *") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name *") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Select Delivery Location", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                DELIVERY_LOCATIONS.forEach { location ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (selectedLocation == location),
                                onClick = { selectedLocation = location }
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedLocation == location)
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            else
                                MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = (selectedLocation == location),
                                    onClick = { selectedLocation = location }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(text = location.name, fontWeight = FontWeight.Medium)
                                    Text(
                                        text = "Distance: ${location.distanceKm} km",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                }
                            }
                            Text(
                                text = "K${String.format(Locale.getDefault(), "%.2f", location.deliveryFee)}",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Payment Information (Airtel)", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            OutlinedTextField(
                value = airtelNumber,
                onValueChange = { airtelNumber = it.filter { char -> char.isDigit() }.take(10) },
                label = { Text("Airtel Number *") },
                placeholder = { Text("099XXXXXX / 098XXXXXX", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)) },
                supportingText = { Text("10 digit number starting with 099 or 098") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
