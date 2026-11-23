# 🛒 Shop Simple (Shop Smart)

**Shop Simple** is a modern, feature-rich Android e-commerce application built with **Jetpack Compose (Material 3)**, **Firebase Cloud Firestore**, **Firebase Authentication**, and **Airtel CTech Pay Gateway**. Tailored specifically for Offices, Students, and Families, it supports advanced features like Smart AI Budgeting, Live GPS Delivery Map Pinpoint Calculation, Real-Time Order Tracking, Wishlist Favorites, and an intuitive Admin Portal.

---

## 🌟 Key Features

1. **User Authentication & Profiles**:
   - Secure Email/Password registration and login.
   - **Remember Me** session persistence via `SharedPreferences`.
   - **Forgot Password** recovery powered by Firebase Auth.
   - Profile picture upload & live preview via gallery picker.
   - Persona customization (Students, Offices, Families).

2. **Smart AI Budgeting Assistant**:
   - Instantly generates optimal shopping lists based on your persona, household/group size, and budget amount.

3. **Airtel CTech Mobile Payment & Checkout**:
   - Secure payment integration with CTech Pay Gateway sandbox (`api-sandbox.ctechpay.com`).
   - Strict transaction verification (rejects wrong PIN or insufficient balance).
   - Instant digital receipt generation with share functionality.

4. **Live GPS Location & Map Distance Calculation**:
   - Real phone device location permission requests (`ACCESS_FINE_LOCATION`) and `LocationManager` integration.
   - Dynamically calculates exact straight-line distance from city base hubs (e.g., UNIMA Campus in Zomba) to custom map pins/hospitals and computes exact delivery fees (`distanceKm * 250`).

5. **Admin Portal**:
   - Complete inventory management with brand/processor variant linking.
   - Real-time customer support chat message listener.
   - Sales analytics, order tracking, and user profile management.

6. **Custom Color Theme & Dark Mode**:
   - Professional custom palette: Navy Deep (`#0B132B`), Dark Slate (`#1C2541`), Steel Blue (`#3A506B`), Sky Cyan (`#5BC0DE`), Soft Peach (`#F4D6B0`), and Warm Amber (`#D97706`).
   - Built-in manual **Dark / Light Mode Toggle** in the slide-out navigation drawer.

---

## 🛠️ Tech Stack & Architecture

- **UI Framework**: Jetpack Compose, Material 3, Material Icons, Coil (Image Loading).
- **Backend & Database**: Firebase Cloud Firestore (Real-time synchronization), Firebase Auth.
- **Networking**: HttpURLConnection with strict connection timeouts (8s) for CTech Pay Gateway.
- **Testing**: JUnit 4 unit test suite covering cart totals, price lookups, AI budget generation, and delivery fee calculations.

---

## 🚀 Getting Started & Setup

1. Open the project in **Android Studio**.
2. Ensure your `app/google-services.json` file is present in the `app/` module root.
3. Sync project with Gradle files (`gradle_sync`).
4. Run the app on an Android emulator or physical device (`app:assembleDebug`).
