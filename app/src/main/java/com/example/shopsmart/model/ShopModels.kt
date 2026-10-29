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
    val imageUrl: String = "",
    val availableCities: List<String> = listOf("Lilongwe", "Blantyre", "Zomba"),
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
    val selectedWeight: String,
    val imageUrl: String = ""
)

data class CartItem(
    val id: String = UUID.randomUUID().toString(),
    val itemId: String = "",
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
    val deliveryFee: Double
)

data class CompletedOrder(
    val orderId: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val deliveryLocation: DeliveryLocation,
    val items: List<CartItem>,
    val itemsSubtotal: Double = 0.0,
    val grandTotal: Double = 0.0,
    val paymentMethod: String = "PayChangu (Airtel Money)",
    val airtelNumber: String = "",
    val payChanguTxRef: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isReceived: Boolean = false,
    val receivedAt: Long? = null,
    val status: String = "Not Delivered"
)

data class User(
    val id: String = UUID.randomUUID().toString(),
    val firstName: String,
    val lastName: String,
    val username: String,
    val password: String,
    val phoneNumber: String,
    val email: String,
    val city: String,
    val deliveryLocationName: String = "Biwi",
    val category: String = "Students",
    val profileImageUrl: String = ""
)

fun calculateDeliveryFee(distanceKm: Double): Double {
    return (distanceKm / 6.0) * 0.20
}

val CITIES_DELIVERY_LOCATIONS = mapOf(
    "Lilongwe" to listOf(
        DeliveryLocation("ll_biwi", "Biwi", 3.0, calculateDeliveryFee(3.0)),
        DeliveryLocation("ll_chipasula", "Chipasula", 6.0, calculateDeliveryFee(6.0)),
        DeliveryLocation("ll_likuni", "Likuni", 12.0, calculateDeliveryFee(12.0)),
        DeliveryLocation("ll_area25", "Area 25", 15.0, calculateDeliveryFee(15.0))
    ),
    "Blantyre" to listOf(
        DeliveryLocation("bt_vic", "Victoria Pharmacy", 4.0, calculateDeliveryFee(4.0)),
        DeliveryLocation("bt_mubas", "MUBAS", 8.0, calculateDeliveryFee(8.0)),
        DeliveryLocation("bt_ndirande", "Ndirande", 11.0, calculateDeliveryFee(11.0))
    ),
    "Zomba" to listOf(
        DeliveryLocation("zb_unima", "University of Malawi (UNIMA Campus - Base Hub)", 0.0, calculateDeliveryFee(0.0)),
        DeliveryLocation("zb_town", "Zomba Town", 2.0, calculateDeliveryFee(2.0)),
        DeliveryLocation("zb_chinamwali", "Chinamwali", 4.0, calculateDeliveryFee(4.0)),
        DeliveryLocation("zb_matawale", "Matawale", 6.5, calculateDeliveryFee(6.5))
    )
)

val DELIVERY_LOCATIONS = listOf(
    DeliveryLocation("loc_1", "Mponda Primary School", 3.0, calculateDeliveryFee(3.0)),
    DeliveryLocation("loc_2", "UNIMA Campus", 6.0, calculateDeliveryFee(6.0)),
    DeliveryLocation("loc_3", "Zomba Matawale", 9.0, calculateDeliveryFee(9.0))
)

