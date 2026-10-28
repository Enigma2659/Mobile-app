package com.example.shopsmart.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
<<<<<<< HEAD
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
=======
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ReceiptLong
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
<<<<<<< HEAD
=======
import androidx.compose.ui.graphics.Color
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
<<<<<<< HEAD
import com.example.shopsmart.model.CartItem
import com.example.shopsmart.model.DeliveryLocation
=======
import com.example.shopsmart.model.CompletedOrder
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSummaryScreen(
<<<<<<< HEAD
    orderId: String,
    firstName: String,
    lastName: String,
    phoneNumber: String,
    deliveryLocation: DeliveryLocation,
    cartItems: List<CartItem>,
    onFinish: () -> Unit
) {
    val itemsTotal = cartItems.sumOf { it.totalPrice }
    val deliveryFee = deliveryLocation.deliveryFee
    val grandTotal = itemsTotal + deliveryFee

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Summary & Confirmation", fontWeight = FontWeight.Bold) },
=======
    order: CompletedOrder,
    onViewOrdersTracker: () -> Unit,
    onFinish: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Summary & Receipt", fontWeight = FontWeight.Bold) },
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                navigationIcon = {
                    IconButton(onClick = onFinish) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Home"
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
<<<<<<< HEAD
                Box(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = onFinish,
=======
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onViewOrdersTracker,
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
                        Text(text = "Back to Home / New Order", fontWeight = FontWeight.Bold, fontSize = 16.sp)
=======
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(text = "View Orders & Received Status →", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }

                    OutlinedButton(
                        onClick = onFinish,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(text = "Back to Catalog / New Order", fontWeight = FontWeight.Bold)
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
            // Success Card with Order ID
=======
            // Success Card with System Order ID
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
<<<<<<< HEAD
                )
=======
                ),
                shape = RoundedCornerShape(16.dp)
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
<<<<<<< HEAD
                    Text(text = "🎉 Order Placed Successfully!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Your Unique Order ID:", fontSize = 14.sp)
                    Text(
                        text = orderId,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Provide this Order ID upon collection/delivery arrival at your physical location when announced.",
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
=======
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )

                    Text(text = "🎉 Order Placed & Paid!", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                    Text(text = "System Order ID:", fontSize = 13.sp, fontWeight = FontWeight.Medium)

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = order.orderId,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    Text(
                        text = "Saved in Firebase database. Keep your Order ID safe!",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                    )
                }
            }

<<<<<<< HEAD
            // Important Collection Notice (2 hours)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(text = "⚠️ Important Collection Notice", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.error)
                    Text(
                        text = "Ordered items must be collected within 2 hours after order time.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                    )
                }
            }

            // Customer & Delivery Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
=======
            // Arrival Announcement Banner Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF3CD) // Soft Warning/Announcement Amber
                ),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Color(0xFFFFC107))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = "Announcement",
                        tint = Color(0xFF856404),
                        modifier = Modifier.size(32.dp)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "📢 Arrival Announcement Protocol",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF856404)
                        )
                        Text(
                            text = "When your items arrive at physical place (${order.deliveryLocation.name}), you (${order.firstName} ${order.lastName}) will be announced!\n\nPlease present your System Order ID (${order.orderId}) to collect your package.",
                            fontSize = 13.sp,
                            color = Color(0xFF856404),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Customer & Delivery Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Customer & Delivery Details", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    HorizontalDivider()
<<<<<<< HEAD
                    Text(text = "Customer Name: $firstName $lastName", fontWeight = FontWeight.Medium)
                    Text(text = "Contact Phone: $phoneNumber", fontWeight = FontWeight.Medium)
                    Text(text = "Delivery Location: ${deliveryLocation.name}", fontWeight = FontWeight.Medium)
                }
            }

            // Ordered Items Summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
=======

                    Text(
                        text = "Customer Name: ${order.firstName} ${order.lastName}",
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Airtel Phone: ${order.phoneNumber}",
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Delivery Location: ${order.deliveryLocation.name}",
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Payment Method: ${order.paymentMethod}",
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "PayChangu Ref: ${order.payChanguTxRef}",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE50000)
                    )
                }
            }

            // Ordered Items Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "Ordered Items", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    HorizontalDivider()

<<<<<<< HEAD
                    cartItems.forEach { cartItem ->
=======
                    order.items.forEach { cartItem ->
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${cartItem.itemTitle} (${cartItem.selectedProcessor}, ${cartItem.selectedWeight})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Qty: ${cartItem.quantity} × MK${String.format(Locale.getDefault(), "%.2f", cartItem.unitPrice)}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                            Text(
                                text = "MK${String.format(Locale.getDefault(), "%.2f", cartItem.totalPrice)}",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    HorizontalDivider()

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
<<<<<<< HEAD
                        Text("Items Subtotal")
                        Text("MK${String.format(Locale.getDefault(), "%.2f", itemsTotal)}")
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Delivery Fee")
                        Text("MK${String.format(Locale.getDefault(), "%.2f", deliveryFee)}")
=======
                        Text("Items Subtotal:")
                        Text("MK${String.format(Locale.getDefault(), "%.2f", order.itemsSubtotal)}")
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Delivery Fee:")
                        Text("MK${String.format(Locale.getDefault(), "%.2f", order.deliveryFee)}")
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                    }

                    HorizontalDivider()

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
<<<<<<< HEAD
                        Text("Total Paid via CTech Airtel", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                        Text(
                            "MK${String.format(Locale.getDefault(), "%.2f", grandTotal)}",
=======
                        Text("Total Paid via PayChangu:", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                        Text(
                            "MK${String.format(Locale.getDefault(), "%.2f", order.grandTotal)}",
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
