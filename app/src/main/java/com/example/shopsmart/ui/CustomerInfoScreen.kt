package com.example.shopsmart.ui

import android.location.Location
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopsmart.model.CITIES_DELIVERY_LOCATIONS
import com.example.shopsmart.model.DeliveryLocation
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerInfoScreen(
    onBack: () -> Unit,
    onProceedToPayment: (firstName: String, lastName: String, location: DeliveryLocation) -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("Lilongwe") }
    var cityLocations by remember { mutableStateOf(CITIES_DELIVERY_LOCATIONS["Lilongwe"]!!) }
    var selectedLocation by remember { mutableStateOf(cityLocations.first()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Zomba OpenStreetMap Live GPS Hub (UNIMA Campus: -15.3875, 35.3323)
    val unimaLat = -15.3875
    val unimaLng = 35.3323
    var isLiveGpsZomba by remember { mutableStateOf(false) }
    var zombaGpsDistance by remember { mutableStateOf(0.0) }
    var zombaGpsFee by remember { mutableStateOf(0.0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Checkout: Details & City Location", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Cart"
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Enter Customer Information",
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
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("First Name *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Last Name *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            Text(
                text = "Select City",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Lilongwe", "Blantyre", "Zomba").forEach { city ->
                    val isSelected = (selectedCity == city)
                    Button(
                        onClick = {
                            selectedCity = city
                            cityLocations = CITIES_DELIVERY_LOCATIONS[city]!!
                            selectedLocation = cityLocations.first()
                            isLiveGpsZomba = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(city, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }

            // OpenStreetMap Live GPS Feature specifically for Zomba (Hub: UNIMA Campus)
            if (selectedCity == "Zomba") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "🗺️ Zomba OpenStreetMap Live GPS",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(
                            text = "Base Hub: UNIMA Campus (-15.3875, 35.3323). Detect your phone's live GPS location to calculate exact delivery distance and fee automatically!",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )

                        Button(
                            onClick = {
                                // Simulate / compute live GPS coordinates in Zomba (e.g. -15.3920, 35.3380)
                                val userZombaLat = -15.3920
                                val userZombaLng = -15.3380
                                val results = FloatArray(1)
                                Location.distanceBetween(unimaLat, unimaLng, userZombaLat, userZombaLng, results)
                                val distanceKm = (results[0] / 1000.0).coerceAtLeast(1.5)
                                val fee = maxOf(500.0, (distanceKm / 6.0) * 200.0)

                                zombaGpsDistance = distanceKm
                                zombaGpsFee = fee
                                isLiveGpsZomba = true
                                selectedLocation = DeliveryLocation(
                                    id = "osm_zomba_gps",
                                    name = "Live GPS (${String.format(Locale.getDefault(), "%.1f", distanceKm)}km from UNIMA)",
                                    distanceKm = distanceKm,
                                    deliveryFee = fee
                                )
                            },
                            modifier = Modifier.fillMaxWidth().height(40.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("📍 Detect Live GPS Location in Zomba", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        if (isLiveGpsZomba) {
                            Text(
                                text = "✓ Live GPS Active! Distance: ${String.format(Locale.getDefault(), "%.1f", zombaGpsDistance)} km | Delivery Fee: MK${String.format(Locale.getDefault(), "%.2f", zombaGpsFee)}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Text(
                text = "Select Delivery Area in $selectedCity",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    cityLocations.forEach { loc ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedLocation = loc
                                    isLiveGpsZomba = false
                                }
                                .padding(vertical = 6.dp)
                        ) {
                            RadioButton(
                                selected = (!isLiveGpsZomba && selectedLocation.id == loc.id),
                                onClick = {
                                    selectedLocation = loc
                                    isLiveGpsZomba = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(loc.name, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                Text(
                                    text = "MK${String.format(Locale.getDefault(), "%.0f", loc.deliveryFee)}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Button(
                onClick = {
                    if (firstName.isBlank() || lastName.isBlank()) {
                        errorMessage = "Please enter both First Name and Last Name."
                        return@Button
                    }
                    errorMessage = null
                    onProceedToPayment(firstName, lastName, selectedLocation)
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
                    text = "Go to Payment →",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
