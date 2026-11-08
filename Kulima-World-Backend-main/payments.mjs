import express, { json } from "express";
import { Router } from "express";
import FormData from "form-data";
import axios from "axios";
import { checkSchema, matchedData, validationResult } from "express-validator";
import paymentValidationSchema from "../utils/paymentValidationSchemer.mjs";
import { Order } from "../dbSchemers/OrderSchema.mjs";

const router = Router();

// Airtel Pay Endpoint
router.post("/api/aitel-access/mobile/pay", checkSchema(paymentValidationSchema), async (req, res) => {
    const result = validationResult(req);
    if (!result.isEmpty()) return res.status(400).json(result.array());

    const data = matchedData(req);
    const { amount, phone } = data;

    const registration = "BEDCOM2622"; 
    const token = "ed905c7f701083538b6ba366f2e6ed70"; 

    const form = new FormData();
    form.append("airtel", "1");
    form.append("token", token);
    form.append("registration", registration);
    form.append("amount", amount);
    form.append("phone", phone);

    const config = {
        method: "post",
        url: "https://api-sandbox.ctechpay.com/student/mobile/", 
        headers: {
            ...form.getHeaders(),
            'Content-Type': 'multipart/form-data',
        },
        data: form,
    };

    try {
        const response = await axios(config);

        // Prepare order data for the database
        const orderData = {
            transactionId: response.data?.data?.transaction?.id || null,
            status: response.data?.data?.transaction?.status || "Unknown",
            amount: amount,
            phone: phone,
            responseCode: response.data?.status?.response_code || "Unknown",
            resultCode: response.data?.status?.result_code || "Unknown",
            message: response.data?.status?.message || "No message",
            createdAt: new Date(),
        };

        // Save the transaction details in the database
        const newOrder = new Order(orderData);
        const savedOrder = await newOrder.save();

        // Send the saved order and API response back to the frontend
        res.status(200).json({
            success: true,
            message: "Payment processed successfully",
            transaction: savedOrder,
            apiResponse: response.data, // Include the full API response
        });
    } catch (error) {
        console.error({ msg: "Error:", error });

        res.status(500).json({
            success: false,
            message: "Payment processing failed",
            error: error.message,
        });
    }
});

export default router;
