package com.example.shopsmart.repository

import android.util.Log
import com.example.shopsmart.MainActivity
import com.example.shopsmart.model.CartItem
import com.example.shopsmart.model.CompletedOrder
import com.example.shopsmart.model.DeliveryLocation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

object OrderRepository {
    private const val TAG = "OrderRepository"
    private const val COLLECTION_ORDERS = "orders"
    private const val ONE_WEEK_MILLIS = 7 * 24 * 60 * 60 * 1000L // 7 days in milliseconds

    private val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    // In-memory cache for reliable offline fallback
    private val localOrders = mutableListOf<CompletedOrder>()

    /**
     * Save an order to Firebase Firestore and local cache
     */
    fun saveOrder(order: CompletedOrder, onComplete: (Boolean) -> Unit) {
        // Add to local cache first
        localOrders.removeAll { it.orderId == order.orderId }
        localOrders.add(0, order)

        val orderMap = hashMapOf<String, Any>(
            "orderId" to order.orderId,
            "firstName" to order.firstName,
            "lastName" to order.lastName,
            "phoneNumber" to order.phoneNumber,
            "deliveryLocationName" to order.deliveryLocation.name,
            "deliveryDistanceKm" to order.deliveryLocation.distanceKm,
            "deliveryFee" to order.deliveryLocation.deliveryFee,
            "itemsSubtotal" to order.itemsSubtotal,
            "grandTotal" to order.grandTotal,
            "paymentMethod" to order.paymentMethod,
            "airtelNumber" to order.airtelNumber,
            "payChanguTxRef" to order.payChanguTxRef,
            "timestamp" to order.timestamp,
            "isReceived" to order.isReceived,
            "receivedAt" to (order.receivedAt ?: 0L),
            "items" to order.items.map { item ->
                mapOf(
                    "id" to item.id,
                    "itemTitle" to item.itemTitle,
                    "selectedProcessor" to item.selectedProcessor,
                    "selectedWeight" to item.selectedWeight,
                    "quantity" to item.quantity,
                    "unitPrice" to item.unitPrice
                )
            }
        )

        try {
            firestore.collection(COLLECTION_ORDERS)
                .document(order.orderId)
                .set(orderMap)
                .addOnSuccessListener {
                    Log.d(TAG, "Order saved to Firestore successfully: ${order.orderId}")
                    onComplete(true)
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to save order to Firestore, kept in local state", e)
                    onComplete(true) // success in local memory
                }
        } catch (e: Exception) {
            Log.e(TAG, "Firestore exception, using local state", e)
            onComplete(true)
        }
    }

    /**
     * Mark an order as received (Tick) in Firebase Firestore and local cache
     */
    fun markOrderReceived(orderId: String, onComplete: (Boolean) -> Unit) {
        val now = System.currentTimeMillis()

        // Update local cache
        val index = localOrders.indexOfFirst { it.orderId == orderId }
        if (index != -1) {
            localOrders[index] = localOrders[index].copy(
                isReceived = true,
                receivedAt = now
            )
        }

        try {
            firestore.collection(COLLECTION_ORDERS)
                .document(orderId)
                .update(
                    mapOf(
                        "isReceived" to true,
                        "receivedAt" to now
                    )
                )
                .addOnSuccessListener {
                    Log.d(TAG, "Order marked as received in Firestore: $orderId")
                    onComplete(true)
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error marking order received in Firestore", e)
                    onComplete(true)
                }
        } catch (e: Exception) {
            Log.e(TAG, "Firestore exception on mark received", e)
            onComplete(true)
        }
    }

