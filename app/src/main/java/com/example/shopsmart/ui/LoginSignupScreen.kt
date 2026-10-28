package com.example.shopsmart.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopsmart.model.CITIES_DELIVERY_LOCATIONS
import com.example.shopsmart.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginSignupScreen(
    onLoginSuccess: (User) -> Unit,
    onAdminLoginSuccess: () -> Unit,
    onContinueAsGuest: () -> Unit,
    registeredUsers: List<User>,
    onRegisterUser: (User) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Login, 1: Sign Up, 2: Admin

    // Login state
    var loginUsername by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf<String?>(null) }

    // Signup state
    var signUpFirstName by remember { mutableStateOf("") }
    var signUpLastName by remember { mutableStateOf("") }
    var signUpPhone by remember { mutableStateOf("") }
    var signUpEmail by remember { mutableStateOf("") }
    var signUpPassword by remember { mutableStateOf("") }
    var signUpConfirmPassword by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("Lilongwe") }
    var selectedPersona by remember { mutableStateOf("Student") } // Student, Office, Family
    var signUpError by remember { mutableStateOf<String?>(null) }

    // Admin state
    var adminUser by remember { mutableStateOf("") }
    var adminPass by remember { mutableStateOf("") }
    var adminError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🛒", fontSize = 24.sp, modifier = Modifier.padding(end = 8.dp))
                        Text(
                            text = "Shop Simple Portal",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
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
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Welcome Header
            Text(
                text = "Welcome to Shop Simple",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Log In, Sign Up, or Access Admin Portal",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            // Tabs for Login / Sign Up / Admin
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Log In", fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Sign Up", fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.ShoppingBag, contentDescription = null) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Admin", fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = null) }
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    when (selectedTab) {
                        0 -> {
                            // --- LOGIN TAB ---
                            Text(text = "Customer Log In", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(text = "Use your Username and Password", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))

                            OutlinedTextField(
                                value = loginUsername,
                                onValueChange = { loginUsername = it },
                                label = { Text("Username (e.g. BSanudi)") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = loginPassword,
                                onValueChange = { loginPassword = it },
                                label = { Text("Password") },
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            if (loginError != null) {
                                Text(text = loginError!!, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                            }

                            Button(
                                onClick = {
                                    if (loginUsername.isBlank() || loginPassword.isBlank()) {
                                        loginError = "Please enter both username and password."
                                        return@Button
                                    }
                                    val matchedUser = registeredUsers.find {
                                        it.username.equals(loginUsername.trim(), true) && it.password == loginPassword
                                    }
                                    if (matchedUser != null) {
                                        loginError = null
                                        onLoginSuccess(matchedUser)
                                    } else {
                                        loginError = "User not found. Please Sign Up first."
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                Text("Log In", fontWeight = FontWeight.Bold)
                            }
                        }

                        1 -> {
                            // --- SIGN UP TAB ---
                            Text(text = "Create Customer Account", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = signUpFirstName,
                                    onValueChange = { signUpFirstName = it },
                                    label = { Text("First Name *") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = signUpLastName,
                                    onValueChange = { signUpLastName = it },
                                    label = { Text("Surname *") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                            }

                            OutlinedTextField(
                                value = signUpPhone,
                                onValueChange = { signUpPhone = it },
                                label = { Text("Phone Number *") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = signUpEmail,
                                onValueChange = { signUpEmail = it },
                                label = { Text("Email Address") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = signUpPassword,
                                onValueChange = { signUpPassword = it },
                                label = { Text("Password *") },
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = signUpConfirmPassword,
                                onValueChange = { signUpConfirmPassword = it },
                                label = { Text("Confirm Password *") },
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            // City Selection
                            Text("Select City:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("Lilongwe", "Blantyre", "Zomba").forEach { city ->
                                    val isSelected = (selectedCity == city)
                                    Button(
                                        onClick = { selectedCity = city },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(city, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                                    }
                                }
                            }

                            // Type of Person / Persona (Student, Office, Family)
                            Text("Type of Person (AI Persona):", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("Student", "Office", "Family").forEach { persona ->
                                    val isSelected = (selectedPersona == persona)
                                    Button(
                                        onClick = { selectedPersona = persona },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(persona, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                                    }
                                }
                            }

                            if (signUpError != null) {
                                Text(text = signUpError!!, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                            }

                            Button(
                                onClick = {
                                    if (signUpFirstName.isBlank() || signUpLastName.isBlank() || signUpPhone.isBlank() || signUpPassword.isBlank()) {
                                        signUpError = "Please fill in all required fields."
                                        return@Button
                                    }
                                    if (signUpPassword != signUpConfirmPassword) {
                                        signUpError = "Passwords do not match."
                                        return@Button
                                    }

                                    // Username rule: First letter of first name + surname (e.g. Bridget Sanudi -> BSanudi)
                                    val firstInitial = signUpFirstName.trim().firstOrNull()?.uppercase() ?: ""
                                    val surname = signUpLastName.trim().replace(" ", "")
                                    val generatedUsername = "$firstInitial$surname"

                                    signUpError = null
                                    val newUser = User(
                                        firstName = signUpFirstName,
                                        lastName = signUpLastName,
                                        username = generatedUsername,
                                        password = signUpPassword,
                                        phoneNumber = signUpPhone,
                                        email = signUpEmail.ifBlank { "$signUpPhone@shop.mw" },
                                        city = selectedCity,
                                        deliveryLocationName = CITIES_DELIVERY_LOCATIONS[selectedCity]?.first()?.name ?: "Biwi",
                                        category = selectedPersona
                                    )
                                    onRegisterUser(newUser)
                                    onLoginSuccess(newUser)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                Text("Sign Up & Start Shopping", fontWeight = FontWeight.Bold)
                            }
                        }

                        2 -> {
                            // --- ADMIN LOGIN TAB ---
                            Text(text = "System Admin Log In", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                            OutlinedTextField(
                                value = adminUser,
                                onValueChange = { adminUser = it },
                                label = { Text("Admin Username") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = adminPass,
                                onValueChange = { adminPass = it },
                                label = { Text("Admin Password") },
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            if (adminError != null) {
                                Text(text = adminError!!, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                            }

                            Button(
                                onClick = {
                                    if (adminUser.trim().equals("Enigma", true) && adminPass == "Sanudi") {
                                        adminError = null
                                        onAdminLoginSuccess()
                                    } else {
                                        adminError = "Invalid admin credentials. Use Enigma / Sanudi."
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                            ) {
                                Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                                Text("Log In as Admin", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Continue As Guest
            OutlinedButton(
                onClick = onContinueAsGuest,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Continue As Guest", fontWeight = FontWeight.Bold)
            }
        }
    }
}
