package com.example.shopsmart.model

import java.util.UUID

enum class UserPersona(val title: String, val description: String, val icon: String) {
    OFFICES("Offices", "Bulk pantry, tea/coffee, cleaning & stationery supplies", "🏢"),
    STUDENTS("Students", "Budget-friendly, quick meals & essential toiletries", "🎓"),
    FAMILIES("Families", "Full monthly groceries, fresh items & household care", "👨‍👩‍👧‍👦")
}

data class Category(
    val id: Int,
    val name: String,
    val items: List<ShopItem>
)

data class ShopItem(
    val id: String,
    val name: String,
    val categoryId: Int,
    val processors: List<String>,
    val weightsOrSizes: List<String>,
    val basePrice: Double,
    val description: String = "",
    val priceTable: Map<Pair<String, String>, Double> = emptyMap()
) {
    fun getPrice(processor: String, weight: String): Double {
        return priceTable[Pair(processor, weight)] 
            ?: priceTable[Pair(processors.firstOrNull() ?: "", weight)] 
            ?: basePrice
    }
}

data class PromotionItem(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val initialPrice: Double,
    val newPrice: Double,
    val selectedProcessor: String,
    val selectedWeight: String
)

data class CartItem(
    val id: String = UUID.randomUUID().toString(),
<<<<<<< HEAD
    val itemId: String = "",
=======
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
    val itemTitle: String,
    val selectedProcessor: String,
    val selectedWeight: String,
    val quantity: Int,
    val unitPrice: Double
) {
    val totalPrice: Double get() = unitPrice * quantity
}

data class DeliveryLocation(
    val id: String,
    val name: String,
    val distanceKm: Double,
<<<<<<< HEAD
    val deliveryFee: Double
)

val DELIVERY_LOCATIONS = listOf(
    DeliveryLocation("loc_1", "Mponda Primary School", 2.0, 2000.0),
    DeliveryLocation("loc_2", "UNIMA Campus", 6.0, 6000.0),
    DeliveryLocation("loc_3", "Zomba Matawale", 4.0, 4000.0)
=======
    val deliveryFee: Double,
    val isCustom: Boolean = false
)

data class CompletedOrder(
    val orderId: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val deliveryLocation: DeliveryLocation,
    val items: List<CartItem>,
    val itemsSubtotal: Double = items.sumOf { it.totalPrice },
    val deliveryFee: Double = deliveryLocation.deliveryFee,
    val grandTotal: Double = itemsSubtotal + deliveryFee,
    val paymentMethod: String = "PayChangu (Airtel Money)",
    val airtelNumber: String = "",
    val payChanguTxRef: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isReceived: Boolean = false,
    val receivedAt: Long? = null
)

val DELIVERY_LOCATIONS = listOf(
    DeliveryLocation("loc_1", "Mponda Primary School (Physical Place)", 2.0, 2000.0),
    DeliveryLocation("loc_2", "UNIMA Campus - Main Gate (Physical Place)", 6.0, 6000.0),
    DeliveryLocation("loc_3", "Zomba Matawale Market (Physical Place)", 4.0, 4000.0),
    DeliveryLocation("loc_4", "City Center Bus Depot (Physical Place)", 3.0, 3000.0),
    DeliveryLocation("loc_5", "Main Post Office / Market Square (Physical Place)", 2.5, 2500.0),
    DeliveryLocation("loc_custom", "Deliver to My Location / Address", 5.0, 5000.0, isCustom = true)
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
)

val PROMOTIONS_LIST = listOf(
    PromotionItem(
        id = "promo_1",
        name = "Illovo Sugar Super Pack (10kg)",
        category = "Pantry Staples",
        description = "Special discount on Illovo Sugar for home and office tea time!",
        initialPrice = 19500.0,
        newPrice = 17500.0,
        selectedProcessor = "Illovo",
        selectedWeight = "5kg"
    ),
    PromotionItem(
        id = "promo_2",
        name = "Kukoma Cooking Oil (5 Litre)",
        category = "Cooking Essentials",
        description = "Pure vegetable cooking oil mega saver deal.",
        initialPrice = 16500.0,
        newPrice = 14200.0,
        selectedProcessor = "Kukoma",
        selectedWeight = "5litre"
    ),
    PromotionItem(
        id = "promo_3",
        name = "Kilombero Premium Rice (10kg)",
        category = "Pantry Staples",
        description = "Long grain aromatic Kilombero rice at reduced promotional price.",
        initialPrice = 18000.0,
        newPrice = 15500.0,
        selectedProcessor = "Kilombero",
        selectedWeight = "10kg"
    )
)