    /**
     * Automatically clean up orders older than 1 week (7 days)
     */
    fun cleanupOldOrders(onComplete: (Int) -> Unit) {
        val cutoffTime = System.currentTimeMillis() - ONE_WEEK_MILLIS

        // Clean local cache
        val initialCount = localOrders.size
        localOrders.removeAll { it.timestamp < cutoffTime }
        val localDeleted = initialCount - localOrders.size

        try {
            firestore.collection(COLLECTION_ORDERS)
                .whereLessThan("timestamp", cutoffTime)
                .get()
                .addOnSuccessListener { snapshot ->
                    var deletedCount = 0
                    val batch = firestore.batch()
                    for (doc in snapshot.documents) {
                        batch.delete(doc.reference)
                        deletedCount++
                    }
                    if (deletedCount > 0) {
                        batch.commit().addOnCompleteListener {
                            Log.d(TAG, "Cleaned up $deletedCount old orders (>1 week) from Firestore")
                            onComplete(deletedCount)
                        }
                    } else {
                        onComplete(localDeleted)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error cleaning up old orders from Firestore", e)
                    onComplete(localDeleted)
                }
        } catch (e: Exception) {
            Log.e(TAG, "Firestore exception during cleanup", e)
            onComplete(localDeleted)
        }
    }

    /**
     * Real-time listener Flow for orders
     */
    fun getOrdersFlow(): Flow<List<CompletedOrder>> = callbackFlow {
        // Trigger automatic 1-week cleanup first
        cleanupOldOrders { }

        try {
            val registration = firestore.collection(COLLECTION_ORDERS)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e(TAG, "Error listening to orders in Firestore", error)
                        trySend(localOrders.toList())
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        val cutoffTime = System.currentTimeMillis() - ONE_WEEK_MILLIS
                        val remoteOrders = snapshot.documents.mapNotNull { doc ->
                            try {
                                val timestamp = doc.getLong("timestamp") ?: 0L
                                // Skip orders older than 1 week
                                if (timestamp < cutoffTime) return@mapNotNull null

                                val orderId = doc.getString("orderId") ?: doc.id
                                val firstName = doc.getString("firstName") ?: ""
                                val lastName = doc.getString("lastName") ?: ""
                                val phoneNumber = doc.getString("phoneNumber") ?: ""
                                val locationName = doc.getString("deliveryLocationName") ?: "Delivery Point"
                                val distanceKm = doc.getDouble("deliveryDistanceKm") ?: 2.0
                                val deliveryFee = doc.getDouble("deliveryFee") ?: 2000.0
                                val itemsSubtotal = doc.getDouble("itemsSubtotal") ?: 0.0
                                val grandTotal = doc.getDouble("grandTotal") ?: 0.0
                                val paymentMethod = doc.getString("paymentMethod") ?: "PayChangu (Airtel Money)"
                                val airtelNumber = doc.getString("airtelNumber") ?: ""
                                val payChanguTxRef = doc.getString("payChanguTxRef") ?: ""
                                val isReceived = doc.getBoolean("isReceived") ?: false
                                val receivedAt = doc.getLong("receivedAt")

                                @Suppress("UNCHECKED_CAST")
                                val rawItems = doc.get("items") as? List<Map<String, Any>> ?: emptyList()
                                val cartItems = rawItems.map { itemMap ->
                                    CartItem(
                                        id = itemMap["id"] as? String ?: "",
                                        itemTitle = itemMap["itemTitle"] as? String ?: "Item",
                                        selectedProcessor = itemMap["selectedProcessor"] as? String ?: "",
                                        selectedWeight = itemMap["selectedWeight"] as? String ?: "",
                                        quantity = (itemMap["quantity"] as? Long)?.toInt() ?: 1,
                                        unitPrice = (itemMap["unitPrice"] as? Double) ?: 0.0
                                    )
                                }

                                CompletedOrder(
                                    orderId = orderId,
                                    firstName = firstName,
                                    lastName = lastName,
                                    phoneNumber = phoneNumber,
                                    deliveryLocation = DeliveryLocation(
                                        id = "loc_fire",
                                        name = locationName,
                                        distanceKm = distanceKm,
                                        deliveryFee = deliveryFee
                                    ),
                                    items = cartItems,
                                    itemsSubtotal = itemsSubtotal,
                                    grandTotal = grandTotal,
                                    paymentMethod = paymentMethod,
                                    airtelNumber = airtelNumber,
                                    payChanguTxRef = payChanguTxRef,
                                    timestamp = timestamp,
                                    isReceived = isReceived,
                                    receivedAt = receivedAt
                                )
                            } catch (e: Exception) {
                                Log.e(TAG, "Error parsing order document ${doc.id}", e)
                                null
                            }
                        }

                        // Merge remote and local orders to ensure no data is lost
                        val combined = (remoteOrders + localOrders).distinctBy { it.orderId }
                            .sortedByDescending { it.timestamp }

                        trySend(combined)
                    } else {
                        trySend(localOrders.toList())
                    }
                }

            awaitClose { registration.remove() }
        } catch (e: Exception) {
            Log.e(TAG, "Exception setting up Firestore listener", e)
            trySend(localOrders.toList())
            awaitClose { }
        }
    }

    /**
     * Get current list of orders (Local or cached)
     */
    fun getLocalOrders(): List<CompletedOrder> {
        val cutoffTime = System.currentTimeMillis() - ONE_WEEK_MILLIS
        return localOrders.filter { it.timestamp >= cutoffTime }.sortedByDescending { it.timestamp }
    }
}
