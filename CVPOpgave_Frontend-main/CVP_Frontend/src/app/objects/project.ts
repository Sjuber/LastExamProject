import { Broker } from "./broker";
import { Company } from "./company";
import { Project_Knowledge } from "./project_knowledge";

export class Project{
    description:String;
    dateStart:Date;
    dateEnd:Date;
    roleForProject:String;
    customer:Company;
    projectknowledgeList:Project_Knowledge[];
    broker?:Broker;
    constructor(
        description:String,
        dateStart:Date,
        dateEnd:Date,
        roleForProject:String,
        customer:Company,
        projectknowledgeList:Project_Knowledge[],
        broker?:Broker
        ){
            this.description = description;
            this.dateStart = dateStart;
            this.dateEnd = dateEnd;
            this.roleForProject = roleForProject;
            this.customer = customer;
            this.projectknowledgeList = projectknowledgeList;
            this.broker = broker;
    }
}