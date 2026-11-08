const userDataValidation = {
    username:{
        isString:{
            errorMessage:"Username must be a string"
        },
        isLength:{
            options:{min:5,max:255},
            errorMessage:"Username must be atleast 5 charecters"
        },
        notEmpty:{
            errorMessage:"Username cant be empty"
        }
    },
    firstname:{

        isString:{
            errorMessage:"firstname must be a string"
        },
        isLength:{
            options:{min:1,max:255},
            errorMessage:"firstname must be atleast 5 charecters"
        },
        notEmpty:{
            errorMessage:"firstname cant be empty"
        }
    },
    lastname:{
        isString:{
            errorMessage:"lastname must be a string"
        },
        isLength:{
            options:{min:1,max:255},
            errorMessage:"lastname must be atleast 5 charecters"
        },
        notEmpty:{
            errorMessage:"lastnamecant be empty"
        }

    },
    password:{
        isLength:{
            options:{
                min:4,
                max:255
            },
            errorMessage:"Password must be atleast 8 charecters"
        },
        notEmpty:{
            errorMessage:"Psswordcant be empty"
        }

    },
    email: {
        isString: true
    }
}

export default userDataValidation;