import passport from "passport";
import "../stratagy/loca-stratagy.mjs"
import { request, response, Router } from "express";

const router = Router();

//auth endpoint
router.post("/api/auth",passport.authenticate("local"),(request,response) => {
    return response.status(200).send({"msg":"Success!!"});
})
//auth status
router.get("/api/auth/status",(request,response) => {
    if(!request.user) return response.sendStatus(404);
    return response.status(200).send(request.user);
})

export default router;