import { User } from "../dbSchemers/userSchema.mjs";
import { hashPassword } from "../utils/helpers.mjs";
import { validationResult,checkSchema,matchedData } from "express-validator";
import {request, Router } from "express";
import userDataValidation from "../utils/userDataValidation.mjs";

const router = Router();
// Endpoint for registering a new user (student or agent)
router.post("/api/users/register", checkSchema(userDataValidation), async (request, response) => {
    const results = validationResult(request);
    if (!results.isEmpty()) return response.status(400).send({ error: results.array() });

    const data = matchedData(request);
    const newUser = new User(data);
    newUser.password = await hashPassword(data.password);
    try {
        const savedUser = await newUser.save();
        return response.status(201).send(savedUser);
    } catch (error) {
        console.log(`Error registering user: ${error}`);
        return response.sendStatus(500);
    }
});

//geting
router.get("/api/users",async(request,response) => {
    console.log(request.session);
   // request.session.visited = true
    request.sessionStore.get(request.sessionID, (err,sessionData)=> {
        if(err) {
            console.log(err)
            throw err
        }          
        console.log(sessionData);
        console.log(request.sessionID);
    })
   const users = await User.find({})
   return response.status(200).send(users);

})


// Endpoint for retrieving user profile by ID
router.get("/api/users/:id", async (request, response) => {
    try {
        const user = await User.findById(request.params.id).select("-password");
        if (!user) return response.status(404).send({ error: "User not found" });
        return response.status(200).send(user);
    } catch (error) {
        console.log(`Error fetching user profile: ${error}`);
        return response.sendStatus(500);
    }
});

// Endpoint for updating user profile by ID
router.put("/api/users/:id", checkSchema(userDataValidation), async (request, response) => {
    const results = validationResult(request);
    if (!results.isEmpty()) return response.status(400).send({ error: results.array() });

    const data = matchedData(request);

    try {
        const updatedUser = await User.findByIdAndUpdate(request.params.id, data, { new: true, runValidators: true }).select("-password");
        if (!updatedUser) return response.status(404).send({ error: "User not found" });
        return response.status(200).send(updatedUser);
    } catch (error) {
        console.log(`Error updating user profile: ${error}`);
        return response.sendStatus(500);
    }
});

// Endpoint for deleting user account by ID
router.delete("/api/users/:id", async (request, response) => {
    try {
        const deletedUser = await User.findByIdAndDelete(request.params.id);
        if (!deletedUser) return response.status(404).send({ error: "User not found" });
        return response.status(200).send({ message: "User deleted successfully" });
    } catch (error) {
        console.log(`Error deleting user: ${error}`);
        return response.sendStatus(500);
    }
});

// Endpoint for checking user role by ID
router.get("/api/users/role/:id", async (request, response) => {
    try {
        const user = await User.findById(request.params.id).select("role");
        if (!user) return response.status(404).send({ error: "User not found" });
        return response.status(200).send({ role: user.role });
    } catch (error) {
        console.log(`Error fetching user role: ${error}`);
        return response.sendStatus(500);
    }
});


router.delete("/api/users",(request,respose) => {
    //route for deleting user

})

export default router;
