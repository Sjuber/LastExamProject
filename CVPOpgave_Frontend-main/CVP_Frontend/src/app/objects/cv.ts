import { CourseCertification } from "./courseCertification";
import { Education } from "./education";
import { CVState } from "./CVState";
import { CV_knowledge } from "./cv_knowledge";
import { Job } from "./job";
import { PractisedLanguage } from "./practisedLanguage";
import { Project } from "./project";
import { User } from "./user";
import { Timestamp } from "rxjs";

export class CV{
  
    id:undefined|number;
    bookedHours:number;
    maxHours:number;
    description:string;
    techBackground:string;
    cvStateString:CVState;
    cvOriginalID:CV | null;
    consultantDTO:User | undefined;
    authorDTO:User | undefined;
    cv_knowledgeList:CV_knowledge[];
    jobs:Job[] ;
    practisedLanguages:PractisedLanguage[];
    coursesCertifications:CourseCertification[];
    educations:Education[];
    projects?:Project[];
     title?:string;
     constructor(
      
      bookedHours:number,
      maxHours:number,
      description:string,
      techBackground:string,
      cvStateString:CVState,
      cvOriginalID:CV | null,
      consultantDTO:User,
      authorDTO: User,
      cv_knowledgeList: CV_knowledge[],
      jobs:Job[],
      practisedLanguages:PractisedLanguage[],
      coursesCertifications:CourseCertification[], 
      educations:Education[],
      projects?:Project[],
      title?: string,
      id?:number

    ) {
      this.id = id;
      this.title = title;
       this.description =  description;
       this.techBackground = techBackground;
       this.bookedHours = bookedHours;
       this.maxHours = maxHours;
       this.cvStateString = cvStateString;
       this.cvOriginalID = cvOriginalID;
        this.consultantDTO = consultantDTO;
        this.authorDTO = authorDTO;
        this.cv_knowledgeList = cv_knowledgeList;
        this.jobs = jobs
        this.practisedLanguages = practisedLanguages;
        this.coursesCertifications = coursesCertifications;
        this.educations = educations;
        this.projects = projects;
     }
  
}