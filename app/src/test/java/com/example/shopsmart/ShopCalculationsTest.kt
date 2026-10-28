package com.example.shopsmart

import com.example.shopsmart.model.CartItem
import com.example.shopsmart.model.DELIVERY_LOCATIONS
import com.example.shopsmart.model.ShopItem
import com.example.shopsmart.model.UserPersona
import com.example.shopsmart.model.generateSmartBudgetPlan
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ShopCalculationsTest {

    @Test
    fun testCartItemTotalPrice() {
        val cartItem = CartItem(
            itemTitle = "Fresh Beef Sausages",
            selectedProcessor = "Beef Sausages",
            selectedWeight = "400g",
            quantity = 3,
            unitPrice = 50.0
        )
        assertEquals(150.0, cartItem.totalPrice, 0.001)
    }

    @Test
    fun testShopItemPriceLookup() {
        val shopItem = ShopItem(
            id = "sausage",
            name = "Fresh Beef Sausages",
            categoryId = 4,
            processors = listOf("Beef Sausages"),
            weightsOrSizes = listOf("400g", "700g"),
            basePrice = 50.0,
            priceTable = mapOf(
                Pair("Beef Sausages", "400g") to 50.0,
                Pair("Beef Sausages", "700g") to 65.0
            )
        )
        assertEquals(50.0, shopItem.getPrice("Beef Sausages", "400g"), 0.001)
        assertEquals(65.0, shopItem.getPrice("Beef Sausages", "700g"), 0.001)
    }

    @Test
    fun testDeliveryLocations() {
        assertEquals(3, DELIVERY_LOCATIONS.size)
        val firstLocation = DELIVERY_LOCATIONS.first()
        assertEquals(0.10, firstLocation.deliveryFee, 0.001)
        assertEquals(3.0, firstLocation.distanceKm, 0.001)
    }

    @Test
    fun testSmartAiBudgetPlan() {
        val budgetPlan = generateSmartBudgetPlan(
            persona = UserPersona.STUDENTS,
            groupCount = 1,
            budgetAmount = 30000.0
        )
        assertTrue(budgetPlan.isNotEmpty())
        val totalPlanCost = budgetPlan.sumOf { it.totalPrice }
        assertTrue(totalPlanCost <= 30000.0)
    }

    @Test
    fun testUsernameGeneration() {
        val firstName = "Bridget"
        val lastName = "Sanudi"
        val firstInitial = firstName.trim().firstOrNull()?.uppercase() ?: ""
        val surname = lastName.trim().replace(" ", "")
        val generatedUsername = "$firstInitial$surname"
        assertEquals("BSanudi", generatedUsername)
    }

    @Test
    fun testDeliveryFeePerKilometer() {
        val distanceKm = 6.0
        val deliveryFee = (distanceKm / 6.0) * 0.20
        assertEquals(0.20, deliveryFee, 0.001)
    }
}
