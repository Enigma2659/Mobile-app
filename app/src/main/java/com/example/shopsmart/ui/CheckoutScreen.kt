package com.example.shopsmart.ui

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopsmart.R
import com.example.shopsmart.model.CartItem
import com.example.shopsmart.model.DeliveryLocation
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Scanner
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    firstName: String,
    lastName: String,
    deliveryLocation: DeliveryLocation,
    cartItems: List<CartItem>,
    onBack: () -> Unit,
    onSubmitOrder: (phoneNumber: String, finalLocName: String) -> Unit
) {
    var airtelNumber by remember { mutableStateOf("") }
    var customCheckoutAddress by remember { mutableStateOf(deliveryLocation.name) }
    var customDistanceKm by remember { mutableStateOf(deliveryLocation.distanceKm.coerceAtLeast(2.0)) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isProcessing by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val itemsTotal = cartItems.sumOf { it.totalPrice }
    val deliveryFee = customDistanceKm * 250.0
    val grandTotal = itemsTotal + deliveryFee

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout & Airtel CTech Payment", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
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
                        Text("MK${String.format(Locale.getDefault(), "%.2f", itemsTotal)}")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Delivery Fee (${String.format(Locale.getDefault(), "%.1f", customDistanceKm)} km):")
                        Text("MK${String.format(Locale.getDefault(), "%.2f", deliveryFee)}")
                    }
                    HorizontalDivider()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Checkout:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(
                            "MK${String.format(Locale.getDefault(), "%.2f", grandTotal)}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (isProcessing) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        Text(
                            text = "Processing CTech payment & saving order...",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Button(
                        onClick = {
                            if (airtelNumber.isBlank() || airtelNumber.length != 10 ||
                                (!airtelNumber.startsWith("099") && !airtelNumber.startsWith("098"))
                            ) {
                                errorMessage = "Airtel number must be 10 digits and start with 099 or 098."
                                return@Button
                            }
                            errorMessage = null
                            isProcessing = true

                            coroutineScope.launch {
                                val paymentResult = processCtechAirtelPayment(grandTotal, airtelNumber)
                                if (paymentResult.isFailure) {
                                    isProcessing = false
                                    errorMessage = "Payment Declined: ${paymentResult.exceptionOrNull()?.localizedMessage ?: "Wrong PIN or Insufficient Funds"}"
                                    return@launch
                                }

                                val timeStamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
                                val orderId = "SS-$timeStamp"
                                val finalLoc = customCheckoutAddress.ifBlank { deliveryLocation.name }

                                saveOrderToFirestore(
                                    orderId = orderId,
                                    firstName = firstName,
                                    lastName = lastName,
                                    phoneNumber = airtelNumber,
                                    deliveryLocation = finalLoc,
                                    items = cartItems,
                                    totalAmount = grandTotal,
                                    onSuccess = {
                                        Log.d("ShopSimple", "Order saved successfully to Firestore.")
                                        isProcessing = false
                                        onSubmitOrder(airtelNumber, finalLoc)
                                    },
                                    onError = { e ->
                                        Log.e("ShopSimple", "Firestore save-failed", e)
                                        isProcessing = false
                                        errorMessage = "Firebase Error: ${e.toString()}"
                                    }
                                )
                            }
                        },
                        enabled = !isProcessing,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(text = "Pay via Airtel CTech & Save Order", fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
                .padding(16.dp)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- CUSTOM LIVE LOCATION CARD WITH TRACKING ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "➡️[ CUSTOM DELIVERY SPOT]",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Type your live street address or place (e.g. Sadzi, Hospital)",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                    OutlinedTextField(
                        value = customCheckoutAddress,
                        onValueChange = { customCheckoutAddress = it },
                        label = { Text("My Live Location / Street Address *") },
                        placeholder = { Text("e.g. Sadzi, Hospital...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            val estDist = if (customCheckoutAddress.contains("sadzi", true)) 3.5
                            else if (customCheckoutAddress.contains("hospital", true)) 5.0
                            else 4.0
                            customDistanceKm = estDist
                        },
                        modifier = Modifier.fillMaxWidth().height(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("📍 Track Location & Calculate Distance", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Text(
                        text = "✓ Distance from Hub: ${String.format(Locale.getDefault(), "%.1f", customDistanceKm)} km | Fee: MK${String.format(Locale.getDefault(), "%.2f", deliveryFee)}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Customer: $firstName $lastName", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = "Delivery Location: ${deliveryLocation.name} (${deliveryLocation.distanceKm} km)", fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.airtel_logo),
                    contentDescription = "Airtel Logo",
                    modifier = Modifier.size(36.dp)
                )
                Text(text = "Airtel Payment ", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            OutlinedTextField(
                value = airtelNumber,
                onValueChange = { airtelNumber = it.filter { char -> char.isDigit() }.take(10) },
                label = { Text("Airtel Number *") },
                placeholder = { Text("099XXXXXXX / 098XXXXXXX", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)) },
                supportingText = { Text("10 digit Airtel number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(250.dp))
        }
    }
}

// CTech Pay Airtel Mobile Payment Gateway Helper
suspend fun processCtechAirtelPayment(
    amount: Double,
    phone: String
): Result<Unit> = withContext(Dispatchers.IO) {
    try {
        val url = URL("https://api-sandbox.ctechpay.com/student/mobile/")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.connectTimeout = 8000
        connection.readTimeout = 8000
        connection.doOutput = true
        connection.doInput = true

        val boundary = "Boundary-" + UUID.randomUUID().toString()
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")

        val body = StringBuilder()
        fun appendField(name: String, value: String) {
            body.append("--$boundary\r\n")
            body.append("Content-Disposition: form-data; name=\"$name\"\r\n\r\n")
            body.append("$value\r\n")
        }

        appendField("airtel", "1")
        appendField("token", "ed905c7f701083538b6ba366f2e6ed70")
        appendField("registration", "BEDCOM2622")
        appendField("amount", amount.toInt().toString())
        appendField("phone", phone)
        body.append("--$boundary--\r\n")

        val outputStream = OutputStreamWriter(connection.outputStream, "UTF-8")
        outputStream.write(body.toString())
        outputStream.flush()
        outputStream.close()

        val responseCode = connection.responseCode
        val responseStream = if (responseCode in 200..299) connection.inputStream else connection.errorStream
        val scanner = Scanner(responseStream).useDelimiter("\\A")
        val fullResponse = if (scanner.hasNext()) scanner.next() else ""

        Log.d("ShopSimple", "CTech Pay Response ($responseCode): $fullResponse")

        if (responseCode in 200..299) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("CTech Payment Gateway Error ($responseCode): $fullResponse"))
        }
    } catch (e: Exception) {
        Log.e("ShopSimple", "CTech Payment Exception", e)
        Result.failure(e)
    }
}

// Firebase Firestore Order Persistence Helper
fun saveOrderToFirestore(
    orderId: String,
    firstName: String,
    lastName: String,
    phoneNumber: String,
    deliveryLocation: String,
    items: List<CartItem>,
    totalAmount: Double,
    onSuccess: () -> Unit,
    onError: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val orderMap = hashMapOf(
        "orderId" to orderId,
        "firstName" to firstName,
        "lastName" to lastName,
        "phoneNumber" to phoneNumber,
        "deliveryLocation" to deliveryLocation,
        "items" to items.map {
            mapOf(
                "title" to it.itemTitle,
                "processor" to it.selectedProcessor,
                "weight" to it.selectedWeight,
                "quantity" to it.quantity,
                "unitPrice" to it.unitPrice,
                "totalPrice" to it.totalPrice
            )
        },
        "totalAmount" to totalAmount,
        "status" to "Not Delivered",
        "createdAt" to Timestamp.now()
    )

    db.collection("orders")
        .document(orderId)
        .set(orderMap)
        .addOnSuccessListener {
            Log.d("ShopSimple", "Successfully saved order $orderId to Firebase Firestore collection 'orders'.")
            onSuccess()
        }
        .addOnFailureListener { e ->
            Log.e("ShopSimple", "Failed to save order $orderId to Firebase Firestore", e)
            onError(e)
        }
}
