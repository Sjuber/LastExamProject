import { Knowledge } from "./knowledge";

export class Project_Knowledge{
        note?:String;
        knowledge:Knowledge;
    constructor(
        knowledge:Knowledge,
        note?:String,
        ){
            this.note = note;
            this.knowledge = knowledge;

    }
}