val PROMOTIONS_LIST = listOf(
    PromotionItem(
        id = "promo_1",
        name = "Illovo Sugar Super Pack (10kg)",
        category = "Pantry Staples",
        description = "Special discount on Illovo Sugar for home and office tea time!",
        initialPrice = 115.0,
        newPrice = 90.0,
        selectedProcessor = "Illovo",
        selectedWeight = "5kg"
    ),
    PromotionItem(
        id = "promo_2",
        name = "Kukoma Cooking Oil (5 Litre)",
        category = "Cooking Essentials",
        description = "Pure vegetable cooking oil mega saver deal.",
        initialPrice = 110.0,
        newPrice = 85.0,
        selectedProcessor = "Kukoma",
        selectedWeight = "5litre"
    ),
    PromotionItem(
        id = "promo_3",
        name = "Kilombero Premium Rice (10kg)",
        category = "Pantry Staples",
        description = "Long grain aromatic Kilombero rice at reduced promotional price.",
        initialPrice = 105.0,
        newPrice = 80.0,
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
                basePrice = 85.0,
                description = "High quality aromatic rice for households & offices.",
                priceTable = mapOf(
                    Pair("Kilombero", "10kg") to 85.0,
                    Pair("Kilombero", "50kg") to 120.0,
                    Pair("Pishori", "10kg") to 95.0,
                    Pair("Pishori", "50kg") to 120.0
                )
            ),
            ShopItem(
                id = "flour",
                name = "Maize Flour",
                categoryId = 1,
                processors = listOf("A1", "Rab Processors"),
                weightsOrSizes = listOf("10kg", "50kg"),
                basePrice = 50.0,
                description = "Fine ground flour for baking and staple meal prep.",
                priceTable = mapOf(
                    Pair("A1", "10kg") to 50.0,
                    Pair("A1", "50kg") to 90.0,
                    Pair("Rab Processors", "10kg") to 48.0,
                    Pair("Rab Processors", "50kg") to 88.0
                )
            ),
            ShopItem(
                id = "sugar",
                name = "Refined Illovo Sugar",
                categoryId = 1,
                processors = listOf("Illovo"),
                weightsOrSizes = listOf("1kg", "2kg", "5kg"),
                basePrice = 45.0,
                description = "Pure sweet refined sugar.",
                priceTable = mapOf(
                    Pair("Illovo", "1kg") to 45.0,
                    Pair("Illovo", "2kg") to 75.0,
                    Pair("Illovo", "5kg") to 110.0
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
                basePrice = 55.0,
                description = "Double refined clear vegetable cooking oil.",
                priceTable = mapOf(
                    Pair("Kukoma", "1litre") to 55.0,
                    Pair("Kukoma", "2litre") to 85.0,
                    Pair("Kukoma", "5litre") to 115.0,
                    Pair("Soyalite", "1litre") to 50.0,
                    Pair("Soyalite", "2litre") to 80.0,
                    Pair("Soyalite", "5litre") to 110.0
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
                processors = listOf("Charcoal", "Colgate"),
                weightsOrSizes = listOf("100ml", "200ml"),
                basePrice = 45.0,
                description = "Maximum cavity protection toothpaste.",
                priceTable = mapOf(
                    Pair("Charcoal", "100ml") to 50.0,
                    Pair("Charcoal", "200ml") to 80.0,
                    Pair("Colgate", "100ml") to 45.0,
                    Pair("Colgate", "200ml") to 75.0
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
                basePrice = 50.0,
                description = "Ready-to-cook savory sausage packs.",
                priceTable = mapOf(
                    Pair("Beef Sausages", "400g") to 50.0,
                    Pair("Beef Sausages", "700g") to 80.0
                )
            ),
            ShopItem(
                id = "chicken_parts",
                name = "Fresh Chicken Cuts",
                categoryId = 4,
                processors = listOf("Full Chicken", "Liver", "Wings"),
                weightsOrSizes = listOf("1kg", "2kg"),
                basePrice = 65.0,
                description = "Hygienically dressed chicken portions.",
                priceTable = mapOf(
                    Pair("Full Chicken", "1kg") to 65.0,
                    Pair("Full Chicken", "2kg") to 110.0,
                    Pair("Liver", "1kg") to 45.0,
                    Pair("Liver", "2kg") to 75.0,
                    Pair("Wings", "1kg") to 55.0,
                    Pair("Wings", "2kg") to 95.0
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
            // Offices (5 members): prioritize sugar, rice pishori, cooking oil 5 litres, beef sausage
            val officeSugar = CartItem(
                itemTitle = "Refined White Sugar (Illovo 5kg)",
                selectedProcessor = "Illovo",
                selectedWeight = "5kg",
                quantity = groupCount.coerceAtLeast(1),
                unitPrice = 9500.0
            )
            val officePishoriRice = CartItem(
                itemTitle = "Pishori Rice (10kg)",
                selectedProcessor = "Pishori",
                selectedWeight = "10kg",
                quantity = groupCount.coerceAtLeast(1),
                unitPrice = 19500.0
            )
            val officeCookingOil = CartItem(
                itemTitle = "Pure Cooking Oil (Kukoma 5L)",
                selectedProcessor = "Kukoma",
                selectedWeight = "5litre",
                quantity = 1,
                unitPrice = 15500.0
            )
            val officeBeefSausage = CartItem(
                itemTitle = "Fresh Beef Sausages (700g)",
                selectedProcessor = "Beef Sausages",
                selectedWeight = "700g",
                quantity = groupCount.coerceAtLeast(1),
                unitPrice = 65.0
            )

            for (item in listOf(officeSugar, officePishoriRice, officeCookingOil, officeBeefSausage)) {
                if (item.totalPrice <= remainingBudget) {
                    recommendedList.add(item)
                    remainingBudget -= item.totalPrice
                }
            }
        }
        UserPersona.STUDENTS -> {
            // Students (1 student): rice 10kg, flour 10kg, sugar, cooking oil 1litre, liver
            val studentRice = CartItem(
                itemTitle = "Kilombero Rice (10kg)",
                selectedProcessor = "Kilombero",
                selectedWeight = "10kg",
                quantity = 1,
                unitPrice = 16500.0
            )
            val studentFlour = CartItem(
                itemTitle = "Maize / Wheat Flour (A1 10kg)",
                selectedProcessor = "A1",
                selectedWeight = "10kg",
                quantity = 1,
                unitPrice = 8200.0
            )
            val studentSugar = CartItem(
                itemTitle = "Refined White Sugar (1kg)",
                selectedProcessor = "Illovo",
                selectedWeight = "1kg",
                quantity = 1,
                unitPrice = 2000.0
            )
            val studentCookingOil = CartItem(
                itemTitle = "Pure Cooking Oil (Kukoma 1L)",
                selectedProcessor = "Kukoma",
                selectedWeight = "1litre",
                quantity = 1,
                unitPrice = 3200.0
            )
            val studentLiver = CartItem(
                itemTitle = "Chicken Liver (1kg)",
                selectedProcessor = "Liver",
                selectedWeight = "1kg",
                quantity = 1,
                unitPrice = 55.0
            )

            for (item in listOf(studentRice, studentFlour, studentSugar, studentCookingOil, studentLiver)) {
                if (item.totalPrice <= remainingBudget) {
                    recommendedList.add(item)
                    remainingBudget -= item.totalPrice
                }
            }
        }
        UserPersona.FAMILIES -> {
            // Families (3 members): same as offices plus rice, full chicken, colgate herbal, flour, 2 litre cooking oil, sugar, beef sausage
            val familyRice = CartItem(
                itemTitle = "Kilombero Rice (10kg)",
                selectedProcessor = "Kilombero",
                selectedWeight = "10kg",
                quantity = groupCount.coerceAtLeast(1),
                unitPrice = 16500.0
            )
            val familyFullChicken = CartItem(
                itemTitle = "Fresh Full Chicken (2kg)",
                selectedProcessor = "Full Chicken",
                selectedWeight = "2kg",
                quantity = 1,
                unitPrice = 80.0
            )
            val familyColgate = CartItem(
                itemTitle = "Colgate Herbal Toothpaste (100ml)",
                selectedProcessor = "Colgate",
                selectedWeight = "100ml",
                quantity = 1,
                unitPrice = 1400.0
            )
            val familyFlour = CartItem(
                itemTitle = "Maize / Wheat Flour (A1 10kg)",
                selectedProcessor = "A1",
                selectedWeight = "10kg",
                quantity = groupCount.coerceAtLeast(1),
                unitPrice = 8200.0
            )
            val familyCookingOil2L = CartItem(
                itemTitle = "Pure Cooking Oil (Kukoma 2L)",
                selectedProcessor = "Kukoma",
                selectedWeight = "2litre",
                quantity = 1,
                unitPrice = 6400.0
            )
            val familySugar2Kg = CartItem(
                itemTitle = "Refined White Sugar (2kg)",
                selectedProcessor = "Illovo",
                selectedWeight = "2kg",
                quantity = groupCount.coerceAtLeast(1),
                unitPrice = 3900.0
            )
            val familyBeefSausage = CartItem(
                itemTitle = "Fresh Beef Sausages (700g)",
                selectedProcessor = "Beef Sausages",
                selectedWeight = "700g",
                quantity = 1,
                unitPrice = 65.0
            )

            for (item in listOf(familyRice, familyFullChicken, familyColgate, familyFlour, familyCookingOil2L, familySugar2Kg, familyBeefSausage)) {
                if (item.totalPrice <= remainingBudget) {
                    recommendedList.add(item)
                    remainingBudget -= item.totalPrice
                }
            }
        }
    }

    return recommendedList
}
