
const ProductsValidaion = {
    //product name
    productsName: {
        isString: { 
         errorMessage: "productsName must be a string" 
      },
        notEmpty: { 
         errorMessage: "productsName cannot be empty"
       }
    },
    //price
    price: {
        isString: { 
         errorMessage: "price must be a string" 
      },
        notEmpty: { 
         errorMessage: "price cannot be empty" 
      }
    },
    //product discription
    description: {
        isString: {
          errorMessage: "description must be a string" 
         },
        notEmpty: { 
         errorMessage: "description cannot be empty" 
      }
    },
   
    //the location of the products
    category: {
        isString: { errorMessage: "Category must be a string" },
        notEmpty: { errorMessage: "Category cannot be empty" }
    },
}

export default ProductsValidaion;
