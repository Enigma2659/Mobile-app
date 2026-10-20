package com.example.shopsmart.ui

import androidx.compose.foundation.BorderStroke
<<<<<<< HEAD
import androidx.compose.foundation.Image
=======
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
<<<<<<< HEAD
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
=======
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopsmart.model.CartItem
<<<<<<< HEAD
=======
import com.example.shopsmart.model.DELIVERY_LOCATIONS
import com.example.shopsmart.model.DeliveryLocation
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartAndCheckoutScreen(
    cartItems: List<CartItem>,
    onUpdateQuantity: (CartItem, Int) -> Unit,
    onRemoveItem: (CartItem) -> Unit,
<<<<<<< HEAD
    onProceedToCustomerInfo: () -> Unit,
=======
    onProceedToCustomerInfo: (selectedLocation: DeliveryLocation) -> Unit,
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
    onContinueShopping: () -> Unit,
    onBack: () -> Unit
) {
    var isAgreed by remember { mutableStateOf(false) }
<<<<<<< HEAD
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val itemsSubtotal = cartItems.sumOf { it.totalPrice }
=======

    // Dropdown combo box state for delivery location
    var locationExpanded by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf(DELIVERY_LOCATIONS.first()) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val itemsSubtotal = cartItems.sumOf { it.totalPrice }
    val deliveryFee = selectedLocation.deliveryFee
    val grandTotal = itemsSubtotal + deliveryFee
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
<<<<<<< HEAD
                            text = "Step 1: Cart & Review",
=======
                            text = "My Cart & Delivery",
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
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
        }
    ) { padding ->
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = "🛒", fontSize = 64.sp)
                    Text(
                        text = "Your Cart is Currently Empty",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Browse our Catalog, Hot Promotions, or use our Smart AI Budget Helper to add items!",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Button(
                        onClick = onContinueShopping,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("Explore Catalog", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
<<<<<<< HEAD
                Text(
                    text = "Review Cart Items",
=======
                // SECTION 1: Cart Items Summary with + and - quantity buttons
                Text(
                    text = "1. Cart Summary",
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
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
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        cartItems.forEachIndexed { index, cartItem ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
<<<<<<< HEAD
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(id = getDrawableRes(cartItem.itemId, cartItem.selectedProcessor, cartItem.itemTitle)),
                                            contentDescription = cartItem.itemTitle,
                                            modifier = Modifier
                                                .size(56.dp)
                                                .clip(RoundedCornerShape(8.dp)),
                                            contentScale = ContentScale.Crop
                                        )

                                        Column(
                                            modifier = Modifier.weight(1f),
                                            verticalArrangement = Arrangement.spacedBy(2.dp)
                                        ) {
=======
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                                            Text(
                                                text = cartItem.itemTitle,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = "${cartItem.selectedProcessor} | ${cartItem.selectedWeight}",
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                            )
                                            Text(
                                                text = "MK${String.format(Locale.getDefault(), "%.2f", cartItem.unitPrice)} each",
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }

<<<<<<< HEAD
                                        Column(
                                            horizontalAlignment = Alignment.End,
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            IconButton(
                                                onClick = { onRemoveItem(cartItem) },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "Delete",
                                                    tint = MaterialTheme.colorScheme.error
                                                )
                                            }

                                            Text(
                                                text = "MK${String.format(Locale.getDefault(), "%.2f", cartItem.totalPrice)}",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
=======
                                        Text(
                                            text = "MK${String.format(Locale.getDefault(), "%.2f", cartItem.totalPrice)}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = MaterialTheme.colorScheme.primary
                                        )
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                                    }

                                    HorizontalDivider()

<<<<<<< HEAD
=======
                                    // Bottom row: Quantity controls + Prominent Recycle Bin at the very bottom
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
<<<<<<< HEAD
                                        Text(text = "Quantity:", fontSize = 13.sp, fontWeight = FontWeight.Medium)

=======
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                                        ) {
<<<<<<< HEAD
=======
                                            Text(text = "Qty:", fontSize = 13.sp, fontWeight = FontWeight.Medium)
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                                            IconButton(
                                                onClick = {
                                                    if (cartItem.quantity > 1) {
                                                        onUpdateQuantity(cartItem, cartItem.quantity - 1)
                                                    }
                                                },
<<<<<<< HEAD
                                                modifier = Modifier.size(30.dp)
=======
                                                modifier = Modifier.size(32.dp)
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Remove,
                                                    contentDescription = "Decrease"
                                                )
                                            }

                                            Text(
                                                text = cartItem.quantity.toString(),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp,
                                                modifier = Modifier.padding(horizontal = 4.dp)
                                            )

                                            IconButton(
                                                onClick = { onUpdateQuantity(cartItem, cartItem.quantity + 1) },
<<<<<<< HEAD
                                                modifier = Modifier.size(30.dp)
=======
                                                modifier = Modifier.size(32.dp)
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Add,
                                                    contentDescription = "Increase"
                                                )
                                            }
                                        }
<<<<<<< HEAD
=======

                                        // Prominent Recycle Bin / Delete button at the very bottom of the item card
                                        OutlinedButton(
                                            onClick = { onRemoveItem(cartItem) },
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                contentColor = MaterialTheme.colorScheme.error,
                                                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
                                            ),
                                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                            modifier = Modifier.height(34.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Recycle Bin",
                                                modifier = Modifier.size(16.dp),
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                            text = "Delete",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                                    }
                                }
                            }

                            if (index < cartItems.lastIndex) {
<<<<<<< HEAD
                                Spacer(modifier = Modifier.height(8.dp))
=======
                                Spacer(modifier = Modifier.height(10.dp))
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
<<<<<<< HEAD
                            Text("Subtotal:", fontWeight = FontWeight.SemiBold)
                            Text(
                                "MK${String.format(Locale.getDefault(), "%.2f", itemsSubtotal)}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.primary
=======
                            Text("Items Subtotal:", fontWeight = FontWeight.SemiBold)
                            Text(
                                "MK${String.format(Locale.getDefault(), "%.2f", itemsSubtotal)}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                            )
                        }
                    }
                }

                // Checkbox: Agree with cart summary
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isAgreed) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isAgreed = !isAgreed }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isAgreed,
                            onCheckedChange = { isAgreed = it }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "I review and AGREE with my cart summary and quantities above.",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

