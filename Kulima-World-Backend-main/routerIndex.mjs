import { Router } from "express";
import userRouter from "./users.mjs";
import authRouter  from "./autheticateUser.mjs";
import paymetRouter from "./payments.mjs";
import productsRouter from "./products.mjs";
const routerIndex = Router()
routerIndex.use(userRouter);
routerIndex.use(authRouter);
routerIndex.use(paymetRouter);
routerIndex.use(productsRouter);

export default routerIndex