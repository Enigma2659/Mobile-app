package com.example.shopsmart.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopsmart.R
import com.example.shopsmart.model.CartItem
import com.example.shopsmart.model.Category
import com.example.shopsmart.model.SHOP_CATEGORIES
import com.example.shopsmart.model.ShopItem
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    cartItems: List<CartItem>,
    categories: List<Category> = SHOP_CATEGORIES,
    onAddToCart: (CartItem) -> Unit,
    onSelectItem: (ShopItem) -> Unit,
    onOpenDrawer: () -> Unit,
    onOpenSmartAI: () -> Unit,
    onProceedToCheckout: () -> Unit
) {
    val totalCartAmount = cartItems.sumOf { it.totalPrice }
    val totalCartCount = cartItems.sumOf { it.quantity }

    var selectedCategoryId by remember { mutableIntStateOf(0) } // 0 = All

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "SS",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = "Shop Simple", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "For Offices, Students & Families",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Open Navigation Menu"
                        )
                    }
                },
                actions = {
                    BadgedBox(
                        badge = {
                            if (totalCartCount > 0) {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ) {
                                    Text(text = totalCartCount.toString())
                                }
                            }
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        IconButton(onClick = onProceedToCheckout) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "My Cart"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                Surface(
                    tonalElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "$totalCartCount items in cart",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "Total: MK${String.format(Locale.getDefault(), "%.2f", totalCartAmount)}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Button(
                            onClick = onProceedToCheckout,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(text = "Checkout", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Smart AI Banner Callout
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenSmartAI() },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "🤖 Smart AI Budgeting Assistant",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Auto-calculate groceries based on team/family size & income budget!",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }

                        Text(
                            text = "TRY NOW →",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Category Filter Pills
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Browse Categories",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = selectedCategoryId == 0,
                                onClick = { selectedCategoryId = 0 },
                                label = { Text("All Items") }
                            )
                        }
                        items(categories) { category ->
                            FilterChip(
                                selected = selectedCategoryId == category.id,
                                onClick = { selectedCategoryId = category.id },
                                label = { Text(category.name) }
                            )
                        }
                    }
                }
            }

            // Category list with Shop Items
            val displayedCategories = if (selectedCategoryId == 0) {
                categories
            } else {
                categories.filter { it.id == selectedCategoryId }
            }

            items(displayedCategories) { category ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "${category.id}. ${category.name}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        category.items.forEachIndexed { idx, shopItem ->
                            CatalogItemRow(
                                shopItem = shopItem,
                                onAddToCart = onAddToCart,
                                onSelectItem = onSelectItem
                            )
                            if (idx < category.items.lastIndex) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CatalogItemRow(
    shopItem: ShopItem,
    onAddToCart: (CartItem) -> Unit,
    onSelectItem: (ShopItem) -> Unit
) {
    var selectedProcessor by remember { mutableStateOf(shopItem.processors.firstOrNull() ?: "") }
    var selectedWeight by remember { mutableStateOf(shopItem.weightsOrSizes.firstOrNull() ?: "") }
    var quantity by remember { mutableIntStateOf(2) }
    var addedFeedback by remember { mutableStateOf(false) }

    val currentUnitPrice = shopItem.getPrice(selectedProcessor, selectedWeight)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onSelectItem(shopItem) }
            ) {
                Text(
                    text = shopItem.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                if (shopItem.description.isNotBlank()) {
                    Text(
                        text = shopItem.description,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "MK${String.format(Locale.getDefault(), "%.2f", currentUnitPrice * quantity)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "MK${String.format(Locale.getDefault(), "%.2f", currentUnitPrice)} each",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                TextButton(
                    onClick = { onSelectItem(shopItem) },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Customize →", fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }

        // Type / Processor Glass Containers with 1x1.3 inch images and glass option button
        if (shopItem.processors.isNotEmpty()) {
            BrandTypeGlassSelector(
                itemId = shopItem.id,
                processors = shopItem.processors,
                selectedProcessor = selectedProcessor,
                onSelectProcessor = { selectedProcessor = it }
            )
        }

        // Weight / Size Selector Chips
        if (shopItem.weightsOrSizes.size > 1) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(text = "Size / Weight:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(shopItem.weightsOrSizes) { weight ->
                        FilterChip(
                            selected = (selectedWeight == weight),
                            onClick = { selectedWeight = weight },
                            label = { Text(weight, fontSize = 11.sp) },
                            modifier = Modifier.height(28.dp)
                        )
                    }
                }
            }
        }

        // Quantity controls (+ and - symbols) + Add to Cart button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(text = "Qty: ", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                IconButton(
                    onClick = { if (quantity > 1) quantity-- },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease"
                    )
                }

                Text(
                    text = quantity.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )

                IconButton(
                    onClick = { quantity++ },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase"
                    )
                }
            }

            Button(
                onClick = {
                    val cartItem = CartItem(
                        itemId = shopItem.id,
                        itemTitle = shopItem.name,
                        selectedProcessor = selectedProcessor,
                        selectedWeight = selectedWeight,
                        quantity = quantity,
                        unitPrice = currentUnitPrice
                    )
                    onAddToCart(cartItem)
                    addedFeedback = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (addedFeedback) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text(
                    text = if (addedFeedback) "✓ Added!" else "+ Add to Cart",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

fun getDrawableRes(itemId: String, processor: String, itemTitle: String = ""): Int {
    val id = if (itemId.isNotBlank()) itemId else when {
        itemTitle.contains("Rice", ignoreCase = true) -> "rice"
        itemTitle.contains("Flour", ignoreCase = true) -> "flour"
        itemTitle.contains("Sugar", ignoreCase = true) -> "sugar"
        itemTitle.contains("Oil", ignoreCase = true) -> "cooking_oil"
        itemTitle.contains("Toothpaste", ignoreCase = true) || itemTitle.contains("Colgate", ignoreCase = true) -> "toothpaste"
        itemTitle.contains("Sausage", ignoreCase = true) -> "sausage"
        itemTitle.contains("Chicken", ignoreCase = true) || itemTitle.contains("Liver", ignoreCase = true) || itemTitle.contains("Wings", ignoreCase = true) -> "chicken_parts"
        else -> "icon"
    }
    return when (id) {
        "rice" -> when (processor) {
            "Kilombero" -> R.drawable.kilombero_rice
            "Pishori" -> R.drawable.pishori_rice
            else -> R.drawable.kilombero_rice
        }
        "flour" -> when (processor) {
            "A1" -> R.drawable.a1_flour
            "Rab Processors" -> R.drawable.kula_maize_flour
            else -> R.drawable.a1_flour
        }
        "sugar" -> R.drawable.illovo_sugar
        "cooking_oil" -> when (processor) {
            "Soyalite" -> R.drawable.soyalite_cooking_oil
            else -> R.drawable.kukoma_cooking_oil
        }
        "sausage" -> R.drawable.beef_sausage
        "chicken_parts" -> when (processor) {
            "Full Chicken" -> R.drawable.full_chicken
            "Feets" -> R.drawable.chicken_wings
            "Liver" -> R.drawable.chicken_liver
            "Wings" -> R.drawable.chicken_wings
            else -> R.drawable.full_chicken
        }
        "toothpaste" -> when (processor) {
            "Charcoal" -> R.drawable.charcoal_colgate
            else -> R.drawable.colgate_natural_herb
        }
        else -> R.drawable.icon
    }
}

@Composable
fun BrandTypeGlassSelector(
    itemId: String,
    processors: List<String>,
    selectedProcessor: String,
    onSelectProcessor: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "Brand / Type:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(processors) { processor ->
                val isSelected = (selectedProcessor == processor)
                val drawableRes = getDrawableRes(itemId, processor)

                // Glass container card (1.35 inch wide = ~97 dp + padding = 109 dp, 1.68 inch height = ~121 dp + padding + button = 161 dp)
                Surface(
                    modifier = Modifier
                        .width(109.dp)
                        .height(161.dp)
                        .clickable { onSelectProcessor(processor) },
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
                    } else {
                        Color.Transparent
                    },
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    ),
                    tonalElevation = if (isSelected) 6.dp else 0.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Picture 1.35 inch wide (97 dp) and height 1.68 inch (121 dp)
                        Image(
                            painter = painterResource(id = drawableRes),
                            contentDescription = processor,
                            modifier = Modifier
                                .width(97.dp)
                                .height(121.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )

                        // Glass option button inside captioned with their name
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(28.dp),
                            tonalElevation = 2.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = processor,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