<<<<<<< HEAD
=======
                // SECTION 2: Select Delivery Location (word "Combo Box" removed as requested!)
                Text(
                    text = "2. Select Delivery Location",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                ExposedDropdownMenuBox(
                    expanded = locationExpanded,
                    onExpandedChange = { locationExpanded = !locationExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Closed text field value showing ONLY location name (no price/distance indicated next to it as requested!)
                    OutlinedTextField(
                        value = selectedLocation.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Choose Delivery Location") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = locationExpanded)
                        },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = locationExpanded,
                        onDismissRequest = { locationExpanded = false }
                    ) {
                        DELIVERY_LOCATIONS.forEach { location ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = location.name,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = "${location.distanceKm.toInt()} km (MK${String.format(Locale.getDefault(), "%.0f", location.deliveryFee)})",
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontSize = 13.sp
                                        )
                                    }
                                },
                                onClick = {
                                    selectedLocation = location
                                    locationExpanded = false
                                }
                            )
                        }
                    }
                }

                // SECTION 3: Updated Total according to Delivery Fee (K1000 per km)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
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
                            Text("ESTIMATED TOTAL:", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                            Text(
                                "MK${String.format(Locale.getDefault(), "%.2f", grandTotal)}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

<<<<<<< HEAD
=======
                // PROCEED TO CUSTOMER INFO BUTTON
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                Button(
                    onClick = {
                        if (!isAgreed) {
                            errorMessage = "Please review and AGREE with your cart summary before proceeding."
                            return@Button
                        }
                        errorMessage = null
<<<<<<< HEAD
                        onProceedToCustomerInfo()
=======
                        onProceedToCustomerInfo(selectedLocation)
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
<<<<<<< HEAD
                        text = "Go to Customer Details →",
=======
                        text = "Proceed to Customer Details →",
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
