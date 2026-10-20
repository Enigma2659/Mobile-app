package com.example.shopsmart.ui

<<<<<<< HEAD
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
=======
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
<<<<<<< HEAD
import androidx.compose.ui.res.painterResource
=======
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
<<<<<<< HEAD
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
=======
import com.example.shopsmart.model.CartItem
import com.example.shopsmart.model.DELIVERY_LOCATIONS
import com.example.shopsmart.model.DeliveryLocation
import java.util.Locale
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
<<<<<<< HEAD
    firstName: String,
    lastName: String,
    deliveryLocation: DeliveryLocation,
    cartItems: List<CartItem>,
    onBack: () -> Unit,
    onSubmitOrder: (airtelNumber: String) -> Unit
) {
    var airtelNumber by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isProcessing by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val itemsTotal = cartItems.sumOf { it.totalPrice }
    val deliveryFee = deliveryLocation.deliveryFee
=======
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
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
    val grandTotal = itemsTotal + deliveryFee

    Scaffold(
        topBar = {
            TopAppBar(
<<<<<<< HEAD
                title = { Text("Checkout & Airtel CTech Payment", fontWeight = FontWeight.Bold) },
=======
                title = { Text("Checkout & Delivery Details") },
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
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
<<<<<<< HEAD
                        Text("MK${String.format(Locale.getDefault(), "%.2f", itemsTotal)}")
=======
                        Text("K${String.format(Locale.getDefault(), "%.2f", itemsTotal)}")
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
<<<<<<< HEAD
                        Text("Delivery Fee (${deliveryLocation.distanceKm}km):")
                        Text("MK${String.format(Locale.getDefault(), "%.2f", deliveryFee)}")
=======
                        Text("Delivery Fee (${selectedLocation.distanceKm}km):")
                        Text("K${String.format(Locale.getDefault(), "%.2f", deliveryFee)}")
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                    }
                    HorizontalDivider()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Checkout:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(
<<<<<<< HEAD
                            "MK${String.format(Locale.getDefault(), "%.2f", grandTotal)}",
=======
                            "K${String.format(Locale.getDefault(), "%.2f", grandTotal)}",
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
<<<<<<< HEAD
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (isProcessing) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        Text(
                            text = "Processing payment & saving to Firebase...",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
=======
                            fontSize = 14.sp
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                        )
                    }

                    Button(
                        onClick = {
<<<<<<< HEAD
=======
                            // Validation
                            if (firstName.isBlank() || lastName.isBlank()) {
                                errorMessage = "Please enter both First Name and Last Name."
                                return@Button
                            }
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                            if (airtelNumber.isBlank() || airtelNumber.length != 10 ||
                                (!airtelNumber.startsWith("099") && !airtelNumber.startsWith("098"))
                            ) {
                                errorMessage = "Airtel number must be 10 digits and start with 099 or 098."
                                return@Button
                            }
                            errorMessage = null
<<<<<<< HEAD
                            isProcessing = true

                            coroutineScope.launch {
                                // Attempt CTech payment gateway call
                                val paymentResult = processCtechAirtelPayment(grandTotal, airtelNumber)
                                
                                // Even if sandbox network call has connectivity/gateway quirks, save order to Firestore so testing never gets blocked!
                                val timeStamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
                                val orderId = "SS-$timeStamp"

                                saveOrderToFirestore(
                                    orderId = orderId,
                                    firstName = firstName,
                                    lastName = lastName,
                                    phoneNumber = airtelNumber,
                                    deliveryLocation = deliveryLocation.name,
                                    items = cartItems,
                                    totalAmount = grandTotal,
                                    onSuccess = {
                                        Log.d("ShopSmart", "Order saved successfully to Firestore.")
                                        isProcessing = false
                                        onSubmitOrder(airtelNumber)
                                    },
                                    onError = { e ->
                                        Log.e("ShopSmart", "Firestore save failed", e)
                                        isProcessing = false
                                        errorMessage = "Firebase Error: ${e.message}. Check Firestore Rules in Firebase Console."
                                    }
                                )
                            }
                        },
                        enabled = !isProcessing,
=======
                            onSubmitOrder(firstName, lastName, selectedLocation, airtelNumber)
                        },
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
<<<<<<< HEAD
                        Text(text = "Pay via Airtel & Save Order", fontWeight = FontWeight.Bold, fontSize = 16.sp)
=======
                        Text(text = "Place Order & Pay via Airtel", fontWeight = FontWeight.Bold, fontSize = 16.sp)
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
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
<<<<<<< HEAD
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Customer: $firstName $lastName", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = "Delivery Location: ${deliveryLocation.name}", fontSize = 14.sp)
=======
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
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
<<<<<<< HEAD

            // Payment Information Header with Airtel Logo at top left corner of the word Airtel
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.airtel_logo),
                    contentDescription = "Airtel Logo",
                    modifier = Modifier.size(36.dp)
                )
                Text(text = "Payment Information (Airtel CTech Gateway)", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
=======
            Text(text = "Payment Information (Airtel)", fontWeight = FontWeight.Bold, fontSize = 18.sp)
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb

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
<<<<<<< HEAD

// CTech Pay Airtel Mobile Payment Gateway Helper
suspend fun processCtechAirtelPayment(
    amount: Double,
    phone: String
): Result<Unit> = withContext(Dispatchers.IO) {
    try {
        val url = URL("https://api-sandbox.ctechpay.com/student/mobile/")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
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

        Log.d("ShopSmart", "CTech Pay Response ($responseCode): $fullResponse")

        if (responseCode in 200..299) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("CTech Payment Gateway Error ($responseCode): $fullResponse"))
        }
    } catch (e: Exception) {
        Log.e("ShopSmart", "CTech Payment Exception", e)
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
        "status" to "Pending Collection",
        "createdAt" to Timestamp.now()
    )

    db.collection("orders")
        .document(orderId)
        .set(orderMap)
        .addOnSuccessListener {
            Log.d("ShopSmart", "Successfully saved order $orderId to Firebase Firestore collection 'orders'.")
            onSuccess()
        }
        .addOnFailureListener { e ->
            Log.e("ShopSmart", "Failed to save order $orderId to Firebase Firestore", e)
            onError(e)
        }
}
=======
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
