package com.example.shopsmart.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopsmart.CompletedOrder
import com.example.shopsmart.model.CITIES_DELIVERY_LOCATIONS
import com.example.shopsmart.model.Category
import com.example.shopsmart.model.ShopItem
import com.example.shopsmart.model.User
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    categories: List<Category>,
    onAddItem: (categoryId: Int, newItem: ShopItem) -> Unit,
    onEditItem: (categoryId: Int, updatedItem: ShopItem) -> Unit,
    onDeleteItem: (itemId: String) -> Unit,
    users: List<User> = emptyList(),
    onDeleteUser: (userId: String) -> Unit = {},
    orders: List<CompletedOrder> = emptyList(),
    onUpdateOrderStatus: (orderId: String, newStatus: String) -> Unit = { _, _ -> },
    onDeleteOrder: (orderId: String) -> Unit = {},
    adminUser: User = User(
        firstName = "Enigma",
        lastName = "Sanudi",
        username = "Enigma",
        password = "Sanudi",
        phoneNumber = "+265999000000",
        email = "enigma.sanudi@shopsmart.mw",
        city = "Lilongwe",
        deliveryLocationName = "Biwi",
        category = "Offices"
    ),
    onUpdateAdminUser: (User) -> Unit = {},
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Inventory, 1: Users, 2: Orders, 3: Admin Profile

    // Add / Edit Item Dialog state
    var showAddDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<ShopItem?>(null) }

    var itemName by remember { mutableStateOf("") }
    var itemPrice by remember { mutableStateOf("") }
    var itemProcessors by remember { mutableStateOf("") }
    var itemWeights by remember { mutableStateOf("") }
    var itemImageUrl by remember { mutableStateOf("") }
    var selectedCatId by remember { mutableIntStateOf(1) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            itemImageUrl = uri.toString()
        }
    }

    // Admin Profile editable state
    var adminFirstName by remember { mutableStateOf(adminUser.firstName) }
    var adminLastName by remember { mutableStateOf(adminUser.lastName) }
    var adminPhone by remember { mutableStateOf(adminUser.phoneNumber) }
    var adminEmail by remember { mutableStateOf(adminUser.email) }
    var adminCity by remember { mutableStateOf(adminUser.city) }
    var adminLocation by remember { mutableStateOf(adminUser.deliveryLocationName) }
    var adminCategory by remember { mutableStateOf(adminUser.category) }
    var adminProfilePic by remember { mutableStateOf(adminUser.profileImageUrl) }
    var adminUpdateMsg by remember { mutableStateOf<String?>(null) }
    var adminCityLocations by remember { mutableStateOf(CITIES_DELIVERY_LOCATIONS[adminUser.city] ?: CITIES_DELIVERY_LOCATIONS["Lilongwe"]!!) }

    fun openAddDialog() {
        editingItem = null
        itemName = ""
        itemPrice = ""
        itemProcessors = "Standard"
        itemWeights = "1 Unit"
        itemImageUrl = ""
        selectedCatId = categories.firstOrNull()?.id ?: 1
        showAddDialog = true
    }

    fun openEditDialog(item: ShopItem) {
        editingItem = item
        itemName = item.name
        itemPrice = item.basePrice.toString()
        itemProcessors = item.processors.joinToString(", ")
        itemWeights = item.weightsOrSizes.joinToString(", ")
        itemImageUrl = item.imageUrl
        selectedCatId = item.categoryId
        showAddDialog = true
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text(if (editingItem == null) "Add Product & Upload Photo" else "Edit Product", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        label = { Text("Product Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = itemPrice,
                        onValueChange = { itemPrice = it.filter { char -> char.isDigit() || char == '.' } },
                        label = { Text("Base Price (MK)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = itemProcessors,
                        onValueChange = { itemProcessors = it },
                        label = { Text("Processors / Brands (comma separated)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = itemWeights,
                        onValueChange = { itemWeights = it },
                        label = { Text("Weights / Sizes (comma separated)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = itemImageUrl,
                        onValueChange = { itemImageUrl = it },
                        label = { Text("Item Photo (URL or Gallery Uri)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = { galleryLauncher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer)
                    ) {
                        Text("📁 Pick Photo from Gallery", fontWeight = FontWeight.Bold)
                    }

                    if (editingItem == null) {
                        Text("Select Category:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        categories.forEach { cat ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedCatId = cat.id }
                                    .padding(4.dp)
                            ) {
                                RadioButton(
                                    selected = (selectedCatId == cat.id),
                                    onClick = { selectedCatId = cat.id }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(cat.name, fontSize = 14.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (itemName.isNotBlank() && itemPrice.isNotBlank()) {
                            val price = itemPrice.toDoubleOrNull() ?: 50.0
                            val procList = itemProcessors.split(",").map { it.trim() }.filter { it.isNotEmpty() }.ifEmpty { listOf("Standard") }
                            val weightList = itemWeights.split(",").map { it.trim() }.filter { it.isNotEmpty() }.ifEmpty { listOf("1 Unit") }

                            val priceTableMap = mutableMapOf<Pair<String, String>, Double>()
                            for (p in procList) {
                                for (w in weightList) {
                                    priceTableMap[Pair(p, w)] = price
                                }
                            }

                            if (editingItem == null) {
                                val shopItem = ShopItem(
                                    id = "item_" + System.currentTimeMillis(),
                                    name = itemName,
                                    categoryId = selectedCatId,
                                    processors = procList,
                                    weightsOrSizes = weightList,
                                    basePrice = price,
                                    imageUrl = itemImageUrl,
                                    priceTable = priceTableMap,
                                    description = "Admin managed product with gallery photo."
                                )
                                onAddItem(selectedCatId, shopItem)
                            } else {
                                val updated = editingItem!!.copy(
                                    name = itemName,
                                    processors = procList,
                                    weightsOrSizes = weightList,
                                    basePrice = price,
                                    imageUrl = itemImageUrl,
                                    priceTable = priceTableMap
                                )
                                onEditItem(updated.categoryId, updated)
                            }
                            showAddDialog = false
                        }
                    }
                ) {
                    Text(if (editingItem == null) "Add Product" else "Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(end = 8.dp))
                            Text("System Admin Portal (Enigma Sanudi)", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        if (selectedTab == 0) {
                            IconButton(onClick = { openAddDialog() }) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Product & Photo", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                TabRow(selectedTabIndex = selectedTab) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Inventory") },
                        icon = { Icon(Icons.Default.Inventory, contentDescription = null) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Users (${users.size})") },
                        icon = { Icon(Icons.Default.People, contentDescription = null) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("Orders (${orders.size})") },
                        icon = { Icon(Icons.Default.ReceiptLong, contentDescription = null) }
                    )
                    Tab(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        text = { Text("My Profile") },
                        icon = { Icon(Icons.Default.Person, contentDescription = null) }
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    // --- INVENTORY TAB ---
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        categories.forEach { category ->
                            item {
                                Text(text = category.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                            }
                            items(category.items) { item ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(text = item.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                            if (item.imageUrl.isNotBlank()) {
                                                Text(text = "Photo: ${item.imageUrl}", fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary)
                                            }
                                            Text(text = "Processors: ${item.processors.joinToString(", ")} | Sizes: ${item.weightsOrSizes.joinToString(", ")}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                                            Text(text = "Base Price: MK${String.format(Locale.getDefault(), "%.2f", item.basePrice)}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                                        }

                                        Row {
                                            IconButton(onClick = { openEditDialog(item) }) {
                                                Icon(
                                                    imageVector = Icons.Default.Edit,
                                                    contentDescription = "Edit Item",
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                            IconButton(onClick = { onDeleteItem(item.id) }) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "Delete Item",
                                                    tint = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // --- USERS MANAGEMENT TAB ---
                    if (users.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No registered users yet.", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        }
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(users) { user ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(text = "${user.firstName} ${user.lastName} (@${user.username})", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                            Text(text = "Phone: ${user.phoneNumber} | Email: ${user.email}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                                            Text(text = "City: ${user.city} — Location: ${user.deliveryLocationName} | Category: ${user.category}", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                                        }
                                        IconButton(onClick = { onDeleteUser(user.id) }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete User", tint = MaterialTheme.colorScheme.error)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // --- CUSTOMER ORDERS TAB ---
                    if (orders.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No customer orders received yet.", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        }
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(orders) { order ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(14.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(text = "Order: ${order.orderId}", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
                                            Surface(
                                                color = if (order.status == "Delivered") MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.errorContainer,
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = order.status,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }

                                        Text(text = "Customer: ${order.firstName} ${order.lastName} (${order.phoneNumber})", fontSize = 13.sp)
                                        Text(text = "Location: ${order.deliveryLocation.name}", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                        Text(text = "Time of Order: ${order.timestamp}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                                        Text(text = "Items: ${order.items.sumOf { it.quantity }} | Total: MK${String.format(Locale.getDefault(), "%.2f", order.items.sumOf { it.totalPrice })}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Button(
                                                onClick = {
                                                    onUpdateOrderStatus(order.orderId, "Delivered")
                                                },
                                                enabled = order.status != "Delivered",
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (order.status == "Delivered") MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                                                ),
                                                modifier = Modifier.height(36.dp)
                                            ) {
                                                Text(
                                                    text = if (order.status == "Delivered") "✓ Delivered (Done)" else "✓ Done",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }

                                            IconButton(
                                                onClick = { onDeleteOrder(order.orderId) }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "Delete Order",
                                                    tint = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                3 -> {
                    // --- ADMIN PROFILE TAB ---
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
                        }

                        Text("Admin Username: @${adminUser.username}", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text("Edit Admin Details", fontWeight = FontWeight.Bold, fontSize = 15.sp)

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(value = adminFirstName, onValueChange = { adminFirstName = it }, label = { Text("First Name") }, modifier = Modifier.weight(1f), singleLine = true)
                                    OutlinedTextField(value = adminLastName, onValueChange = { adminLastName = it }, label = { Text("Last Name") }, modifier = Modifier.weight(1f), singleLine = true)
                                }

                                OutlinedTextField(value = adminPhone, onValueChange = { adminPhone = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                                OutlinedTextField(value = adminEmail, onValueChange = { adminEmail = it }, label = { Text("Email Address") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                                OutlinedTextField(value = adminProfilePic, onValueChange = { adminProfilePic = it }, label = { Text("Profile Picture URL / Photo Path") }, modifier = Modifier.fillMaxWidth(), singleLine = true)

                                Text("Category:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    listOf("Students", "Offices", "Families").forEach { cat ->
                                        val isSel = (adminCategory == cat)
                                        Button(
                                            onClick = { adminCategory = cat },
                                            colors = ButtonDefaults.buttonColors(containerColor = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(cat, fontSize = 12.sp)
                                        }
                                    }
                                }

                                Text("City:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    listOf("Lilongwe", "Blantyre", "Zomba").forEach { c ->
                                        val isSel = (adminCity == c)
                                        Button(
                                            onClick = {
                                                adminCity = c
                                                adminCityLocations = CITIES_DELIVERY_LOCATIONS[c]!!
                                                adminLocation = adminCityLocations.first().name
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(c, fontSize = 12.sp)
                                        }
                                    }
                                }

                                Text("Delivery Area:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                adminCityLocations.forEach { loc ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { adminLocation = loc.name }
                                            .padding(2.dp)
                                    ) {
                                        RadioButton(selected = (adminLocation == loc.name), onClick = { adminLocation = loc.name })
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(loc.name, fontSize = 14.sp)
                                    }
                                }

                                if (adminUpdateMsg != null) {
                                    Text(adminUpdateMsg!!, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }

                                Button(
                                    onClick = {
                                        val updatedAdmin = adminUser.copy(
                                            firstName = adminFirstName,
                                            lastName = adminLastName,
                                            phoneNumber = adminPhone,
                                            email = adminEmail,
                                            city = adminCity,
                                            deliveryLocationName = adminLocation,
                                            category = adminCategory,
                                            profileImageUrl = adminProfilePic
                                        )
                                        onUpdateAdminUser(updatedAdmin)
                                        adminUpdateMsg = "Admin profile updated successfully!"
                                    },
                                    modifier = Modifier.fillMaxWidth().height(48.dp)
                                ) {
                                    Text("Save Admin Profile", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
