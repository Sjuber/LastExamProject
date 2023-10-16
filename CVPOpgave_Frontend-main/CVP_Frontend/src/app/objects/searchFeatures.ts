import { CV_knowledge } from "./cv_knowledge";
import { PractisedLanguage } from "./practisedLanguage";

export class SearchFeatures{
    private cvsKnowledgeDTOList?:CV_knowledge[];
    private languageDTO?:PractisedLanguage;
    private notBookedHours?:number ;
    private companyWorkedFor?:string;
    private prioritiesNamesList: string[];
    private amountMostContainCVknowledgeInCV: number;
    
    constructor(
        prioritiesNamesList:string[],
        amountMostContainCVknowledgeInCV:number,
        cvsKnowledgeDTOList?:CV_knowledge[],
        languageDTO?:PractisedLanguage,
        notBookedHours?:number ,
        companyWorkedFor?:string,
        
        ){
            this.prioritiesNamesList = prioritiesNamesList;
            this.cvsKnowledgeDTOList = cvsKnowledgeDTOList;
            this.languageDTO = languageDTO;
            this.notBookedHours = notBookedHours;
            this.companyWorkedFor = companyWorkedFor;
            this.amountMostContainCVknowledgeInCV = amountMostContainCVknowledgeInCV;
    }
}