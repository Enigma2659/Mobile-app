package com.example.shopsmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopsmart.model.CartItem
import com.example.shopsmart.model.DELIVERY_LOCATIONS
import com.example.shopsmart.model.DeliveryLocation
import com.example.shopsmart.model.SHOP_CATEGORIES
import com.example.shopsmart.model.ShopItem
import com.example.shopsmart.ui.*
import com.example.shopsmart.ui.theme.ShopSmartTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class NavDestination(
    val title: String,
    val icon: ImageVector
) {
    CATALOG("Catalog", Icons.Default.ListAlt),
    PROMOTIONS("Promotions", Icons.Default.Star),
    SMART_AI("Smart AI Budget", Icons.Default.AutoAwesome),
    MY_CART("My Cart", Icons.Default.ShoppingCart),
    ADMIN_PANEL("System Admin Panel", Icons.Default.Store),
    CONTACT_US("Contact Us", Icons.Default.Phone),
    ABOUT_US("More About Us", Icons.Default.Info)
}

data class CompletedOrder(
    val orderId: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val deliveryLocation: DeliveryLocation,
    val items: List<CartItem>
)

data class CheckoutCustomerState(
    val firstName: String,
    val lastName: String,
    val location: DeliveryLocation
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopSmartTheme {
                var showSplash by remember { mutableStateOf(true) }
                var currentDestination by remember { mutableStateOf(NavDestination.CATALOG) }
                var cartItems by remember { mutableStateOf(listOf<CartItem>()) }
                var categoriesState by remember { mutableStateOf(SHOP_CATEGORIES) }
                var activeOrder by remember { mutableStateOf<CompletedOrder?>(null) }
                var selectedItemForDetail by remember { mutableStateOf<ShopItem?>(null) }
                var checkoutCustomerState by remember { mutableStateOf<CheckoutCustomerState?>(null) }
                var isPaymentScreen by remember { mutableStateOf(false) }

                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                if (showSplash) {
                    SplashScreen(onSplashFinished = { showSplash = false })
                } else {
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            ModalDrawerSheet(
                                modifier = Modifier.width(280.dp)
                            ) {
                                // Header
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                        .padding(24.dp)
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(
                                            text = "🛒 Shop Simple",
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = "For Offices, Students & Families",
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Navigation Items
                                NavDestination.entries.forEach { destination ->
                                    NavigationDrawerItem(
                                        label = {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = destination.title,
                                                    fontWeight = if (currentDestination == destination) FontWeight.Bold else FontWeight.Normal
                                                )
                                                if (destination == NavDestination.MY_CART && cartItems.isNotEmpty()) {
                                                    Badge(
                                                        containerColor = MaterialTheme.colorScheme.primary,
                                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                                    ) {
                                                        Text(text = cartItems.sumOf { it.quantity }.toString())
                                                    }
                                                }
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = destination.icon,
                                                contentDescription = destination.title,
                                                tint = if (currentDestination == destination) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                            )
                                        },
                                        selected = currentDestination == destination,
                                        onClick = {
                                            currentDestination = destination
                                            activeOrder = null
                                            selectedItemForDetail = null
                                            checkoutCustomerState = null
                                            isPaymentScreen = false
                                            scope.launch { drawerState.close() }
                                        },
                                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                    )
                                }
                            }
                        }
                    ) {
                        // Main Screen Body
                        if (selectedItemForDetail != null) {
                            ItemDetailScreen(
                                item = selectedItemForDetail!!,
                                onAddToCart = { newItem ->
                                    cartItems = addOrMergeCartItem(cartItems, newItem)
                                    selectedItemForDetail = null
                                },
                                onBack = { selectedItemForDetail = null }
                            )
                        } else if (activeOrder != null) {
                            OrderSummaryScreen(
                                orderId = activeOrder!!.orderId,
                                firstName = activeOrder!!.firstName,
                                lastName = activeOrder!!.lastName,
                                phoneNumber = activeOrder!!.phoneNumber,
                                deliveryLocation = activeOrder!!.deliveryLocation,
                                cartItems = activeOrder!!.items,
                                onFinish = {
                                    activeOrder = null
                                    currentDestination = NavDestination.CATALOG
                                }
                            )
                        } else {
                            when (currentDestination) {
                                NavDestination.CATALOG -> {
                                    CatalogScreen(
                                        cartItems = cartItems,
                                        categories = categoriesState,
                                        onAddToCart = { newItem ->
                                            cartItems = addOrMergeCartItem(cartItems, newItem)
                                        },
                                        onSelectItem = { shopItem ->
                                            selectedItemForDetail = shopItem
                                        },
                                        onOpenDrawer = { scope.launch { drawerState.open() } },
                                        onOpenSmartAI = { currentDestination = NavDestination.SMART_AI },
                                        onProceedToCheckout = { currentDestination = NavDestination.MY_CART }
                                    )
                                }

                                NavDestination.PROMOTIONS -> {
                                    PromotionsScreen(
                                        onAddToCart = { newItem ->
                                            cartItems = addOrMergeCartItem(cartItems, newItem)
                                        },
                                        onOpenCart = { currentDestination = NavDestination.MY_CART },
                                        cartItemsCount = cartItems.sumOf { it.quantity },
                                        onBack = { currentDestination = NavDestination.CATALOG }
                                    )
                                }

                                NavDestination.SMART_AI -> {
                                    SmartAIBudgetScreen(
                                        onAddMultipleToCart = { bundleItems ->
                                            var updated = cartItems
                                            bundleItems.forEach { item ->
                                                updated = addOrMergeCartItem(updated, item)
                                            }
                                            cartItems = updated
                                        },
                                        onOpenCart = { currentDestination = NavDestination.MY_CART },
                                        cartItemsCount = cartItems.sumOf { it.quantity },
                                        onBack = { currentDestination = NavDestination.CATALOG }
                                    )
                                }

                                NavDestination.MY_CART -> {
                                    if (isPaymentScreen && checkoutCustomerState != null) {
                                        CheckoutScreen(
                                            firstName = checkoutCustomerState!!.firstName,
                                            lastName = checkoutCustomerState!!.lastName,
                                            deliveryLocation = checkoutCustomerState!!.location,
                                            cartItems = cartItems,
                                            onBack = { isPaymentScreen = false },
                                            onSubmitOrder = { airtelNumber ->
                                                val timeStamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
                                                val orderId = "SS-$timeStamp"
                                                val completed = CompletedOrder(
                                                    orderId = orderId,
                                                    firstName = checkoutCustomerState!!.firstName,
                                                    lastName = checkoutCustomerState!!.lastName,
                                                    phoneNumber = airtelNumber,
                                                    deliveryLocation = checkoutCustomerState!!.location,
                                                    items = cartItems
                                                )
                                                activeOrder = completed
                                                cartItems = emptyList()
                                                checkoutCustomerState = null
                                                isPaymentScreen = false
                                            }
                                        )
                                    } else if (checkoutCustomerState != null) {
                                        CustomerInfoScreen(
                                            onBack = { checkoutCustomerState = null },
                                            onProceedToPayment = { fName, lName, loc ->
                                                checkoutCustomerState = CheckoutCustomerState(fName, lName, loc)
                                                isPaymentScreen = true
                                            }
                                        )
                                    } else {
                                        CartAndCheckoutScreen(
                                            cartItems = cartItems,
                                            onUpdateQuantity = { targetItem, newQty ->
                                                cartItems = cartItems.map {
                                                    if (it.id == targetItem.id) it.copy(quantity = newQty) else it
                                                }
                                            },
                                            onRemoveItem = { targetItem ->
                                                cartItems = cartItems.filterNot { it.id == targetItem.id }
                                            },
                                            onProceedToCustomerInfo = {
                                                checkoutCustomerState = CheckoutCustomerState("", "", DELIVERY_LOCATIONS.first())
                                            },
                                            onContinueShopping = { currentDestination = NavDestination.CATALOG },
                                            onBack = { currentDestination = NavDestination.CATALOG }
                                        )
                                    }
                                }

                                NavDestination.ADMIN_PANEL -> {
                                    AdminPanelScreen(
                                        categories = categoriesState,
                                        onAddItem = { catId, newItem ->
                                            categoriesState = categoriesState.map { cat ->
                                                if (cat.id == catId) cat.copy(items = cat.items + newItem) else cat
                                            }
                                        },
                                        onDeleteItem = { itemId ->
                                            categoriesState = categoriesState.map { cat ->
                                                cat.copy(items = cat.items.filterNot { it.id == itemId })
                                            }
                                        },
                                        onBack = { currentDestination = NavDestination.CATALOG }
                                    )
                                }

                                NavDestination.CONTACT_US -> {
                                    ContactUsScreen(
                                        onBack = { currentDestination = NavDestination.CATALOG }
                                    )
                                }

                                NavDestination.ABOUT_US -> {
                                    AboutUsScreen(
                                        onBack = { currentDestination = NavDestination.CATALOG }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun addOrMergeCartItem(currentList: List<CartItem>, newItem: CartItem): List<CartItem> {
        val existingIndex = currentList.indexOfFirst {
            it.itemTitle == newItem.itemTitle &&
                    it.selectedProcessor == newItem.selectedProcessor &&
                    it.selectedWeight == newItem.selectedWeight
        }
        return if (existingIndex != -1) {
            val existing = currentList[existingIndex]
            val updated = currentList.toMutableList()
            updated[existingIndex] = existing.copy(quantity = existing.quantity + newItem.quantity)
            updated
        } else {
            currentList + newItem
        }
    }
}

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "alpha"
    )
    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.7f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "scale"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(1800)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .graphicsLayer(alpha = alphaAnim.value, scaleX = scaleAnim.value, scaleY = scaleAnim.value)
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFFE94B4B),
                modifier = Modifier.size(90.dp),
                shadowElevation = 8.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "SS",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Shop ",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF5B7CFA)
                )
                Text(
                    text = "Simple",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFE94B4B)
                )
            }

            Text(
                text = "For Offices, Students & Families",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF6E6E77)
            )
        }
    }
}
