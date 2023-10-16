import { Knowledge } from "./knowledge";

export class CV_knowledge{
    levelSkill:String;
        years:Number;
        note:String | null;
        knowledge:Knowledge;
        levelSkillRated?:number;
    constructor(
        levelSkill:String,
        years:Number,
        note:String|null,
        knowledge:Knowledge,
        levelSkillRated?:number
        ){
            this.levelSkill = levelSkill;
            this.years = years;
            if(years == undefined){
                this.years = 0;
            }
            this.note = note;
            this.knowledge = knowledge;
            this.levelSkillRated = levelSkillRated;
    }
}
