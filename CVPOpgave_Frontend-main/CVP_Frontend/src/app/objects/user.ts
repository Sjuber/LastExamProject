
import { RoleUser } from "./roleUser";

export class User{
    email: string;
    name: string;
    phone: string;
    userRoleDTOs: RoleUser[];
    maxWeeklyHours:number;
    
    constructor(
        name: string,
        phone: string,
        email: string,
        userRoleDTOs: RoleUser[],
        maxWeeklyHours:number
    ) {
       this.email = email;
       this.name = name;
       this.phone = phone;
       this.userRoleDTOs = userRoleDTOs;
       this.maxWeeklyHours = maxWeeklyHours;
    }
}
