import bcrypt,{compare, genSaltSync, hashSync} from "bcrypt"

const saltRound = 10;

export const  hashPassword = (password)=> {
    const salt = bcrypt.genSaltSync(saltRound);
    return bcrypt.hashSync(password,salt);
}

export const comparePassword = (raw,hashd)=> {
    return bcrypt.compareSync(raw,hashd)

}