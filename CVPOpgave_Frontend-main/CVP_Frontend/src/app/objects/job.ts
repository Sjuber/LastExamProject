import { Company } from "./company";
import { CV } from "./cv";

export class Job{
    id:number |null;
    startDate: Date;
    endDate: Date | null;
    title: String;
    company: Company;
    constructor(
        id:number |null,
        startDate: Date,
        endDate: Date | null,    
        title: String,
        company: Company,
    ) {
        this.id = id;
       this.startDate = startDate;
       this.endDate = endDate;
       this.title = title;
       this.company = company;
    }
  
}