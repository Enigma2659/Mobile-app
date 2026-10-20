package com.example.shopsmart.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopsmart.model.CartItem
import com.example.shopsmart.model.ShopItem
import java.util.Locale

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    item: ShopItem,
    onAddToCart: (CartItem) -> Unit,
    onBack: () -> Unit
) {
    var selectedProcessor by remember { mutableStateOf(item.processors.firstOrNull() ?: "") }
    var selectedWeight by remember { mutableStateOf(item.weightsOrSizes.firstOrNull() ?: "") }
    var quantity by remember { mutableIntStateOf(1) }
    
    val unitPrice = item.getPrice(selectedProcessor, selectedWeight)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configure ${item.name}") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Item Header info
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = item.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Price: MK${String.format(Locale.getDefault(), "%.2f", unitPrice)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Processor / Type Selection using Glass Containers with 1x1.3 inch images
            if (item.processors.isNotEmpty()) {
                BrandTypeGlassSelector(
                    itemId = item.id,
                    processors = item.processors,
                    selectedProcessor = selectedProcessor,
                    onSelectProcessor = { selectedProcessor = it }
                )
            }

            HorizontalDivider()

            // Weight / Size Selection
            Text(text = "Select Weight / Size", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item.weightsOrSizes.forEach { weight ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (selectedWeight == weight),
                                onClick = { selectedWeight = weight }
                            )
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedWeight == weight),
                            onClick = { selectedWeight = weight }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = weight, fontSize = 16.sp)
                    }
                }
            }

            HorizontalDivider()

            // Quantity Stepper with Up & Down arrows (Non-editable text box)
            Text(text = "Quantity", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Down arrow button
                IconButton(
                    onClick = { if (quantity > 1) quantity-- },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Decrease Quantity",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                // Non-editable quantity display box
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.width(60.dp).height(40.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = quantity.toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Up arrow button
                IconButton(
                    onClick = { quantity++ },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Increase Quantity",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Total and Add to Cart
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Total:", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                    Text(
                        text = "MK${String.format(Locale.getDefault(), "%.2f", unitPrice * quantity)}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = {
                        val cartItem = CartItem(
<<<<<<< HEAD
                            itemId = item.id,
=======
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                            itemTitle = item.name,
                            selectedProcessor = selectedProcessor,
                            selectedWeight = selectedWeight,
                            quantity = quantity,
                            unitPrice = unitPrice
                        )
                        onAddToCart(cartItem)
                    },
                    modifier = Modifier.height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "Add to Cart", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
