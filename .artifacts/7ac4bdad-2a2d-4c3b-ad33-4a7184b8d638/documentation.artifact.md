# Shop Smart App Documentation

## Overview
Shop Smart is an advanced Android e-commerce application designed for offices, students, and families in Malawi. It features a modern Jetpack Compose Material 3 UI, multi-step checkout, CTech Pay Airtel Money payment gateway integration, Firebase Firestore order persistence, and a System Admin panel for inventory management.

---

## 1. System Admin Inventory Management
- **Access**: Available directly from the Navigation Drawer as **"System Admin Panel"**.
- **Capabilities**:
  - **Add Products**: Real-time addition of new items with name, base price, and category selection.
  - **Delete Products**: Instant removal of items from the live shop catalog via the trash bin icon.

---

## 2. CTech Pay Airtel Money Payment Integration
- **Gateway**: Integrates with the CTech Pay sandbox endpoint (`https://api-sandbox.ctechpay.com/student/mobile/`).
- **Flow**:
  - Collects Customer Details & Delivery Location in Step 2.
  - Collects Airtel payment phone number (10-digit number starting with `099` or `098`) with the Airtel logo displayed on the payment screen.
  - Executes a secure multipart POST request to the CTech Pay gateway.
  - Upon successful payment response, automatically persists the order data in Firebase Firestore.

---

## 3. Firebase Firestore Order Persistence
- **Collection**: `orders`
- **Stored Fields**: `orderId`, `firstName`, `lastName`, `phoneNumber`, `deliveryLocation`, `items` (title, processor, weight, quantity, unit price, total price), `totalAmount`, `status` ("Pending Collection"), and `createdAt`.
- **Retention**: Kept securely in Firestore until backend fulfillment or system deletion upon item collection.

---

## 4. Collection Notice & Policies
- **Collection Window**: Order summary displays an explicit notice that **ordered items must be collected within 2 hours after order time**.

---

## 5. Unit Testing
- Comprehensive unit tests are implemented in [ShopCalculationsTest.kt](file:///C:/Users/Enigma%20Sanudi/AndroidStudioProjects/ShopSmart/app/src/test/java/com/example/shopsmart/ShopCalculationsTest.kt) covering cart total price calculations and product price lookups across processors and weights.
