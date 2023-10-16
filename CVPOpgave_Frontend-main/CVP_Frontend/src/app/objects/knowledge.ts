import { Category } from "./category";

export class Knowledge{
    name?:string;
    knowCategoryDTO:Category;
    
    constructor(
        knowCategoryDTO:Category,
        name?:string,
        ){
            this.name = name;
            this.knowCategoryDTO = knowCategoryDTO;
    }
}