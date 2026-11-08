import mongoose from "mongoose";
const ProductsSchema = new mongoose.Schema({
    //product name
    productsName: {
      type:mongoose.Schema.Types.String,
      unique:true,
      required:true  
    },
    //products price
    price:{
        type:mongoose.Schema.Types.String,
        required:true
    },
    //product description
    description: {
        type: mongoose.Schema.Types.String

    },
    //location of the products
    category: {
        type: mongoose.Schema.Types.String
    },

},{timestamps: true})

export const Products = mongoose.model("Products",ProductsSchema)