val SHOP_CATEGORIES = listOf(
    Category(
        id = 1,
        name = "Pantry Staples & Dry Goods",
        items = listOf(
            ShopItem(
                id = "rice",
                name = "Rice",
                categoryId = 1,
                processors = listOf("Kilombero", "Pishori"),
                weightsOrSizes = listOf("10kg", "50kg"),
                basePrice = 16500.0,
                description = "High quality aromatic rice for households & offices.",
                priceTable = mapOf(
                    Pair("Kilombero", "10kg") to 16500.0,
                    Pair("Kilombero", "50kg") to 75000.0,
                    Pair("Pishori", "10kg") to 19500.0,
                    Pair("Pishori", "50kg") to 88000.0
                )
            ),
            ShopItem(
                id = "flour",
                name = "Maize Flour",
                categoryId = 1,
                processors = listOf("A1", "Rab Processors"),
                weightsOrSizes = listOf("10kg", "50kg"),
                basePrice = 8200.0,
                description = "Fine ground flour for baking and staple meal prep.",
                priceTable = mapOf(
                    Pair("A1", "10kg") to 8200.0,
                    Pair("A1", "50kg") to 38000.0,
                    Pair("Rab Processors", "10kg") to 7800.0,
                    Pair("Rab Processors", "50kg") to 36000.0
                )
            ),
            ShopItem(
                id = "sugar",
                name = "Refined illovo Sugar",
                categoryId = 1,
                processors = listOf("Brown"),
                weightsOrSizes = listOf("1kg", "2kg", "5kg"),
                basePrice = 1950.0,
                description = "Pure sweet refined sugar.",
                priceTable = mapOf(
                    Pair("Illovo", "1kg") to 2000.0,
                    Pair("Illovo", "2kg") to 3900.0,
                    Pair("Illovo", "5kg") to 9500.0
                )
            )
        )
    ),
    Category(
        id = 2,
        name = "Cooking Essentials",
        items = listOf(
            ShopItem(
                id = "cooking_oil",
                name = "Pure Cooking Oil",
                categoryId = 2,
                processors = listOf("Kukoma", "Soyalite"),
                weightsOrSizes = listOf("1litre", "2litre", "5litre"),
                basePrice = 3200.0,
                description = "Double refined clear vegetable cooking oil.",
                priceTable = mapOf(
                    Pair("Kukoma", "1litre") to 3200.0,
                    Pair("Kukoma", "2litre") to 6400.0,
                    Pair("Kukoma", "5litre") to 15500.0,
                    Pair("Soyalite", "1litre") to 3000.0,
                    Pair("Soyalite", "2litre") to 6000.0,
                    Pair("Soyalite", "5litre") to 14800.0
                )
            )
        )
    ),
    Category(
        id = 3,
        name = "Personal Care & Hygiene",
        items = listOf(
            ShopItem(
                id = "toothpaste",
                name = "Colgate Herbal Toothpaste",
                categoryId = 3,
                processors = listOf("Charcoal", "herbal"),
                weightsOrSizes = listOf("100ml", "200ml"),
                basePrice = 1400.0,
                description = "Maximum cavity protection toothpaste.",
                priceTable = mapOf(
                    Pair("Charcoal", "100ml") to 1500.0,
                    Pair("Charcoal", "200ml") to 2800.0,
                    Pair("Colgate", "100ml") to 1400.0,
                    Pair("Colgate", "200ml") to 2600.0
                )
            )
        )
    ),
    Category(
        id = 4,
        name = "Chilled & Perishable Foods",
        items = listOf(
            ShopItem(
                id = "sausage",
                name = "Fresh Beef Sausages",
                categoryId = 4,
                processors = listOf("Beef Sausages"),
                weightsOrSizes = listOf("400g", "700g"),
<<<<<<< HEAD
                basePrice = 50.0,
                description = "Ready-to-cook savory sausage packs.",
                priceTable = mapOf(
                    Pair("Beef Sausages", "400g") to 50.0,
                    Pair("Beef Sausages", "700g") to 65.0
=======
                basePrice = 3200.0,
                description = "Ready-to-cook savory sausage packs.",
                priceTable = mapOf(
                    Pair("Beef Sausages", "400g") to 3200.0,
                    Pair("Beef Sausages", "700g") to 5500.0
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                )
            ),
            ShopItem(
                id = "chicken_parts",
                name = "Fresh Chicken Cuts",
                categoryId = 4,
                processors = listOf("Full Chicken", "Liver", "Wings"),
                weightsOrSizes = listOf("1kg", "2kg"),
<<<<<<< HEAD
                basePrice = 60.0,
                description = "Hygienically dressed chicken portions.",
                priceTable = mapOf(
                    Pair("Full Chicken", "1kg") to 60.0,
                    Pair("Full Chicken", "2kg") to 80.0,
                    Pair("Liver", "1kg") to 55.0,
                    Pair("Liver", "2kg") to 75.0,
                    Pair("Wings", "1kg") to 58.0,
                    Pair("Wings", "2kg") to 78.0
=======
                basePrice = 3600.0,
                description = "Hygienically dressed chicken portions.",
                priceTable = mapOf(
                    Pair("Full Chicken", "1kg") to 3600.0,
                    Pair("Full Chicken", "2kg") to 7200.0,
                    Pair("Liver", "1kg") to 3000.0,
                    Pair("Liver", "2kg") to 5900.0,
                    Pair("Wings", "1kg") to 4000.0,
                    Pair("Wings", "2kg") to 7900.0
>>>>>>> 3a76190b84a8a913a5dcfb0e21e88d8287dd35cb
                )
            )
        )
    )
)

// Smart AI Recommendation Logic
fun generateSmartBudgetPlan(
    persona: UserPersona,
    groupCount: Int = 1,
    budgetAmount: Double
): List<CartItem> {
    if (budgetAmount <= 0) return emptyList()

    val recommendedList = mutableListOf<CartItem>()
    var remainingBudget = budgetAmount

    when (persona) {
        UserPersona.OFFICES -> {
            val officeSugar = CartItem(
                itemTitle = "Refined White Sugar (Illovo 5kg)",
                selectedProcessor = "Illovo",
                selectedWeight = "5kg",
                quantity = 1,
                unitPrice = 9500.0
            )
            val officeRice = CartItem(
                itemTitle = "Kilombero Rice (10kg)",
                selectedProcessor = "Kilombero",
                selectedWeight = "10kg",
                quantity = 1,
                unitPrice = 16500.0
            )

            for (item in listOf(officeSugar, officeRice)) {
                if (item.totalPrice <= remainingBudget) {
                    recommendedList.add(item)
                    remainingBudget -= item.totalPrice
                }
            }
        }
        UserPersona.STUDENTS -> {
            val studentFlour = CartItem(
                itemTitle = "Maize / Wheat Flour (10kg)",
                selectedProcessor = "A1",
                selectedWeight = "10kg",
                quantity = 1,
                unitPrice = 8200.0
            )
            val studentRice = CartItem(
                itemTitle = "Kilombero Rice (10kg)",
                selectedProcessor = "Kilombero",
                selectedWeight = "10kg",
                quantity = 1,
                unitPrice = 16500.0
            )
            val studentSugar = CartItem(
                itemTitle = "Refined White Sugar (1kg)",
                selectedProcessor = "Illovo",
                selectedWeight = "1kg",
                quantity = 1,
                unitPrice = 2000.0
            )
            val studentOil = CartItem(
                itemTitle = "Pure Cooking Oil (Kukoma 1L)",
                selectedProcessor = "Kukoma",
                selectedWeight = "1litre",
                quantity = 1,
                unitPrice = 3200.0
            )

            for (item in listOf(studentFlour, studentRice, studentSugar, studentOil)) {
                if (item.totalPrice <= remainingBudget) {
                    recommendedList.add(item)
                    remainingBudget -= item.totalPrice
                }
            }
        }
        UserPersona.FAMILIES -> {
            val familyFlour = CartItem(
                itemTitle = "Maize / Wheat Flour (10kg)",
                selectedProcessor = "A1",
                selectedWeight = "10kg",
                quantity = 1,
                unitPrice = 8200.0
            )
            val familyRice = CartItem(
                itemTitle = "Kilombero Rice (10kg)",
                selectedProcessor = "Kilombero",
                selectedWeight = "10kg",
                quantity = 1,
                unitPrice = 16500.0
            )
            val familySugar = CartItem(
                itemTitle = "Refined White Sugar (2kg)",
                selectedProcessor = "Illovo",
                selectedWeight = "2kg",
                quantity = 1,
                unitPrice = 3900.0
            )
            val familyOil = CartItem(
                itemTitle = "Pure Cooking Oil (Kukoma 2L)",
                selectedProcessor = "Kukoma",
                selectedWeight = "2litre",
                quantity = 1,
                unitPrice = 6400.0
            )

            for (item in listOf(familyFlour, familyRice, familySugar, familyOil)) {
                if (item.totalPrice <= remainingBudget) {
                    recommendedList.add(item)
                    remainingBudget -= item.totalPrice
                }
            }
        }
    }

    return recommendedList
}
