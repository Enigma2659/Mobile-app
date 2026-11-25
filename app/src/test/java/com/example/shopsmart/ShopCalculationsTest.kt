package com.example.shopsmart

import com.example.shopsmart.model.CartItem
import com.example.shopsmart.model.CITIES_DELIVERY_LOCATIONS
import com.example.shopsmart.model.ShopItem
import com.example.shopsmart.model.UserPersona
import com.example.shopsmart.model.calculateDeliveryFee
import com.example.shopsmart.model.generateSmartBudgetPlan
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
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
            unitPrice = 3500.0
        )
        assertEquals(10500.0, cartItem.totalPrice, 0.001)
    }

    @Test
    fun testShopItemPriceLookup() {
        val shopItem = ShopItem(
            id = "sausage",
            name = "Fresh Beef Sausages",
            categoryId = 4,
            processors = listOf("Beef Sausages"),
            weightsOrSizes = listOf("400g", "700g"),
            basePrice = 3500.0,
            priceTable = mapOf(
                Pair("Beef Sausages", "400g") to 3500.0,
                Pair("Beef Sausages", "700g") to 5200.0
            )
        )
        assertEquals(3500.0, shopItem.getPrice("Beef Sausages", "400g"), 0.001)
        assertEquals(5200.0, shopItem.getPrice("Beef Sausages", "700g"), 0.001)
    }

    @Test
    fun testDeliveryFeeFormula() {
        // MK200 for every 6 kilometers: (distanceKm / 6.0) * 200.0
        assertEquals(200.0, calculateDeliveryFee(6.0), 0.001)
        assertEquals(400.0, calculateDeliveryFee(12.0), 0.001)
        assertEquals(100.0, calculateDeliveryFee(3.0), 0.001)
        assertEquals(0.0, calculateDeliveryFee(0.0), 0.001)
    }

    @Test
    fun testZombaLocationsAndHub() {
        val zombaLocations = CITIES_DELIVERY_LOCATIONS["Zomba"]
        assertNotNull(zombaLocations)
        assertTrue(zombaLocations!!.isNotEmpty())

        val unimaHub = zombaLocations.find { it.name.contains("UNIMA Campus", ignoreCase = true) }
        assertNotNull(unimaHub)
        assertEquals(0.0, unimaHub!!.distanceKm, 0.001)
        assertEquals(0.0, unimaHub.deliveryFee, 0.001)

        val chinamwali = zombaLocations.find { it.name.contains("Chinamwali", ignoreCase = true) }
        assertNotNull(chinamwali)
        assertEquals(4.0, chinamwali!!.distanceKm, 0.001)
    }

    @Test
    fun testSmartAiBudgetPlanAllPersonas() {
        UserPersona.entries.forEach { persona ->
            val budgetPlan = generateSmartBudgetPlan(
                persona = persona,
                groupCount = 2,
                budgetAmount = 50000.0
            )
            assertNotNull(budgetPlan)
            val totalPlanCost = budgetPlan.sumOf { it.totalPrice }
            assertTrue(totalPlanCost <= 50000.0)
        }
    }

    @Test
    fun testCartItemMerging() {
        val initialList = listOf(
            CartItem(itemId = "rice", itemTitle = "Kilombero Rice", selectedProcessor = "Kilombero", selectedWeight = "5kg", quantity = 1, unitPrice = 5000.0)
        )
        val duplicateItem = CartItem(itemId = "rice", itemTitle = "Kilombero Rice", selectedProcessor = "Kilombero", selectedWeight = "5kg", quantity = 2, unitPrice = 5000.0)
        
        val mergedList = addOrMergeCartItem(initialList, duplicateItem)
        assertEquals(1, mergedList.size)
        assertEquals(3, mergedList.first().quantity)
    }

    @Test
    fun testPersonaCombinationValidationRules() {
        // Rule: Student + Office is NOT permitted. Student + Family IS permitted.
        val isStudentAndOfficeValid = isValidPersonaCombination(isStudent = true, isOffice = true, isFamily = false)
        assertFalse(isStudentAndOfficeValid)

        val isStudentAndFamilyValid = isValidPersonaCombination(isStudent = true, isOffice = false, isFamily = true)
        assertTrue(isStudentAndFamilyValid)

        val isAllThreeValid = isValidPersonaCombination(isStudent = true, isOffice = true, isFamily = true)
        assertFalse(isAllThreeValid)
    }

    private fun isValidPersonaCombination(isStudent: Boolean, isOffice: Boolean, isFamily: Boolean): Boolean {
        val isAllThree = isStudent && isOffice && isFamily
        val isStudentAndOffice = isStudent && isOffice
        val isNone = !isStudent && !isOffice && !isFamily
        return !isAllThree && !isStudentAndOffice && !isNone
    }
}
