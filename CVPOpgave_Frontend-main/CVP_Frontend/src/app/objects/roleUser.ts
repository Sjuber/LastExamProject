import { Role } from "./role";

export class RoleUser{
    startDate: Date;
    endDate: Date | null;
    roleDTO: Role;
    constructor(
        startDate: Date,
        endDate: Date | null,
        roleDTO: Role
    ) {
       this.startDate = startDate;
       this.endDate = endDate;
       this.roleDTO = roleDTO;
    }
  
}