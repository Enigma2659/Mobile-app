package com.example.shopsmart.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopsmart.model.DeliveryLocation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerInfoScreen(
    selectedLocation: DeliveryLocation,
    itemsSubtotal: Double,
    onBack: () -> Unit,
    onSubmitOrder: (
        firstName: String,
        lastName: String,
        phoneNumber: String,
        customLocationName: String,
        payChanguTxRef: String
    ) -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") } // Surname
    var phoneNumber by remember { mutableStateOf("") }
    var customLocationDetails by remember { mutableStateOf("") }

    var isProcessingPayChangu by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    val deliveryFee = selectedLocation.deliveryFee
    val grandTotal = itemsSubtotal + deliveryFee

    // Airtel validation helper
    fun isValidAirtelNumber(num: String): Boolean {
        val clean = num.filter { it.isDigit() }
        return (clean.length == 10 && (clean.startsWith("099") || clean.startsWith("098"))) ||
                (clean.length == 12 && (clean.startsWith("26599") || clean.startsWith("26598")))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Customer & Payment Details", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Cart"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Order Summary & Delivery Location Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "1. Order & Location Summary",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Delivery Option:", fontWeight = FontWeight.Medium)
                        Text(
                            text = selectedLocation.name,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f, fill = false),
                            textAlign = TextAlign.End
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Items Subtotal:")
                        Text("MK${String.format(Locale.getDefault(), "%.2f", itemsSubtotal)}")
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Delivery Fee:")
                        Text("MK${String.format(Locale.getDefault(), "%.2f", deliveryFee)}")
                    }

                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("GRAND TOTAL:", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                        Text(
                            "MK${String.format(Locale.getDefault(), "%.2f", grandTotal)}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Customer Details Form (First Name & Surname)
            Text(
                text = "2. Customer Information (Compulsory)",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("First Name *") },
                        placeholder = { Text("e.g. Chisomo") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Surname / Last Name *") },
                        placeholder = { Text("e.g. Banda") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    if (selectedLocation.isCustom) {
                        OutlinedTextField(
                            value = customLocationDetails,
                            onValueChange = { customLocationDetails = it },
                            label = { Text("Specific Physical Location Details *") },
                            placeholder = { Text("e.g. UNIMA Hostel Block 4 Room 12 or Office Block B Floor 2") },
                            supportingText = { Text("Specify exact building/dorm/office for physical delivery") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }

            // PayChangu Payment Gateway Card (Airtel Money Only)
            Text(
                text = "3. Payment Gateway (PayChangu - Airtel Money)",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFE50000), // Airtel Red
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "A",
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        fontSize = 18.sp
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = "PayChangu Gateway",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "Airtel Money Only",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFE50000)
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.padding(2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Secured",
                                    modifier = Modifier.size(14.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text("256-Bit SSL", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    HorizontalDivider()

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { input ->
                            phoneNumber = input.filter { it.isDigit() }.take(10)
                        },
                        label = { Text("Airtel Phone Number *") },
                        placeholder = { Text("099XXXXXXX or 098XXXXXXX") },
                        supportingText = {
                            Text(
                                text = "Must start with 099 or 098 (10 digits). Payments restricted to Airtel Money ONLY.",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.PhoneAndroid,
                                contentDescription = null,
                                tint = Color(0xFFE50000)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFE50000)
                        )
                    )
                }
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            // PAYCHANGU PROCESS BUTTON
            Button(
                onClick = {
                    if (firstName.isBlank() || lastName.isBlank()) {
                        errorMessage = "Please enter both First Name and Surname (Last Name)."
                        return@Button
                    }
                    if (selectedLocation.isCustom && customLocationDetails.isBlank()) {
                        errorMessage = "Please enter specific physical location details for custom delivery."
                        return@Button
                    }
                    if (!isValidAirtelNumber(phoneNumber)) {
                        errorMessage = "Invalid phone number! Only Airtel numbers starting with 099 or 098 (10 digits) are accepted by PayChangu."
                        return@Button
                    }

                    errorMessage = null
                    isProcessingPayChangu = true

                    scope.launch {
                        delay(1800) // Simulate PayChangu network push prompt to Airtel phone
                        val randomRef = "PAYCHANGU-AM-" + Random.nextInt(100000, 999999)
                        isProcessingPayChangu = false

                        val finalLocationName = if (selectedLocation.isCustom) {
                            "${selectedLocation.name}: $customLocationDetails"
                        } else {
                            selectedLocation.name
                        }

                        onSubmitOrder(
                            firstName.trim(),
                            lastName.trim(),
                            phoneNumber.trim(),
                            finalLocationName,
                            randomRef
                        )
                    }
                },
                enabled = !isProcessingPayChangu,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE50000), // Airtel Red
                    contentColor = Color.White
                )
            ) {
                if (isProcessingPayChangu) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.5.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Processing PayChangu Airtel Payment...", fontWeight = FontWeight.Bold)
                } else {
                    Text(
                        text = "Pay MK${String.format(Locale.getDefault(), "%.0f", grandTotal)} via PayChangu (Airtel)",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}
