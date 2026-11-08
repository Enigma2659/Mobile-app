import mongoose, {Schema} from "mongoose";

const userSchema = new mongoose.Schema({
    username:{
        type: mongoose.Schema.Types.String,
        requred: true,
        unique:true
    },
    firstname:{
        type: mongoose.Schema.Types.String,
        requred:true

    },
    lastname:{
        type: mongoose.Schema.Types.String,
        requred:true

    },
    password: {
        type: mongoose.Schema.Types.String,
        requred:true,
        unique:true
    },
    email: {
        type: mongoose.Schema.Types.String,
        requred: true
    }
})

export const User = mongoose.model("User",userSchema);