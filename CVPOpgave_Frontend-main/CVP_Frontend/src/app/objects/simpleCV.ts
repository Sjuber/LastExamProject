import { CV_knowledge } from "./cv_knowledge";
import { PractisedLanguage } from "./practisedLanguage";

export class SimpleCV{
    bookedHours:number;
    maxHours:number;
    consultantName:String | undefined;
    consultantPhone:String | undefined;
    cvKnowledgeList:CV_knowledge[];
    languages: PractisedLanguage[];
    sortScore?:number;
    title?: string;
    cvID?: number;

    constructor(
    bookedHours:number,
    maxHours:number,
    consultantName:String,
    consultantPhone:String,
    cvKnowledgeList:CV_knowledge[],
    languages:PractisedLanguage[],
    title?: string,
    cvID?: number
    ) {
       this.bookedHours = bookedHours;
       this.maxHours = maxHours;
       this.consultantName = consultantName;
       this.consultantPhone = consultantPhone;
       this.cvKnowledgeList = cvKnowledgeList;
       this.languages = languages;
       this.title = title;
       this.cvID = cvID
     }
  
}