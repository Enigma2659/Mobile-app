package com.example.shopsmart.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopsmart.model.CITIES_DELIVERY_LOCATIONS
import com.example.shopsmart.model.User
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    registeredUsers: List<User>,
    onLoginSuccess: (User) -> Unit,
    onAdminLoginSuccess: () -> Unit,
    onContinueAsGuest: () -> Unit
) {
    var isSignUpMode by remember { mutableStateOf(false) }
    var showAdminLoginDialog by remember { mutableStateOf(false) }

    // Login Form State
    var loginUsername by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }
    var loginPasswordVisible by remember { mutableStateOf(false) }
    var loginErrorMessage by remember { mutableStateOf<String?>(null) }

    // Sign Up Form State
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var signUpUsername by remember { mutableStateOf("") }
    var signUpPassword by remember { mutableStateOf("") }
    var signUpPasswordVisible by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    
    var selectedCity by remember { mutableStateOf("Lilongwe") }
    val availableLocations = CITIES_DELIVERY_LOCATIONS[selectedCity] ?: CITIES_DELIVERY_LOCATIONS["Lilongwe"]!!
    var selectedLocationName by remember { mutableStateOf(availableLocations.first().name) }

    // Persona Selection (Office, Student, Family)
    var isOfficeSelected by remember { mutableStateOf(false) }
    var isStudentSelected by remember { mutableStateOf(false) }
    var isFamilySelected by remember { mutableStateOf(false) }

    var signUpErrorMessage by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }

    // Admin Login Dialog State
    var adminUserInput by remember { mutableStateOf("") }
    var adminPassInput by remember { mutableStateOf("") }
    var adminError by remember { mutableStateOf<String?>(null) }

    // Persona Validation Logic:
    // Disallowed combos:
    // 1. All three selected (Office + Student + Family)
    // 2. Student + Office
    // Allowed combos:
    // - Single: Office, Student, Family
    // - Dual combo 1: Student + Family
    // - Dual combo 2: Family + Office
    val isAllThreeSelected = isOfficeSelected && isStudentSelected && isFamilySelected
    val isStudentAndOfficeSelected = isStudentSelected && isOfficeSelected
    val isPersonaInvalid = isAllThreeSelected || isStudentAndOfficeSelected
    val isAnyPersonaSelected = isOfficeSelected || isStudentSelected || isFamilySelected

    val personaValidationError = when {
        isAllThreeSelected -> "You cannot select all three personas (Office + Student + Family)."
        isStudentAndOfficeSelected -> "Invalid selection: A person cannot be both a Student and an Office."
        !isAnyPersonaSelected -> "Please select at least one category (Office, Student, or Family)."
        else -> null
    }

    if (showAdminLoginDialog) {
        AlertDialog(
            onDismissRequest = { showAdminLoginDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("System Admin Access", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Enter System Admin Credentials:", fontSize = 13.sp)
                    OutlinedTextField(
                        value = adminUserInput,
                        onValueChange = { adminUserInput = it },
                        label = { Text("Admin Username") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = adminPassInput,
                        onValueChange = { adminPassInput = it },
                        label = { Text("Admin Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (adminError != null) {
                        Text(
                            text = adminError!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (adminUserInput.trim().equals("Enigma", true) && adminPassInput == "Sanudi") {
                            adminError = null
                            showAdminLoginDialog = false
                            onAdminLoginSuccess()
                        } else {
                            adminError = "Invalid admin credentials! (Use Enigma / Sanudi)"
                        }
                    }
                ) {
                    Text("Log In as Admin")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAdminLoginDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // App Brand Logo & Title
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(72.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "🛒",
                        fontSize = 36.sp
                    )
                }
            }

            Text(
                text = "Shop Simple",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = if (isSignUpMode) "Create Your Personalized Account" else "Welcome Back! Sign In to Continue",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (!isSignUpMode) {
                // ==========================================
                // LOGIN FORM
                // ==========================================
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Account Sign In",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            value = loginUsername,
                            onValueChange = {
                                loginUsername = it
                                loginErrorMessage = null
                            },
                            label = { Text("Username") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        OutlinedTextField(
                            value = loginPassword,
                            onValueChange = {
                                loginPassword = it
                                loginErrorMessage = null
                            },
                            label = { Text("Password") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            trailingIcon = {
                                IconButton(onClick = { loginPasswordVisible = !loginPasswordVisible }) {
                                    Icon(
                                        imageVector = if (loginPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = "Toggle password"
                                    )
                                }
                            },
                            visualTransformation = if (loginPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        if (loginErrorMessage != null) {
                            Text(
                                text = loginErrorMessage!!,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = {
                                val u = loginUsername.trim()
                                val p = loginPassword.trim()
                                if (u.isBlank() || p.isBlank()) {
                                    loginErrorMessage = "Please fill in both Username and Password."
                                    return@Button
                                }

                                // Check Admin credentials
                                if (u.equals("Enigma", ignoreCase = true) && p == "Sanudi") {
                                    onAdminLoginSuccess()
                                    return@Button
                                }

                                // Check registered users
                                val matchedUser = registeredUsers.find {
                                    it.username.equals(u, ignoreCase = true) && it.password == p
                                }

                                if (matchedUser != null) {
                                    onLoginSuccess(matchedUser)
                                } else {
                                    loginErrorMessage = "Invalid Username or Password. If you don't have an account, please Sign Up."
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text(text = "Sign In →", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Switch to Sign Up
                OutlinedButton(
                    onClick = {
                        isSignUpMode = true
                        loginErrorMessage = null
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Don't have an account? Sign Up Here", fontWeight = FontWeight.Bold)
                }

                // Admin Login Button
                TextButton(
                    onClick = {
                        adminUserInput = "Enigma"
                        adminPassInput = ""
                        adminError = null
                        showAdminLoginDialog = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Log in as System Admin", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }

                // Guest option
                TextButton(
                    onClick = onContinueAsGuest
                ) {
                    Text("Skip for now (Browse as Guest)", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }

            } else {
                // ==========================================
                // SIGN UP FORM
                // ==========================================
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Register New Account",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // First Name & Last Name
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = firstName,
                                onValueChange = { firstName = it },
                                label = { Text("First Name *") },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            )

                            OutlinedTextField(
                                value = lastName,
                                onValueChange = { lastName = it },
                                label = { Text("Last Name *") },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        // Username & Password
                        OutlinedTextField(
                            value = signUpUsername,
                            onValueChange = { signUpUsername = it },
                            label = { Text("Choose Username *") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        OutlinedTextField(
                            value = signUpPassword,
                            onValueChange = { signUpPassword = it },
                            label = { Text("Choose Password *") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            trailingIcon = {
                                IconButton(onClick = { signUpPasswordVisible = !signUpPasswordVisible }) {
                                    Icon(
                                        imageVector = if (signUpPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = "Toggle password"
                                    )
                                }
                            },
                            visualTransformation = if (signUpPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        // Phone & Email
                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = { Text("Airtel Phone Number (+265) *") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email Address *") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        HorizontalDivider()

                        // City Selection
                        Text(text = "Select Primary City *", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Lilongwe", "Blantyre", "Zomba").forEach { city ->
                                FilterChip(
                                    selected = (selectedCity == city),
                                    onClick = {
                                        selectedCity = city
                                        val newLocs = CITIES_DELIVERY_LOCATIONS[city] ?: CITIES_DELIVERY_LOCATIONS["Lilongwe"]!!
                                        selectedLocationName = newLocs.first().name
                                    },
                                    label = { Text(city, fontSize = 12.sp) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // Delivery Location Area Selection
                        Text(text = "Default Delivery Area ($selectedCity) *", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            availableLocations.chunked(2).forEach { rowLocs ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    rowLocs.forEach { loc ->
                                        FilterChip(
                                            selected = (selectedLocationName == loc.name),
                                            onClick = { selectedLocationName = loc.name },
                                            label = { Text(loc.name, fontSize = 11.sp) },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                    if (rowLocs.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }

                        HorizontalDivider()

                        // Persona Selection Rule
                        Text(text = "Specify Kind of Person / Persona *", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(
                            text = "Select options that apply (e.g. Student & Family, or Family & Office).\nNote: Student + Office is not permitted.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            FilterChip(
                                selected = isOfficeSelected,
                                onClick = {
                                    isOfficeSelected = !isOfficeSelected
                                    signUpErrorMessage = null
                                },
                                label = { Text("🏢 Office", fontSize = 11.sp) },
                                modifier = Modifier.weight(1f)
                            )

                            FilterChip(
                                selected = isStudentSelected,
                                onClick = {
                                    isStudentSelected = !isStudentSelected
                                    signUpErrorMessage = null
                                },
                                label = { Text("🎓 Student", fontSize = 11.sp) },
                                modifier = Modifier.weight(1f)
                            )

                            FilterChip(
                                selected = isFamilySelected,
                                onClick = {
                                    isFamilySelected = !isFamilySelected
                                    signUpErrorMessage = null
                                },
                                label = { Text("👨‍👩‍👧‍👦 Family", fontSize = 11.sp) },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (personaValidationError != null && isAnyPersonaSelected) {
                            Text(
                                text = "⚠️ $personaValidationError",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (signUpErrorMessage != null) {
                            Text(
                                text = signUpErrorMessage!!,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = {
                                if (firstName.isBlank() || lastName.isBlank() || signUpUsername.isBlank() ||
                                    signUpPassword.isBlank() || phoneNumber.isBlank() || email.isBlank()
                                ) {
                                    signUpErrorMessage = "Please fill in all required fields marked with *."
                                    return@Button
                                }

                                if (personaValidationError != null) {
                                    signUpErrorMessage = personaValidationError
                                    return@Button
                                }

                                // Check if username already exists
                                if (registeredUsers.any { it.username.equals(signUpUsername.trim(), ignoreCase = true) }) {
                                    signUpErrorMessage = "Username '${signUpUsername.trim()}' is already taken. Choose another."
                                    return@Button
                                }

                                // Construct category string from selected options
                                val selectedPersonas = mutableListOf<String>()
                                if (isOfficeSelected) selectedPersonas.add("Offices")
                                if (isStudentSelected) selectedPersonas.add("Students")
                                if (isFamilySelected) selectedPersonas.add("Families")
                                val personaCategoryStr = selectedPersonas.joinToString(" & ")

                                isSubmitting = true

                                val newUser = User(
                                    id = UUID.randomUUID().toString(),
                                    firstName = firstName.trim(),
                                    lastName = lastName.trim(),
                                    username = signUpUsername.trim(),
                                    password = signUpPassword.trim(),
                                    phoneNumber = phoneNumber.trim(),
                                    email = email.trim(),
                                    city = selectedCity,
                                    deliveryLocationName = selectedLocationName,
                                    category = personaCategoryStr
                                )

                                // Real-time Firestore write
                                val db = FirebaseFirestore.getInstance()
                                val userMap = hashMapOf<String, Any>(
                                    "id" to newUser.id,
                                    "firstName" to newUser.firstName,
                                    "lastName" to newUser.lastName,
                                    "username" to newUser.username,
                                    "password" to newUser.password,
                                    "phoneNumber" to newUser.phoneNumber,
                                    "email" to newUser.email,
                                    "city" to newUser.city,
                                    "deliveryLocationName" to newUser.deliveryLocationName,
                                    "category" to newUser.category,
                                    "profileImageUrl" to newUser.profileImageUrl
                                )

                                db.collection("users").document(newUser.id).set(userMap)
                                    .addOnSuccessListener {
                                        isSubmitting = false
                                        onLoginSuccess(newUser)
                                    }
                                    .addOnFailureListener { e ->
                                        isSubmitting = false
                                        // Fallback local memory login if Firestore fails
                                        onLoginSuccess(newUser)
                                    }
                            },
                            enabled = !isSubmitting && personaValidationError == null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            if (isSubmitting) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                            } else {
                                Text(text = "Register & Create Account →", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Switch to Sign In
                TextButton(
                    onClick = {
                        isSignUpMode = false
                        signUpErrorMessage = null
                    }
                ) {
                    Text("Already have an account? Sign In Here", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
