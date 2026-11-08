import { Router } from "express";
import { upload } from "../utils/multerConfig.mjs";
import ProductsValidaion from "../utils/ProductsValidaion.mjs";
import { validationResult, checkSchema, matchedData } from "express-validator";
import { Products } from "../dbSchemers/ProductsSchema.mjs";

const router = Router();

// Endpoint for creating a listing
router.post(
  "/api/products",
  upload.fields([{ name: "productImage", maxCount: 1 }]),
  checkSchema(ProductsValidaion),
  async (req, res) => {
    const results = validationResult(req);
    if (!results.isEmpty()) {
      return res.status(400).json({ error: results.array() });
    }

    const data = matchedData(req);

    const newProduct = new Products(data);
    try {
      const savedProduct = await newProduct.save();
      return res.status(201).json(savedProduct);
    } catch (error) {
      console.error(error);
      return res.status(500).json({ error: "Error saving the product" });
    }
  }
);

// Endpoint for retrieving all listings
router.get("/api/products", async (req, res) => {
  try {
    const products = await Products.find();
    res.status(200).json(products);
  } catch (error) {
    console.error("Error fetching listings:", error);
    res.status(500).json({ error: "Error fetching listings" });
  }
});

// Endpoint for getting a product by ID
router.get("/api/products/:id", async (req, res) => {
  try {
    const product = await Products.findById(req.params.id);
    if (!product) {
      return res.status(404).json({ error: "Product not found" });
    }
    res.status(200).json(product);
  } catch (error) {
    console.error("Error retrieving the product:", error);
    res.status(500).json({ error: "Error retrieving the product" });
  }
});

// Endpoint for updating a product by ID
router.put("/api/products/:id", upload.none(), async (req, res) => {
  const { id } = req.params;
  const { body } = req;

  try {
    const updatedProduct = await Products.findByIdAndUpdate(id, body, { new: true });
    if (!updatedProduct) {
      return res.status(404).json({ error: "Product not found" });
    }
    res.status(200).json(updatedProduct);
  } catch (error) {
    console.error("Error updating the product:", error);
    res.status(500).json({ error: "Error updating the product" });
  }
});

// Endpoint for deleting a product by ID
router.delete("/api/products/:id", async (req, res) => {
  try {
    const deletedProduct = await Products.findByIdAndDelete(req.params.id);
    if (!deletedProduct) {
      return res.status(404).json({ error: "Product not found" });
    }
    res.status(200).json({ message: "Product deleted successfully" });
  } catch (error) {
    console.error("Error deleting the product:", error);
    res.status(500).json({ error: "Error deleting the product" });
  }
});

// Endpoint for searching products by name
router.get("/api/products/search", async (req, res) => {
  const { name } = req.query;
  if (!name) {
    return res.status(400).json({ error: "Name parameter is required" });
  }

  try {
    const products = await Products.find({ name: new RegExp(name, "i") });
    res.status(200).json(products);
  } catch (error) {
    console.error("Error searching products:", error);
    res.status(500).json({ error: "Error searching products" });
  }
});

// Endpoint for filtering products by type
router.get("/api/products/type/:type", async (req, res) => {
  try {
    const products = await Products.find({ type: req.params.type });
    res.status(200).json(products);
  } catch (error) {
    console.error("Error fetching products by type:", error);
    res.status(500).json({ error: "Error fetching products by type" });
  }
});

export default router;
