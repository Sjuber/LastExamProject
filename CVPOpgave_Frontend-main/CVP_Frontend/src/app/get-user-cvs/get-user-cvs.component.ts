import { Component, EventEmitter, OnInit, Output} from '@angular/core';
import { CvsService } from '../cvs.service';
import * as moment from 'moment';
import { CV } from '../objects/cv';
import { Job } from '../objects/job';
import { User } from '../objects/user';
import { UsersService } from '../users.service';
import { CVState } from '../objects/CVState';
import { BehaviorSubject} from 'rxjs';
import { Project } from '../objects/project';
import { SimpleCV } from '../objects/simpleCV';
import jsPDF from 'jspdf';

@Component({
  selector: 'app-get-user-cvs',
  templateUrl: './get-user-cvs.component.html',
  styleUrls: ['./get-user-cvs.component.css']
})
export class GetUserCVsComponent implements OnInit {
  browserDefault = navigator;
  userLoggedIn:User;
  originalCVs:CV[] = [];
  cvsToChangeState:CV[] = [];
  originalCV:CV | undefined;
  noCVsMessage:string = "";
  jobs:Job[] = [];

  inDraft = CVState.inDraft;
  published = CVState.published;
  archived = CVState.archived;

  @Output() cvToAddTo;

  constructor(private userService:UsersService,private cvsService:CvsService) {
    this.userLoggedIn = history.state.user;
    this.cvToAddTo = new EventEmitter<CV>();
    if(localStorage.getItem("phone")!=null){
      this.userService.getUserByPhone(Number(localStorage.getItem("phone"))).subscribe({
       next: (user) => {
         this.userLoggedIn = user;
         this.getAllUserCVs(this.userLoggedIn);
        },
       error: (e) => {
         console.error(e);
       }
     });
    }
  }

  ngOnInit(): void {
  }

  getAllUserCVs(loggedInUser:User){
    this.cvsService.getUserCVs(loggedInUser).subscribe({
      next: (cvs) => {
        this.originalCVs = cvs.reverse();
        if(cvs.length == 0){
      this.noCVsMessage = "(Du har ikke oprettet et CV endnu)";
    }else {
      this.noCVsMessage = "";
    }
      },
      error: (e) => {
        console.error(e);
      }
    });

  }

  showOriginalCV(cvChosed:CV){
    if(this.originalCV == undefined ||
      (this.originalCV.description != cvChosed.description
       || this.originalCV.techBackground != cvChosed.techBackground
       || this.originalCV.maxHours != cvChosed.maxHours
       || this.originalCV.bookedHours != cvChosed.bookedHours
       || this.originalCV.cvStateString != cvChosed.cvStateString
       || this.originalCV.jobs.length != cvChosed.jobs.length
       || this.originalCV.cv_knowledgeList.length != cvChosed.cv_knowledgeList.length)){
      this.originalCV = cvChosed;
      this.originalCV.projects = this.sortedProjectsForCV(this.originalCV);
    }
    else{
        this.originalCV=undefined;
    }
    if(cvChosed.jobs!=null){
         this.jobs = cvChosed.jobs.sort(function(a:Job, b:Job){
           return new Date(b.startDate).getTime() - new Date(a.startDate).getTime();
         });
        }

    if(this.originalCV){
      this.originalCV.jobs = this.jobs;
    }
    this.cvsService.cvToShow = new BehaviorSubject<CV|undefined>(this.originalCV);
    this.cvsService.cvToShow.subscribe((cv)=>{
      this.originalCV = cv;
      this.addCVToAddTo(this.originalCV);
      this.getAllUserCVs(this.userLoggedIn);
    });
  }

  sortedProjectsForCV(cv: CV){
    return cv.projects?.sort(function(a:Project, b:Project){
      return new Date(b.dateStart).getTime() - new Date(a.dateStart).getTime();
  });
  }

  formattingDate(dateValue:any){
    moment.locale(this.browserDefault.language);
    return moment(dateValue).format("DD-MMMM-yyyy")
  }

  formattingMonth(dateValue:any){
    moment.locale(this.browserDefault.language);
    return moment(dateValue).format("MMMM-yyyy")
  }

  onCheckboxChange(e: any, cv:CV) {
    if (e.target.checked) {
      this.cvsToChangeState.push(cv);
    }else{
      const indexOfCV = this.cvsToChangeState.findIndex(x => {return x == cv});
      console.log(indexOfCV);
      this.cvsToChangeState.splice(indexOfCV,1);
    }
  }

  printTextToConsole(){
    console.log("Llalalal");

  }

  print_A_CV_As_PDF(){
    console.log("Dette er FØR PDF print");
    this.cvsService.print_CV_As_PDF(this.originalCV?.id).subscribe((CeeVee) => {
      const doc = new jsPDF();
      // @ts-ignore: Unreachable code error
      doc.text("\nCV \n\n\n Navn: " +
      CeeVee.title
      + "\n\n Email: " +
      CeeVee.authorDTO?.email
      + "\n\n Teknisk Baggrund: " +
      CeeVee.techBackground
      + "\n\n Bookede timer: " +
      CeeVee.bookedHours
      + "\n\n Max Ugentlige Timer: " +
      CeeVee.maxHours
      + "\n\n Deskription: " +
      CeeVee.description
      ,5,5);
      doc.save("CVPDF.pdf");
      console.log(CeeVee);});
    
   
   //this.cvsService.print_CV_As_PDF(this.originalCV?.id).subscribe(CeeVee => saveAs(CeeVee.toString(), 'CVtoPDF.pdf'));
   
   console.log("Dette er EFTER PDF print");
    }; 


  setCVStateToPublished(){
    this.cvsToChangeState.forEach(cv=>{
      if(cv.consultantDTO && cv.authorDTO){
        let oldCV:CV = new CV(cv.bookedHours, cv.maxHours, cv.description, cv.techBackground,
          cv.cvStateString, cv.cvOriginalID, cv.consultantDTO, cv.authorDTO, cv.cv_knowledgeList, cv.jobs, cv.practisedLanguages,
          cv.coursesCertifications,cv.educations, cv.projects, cv.title);
      cv.cvStateString=CVState.published;
      this.cvsService.updateCV(oldCV,cv); 

      }
    });
    this.cvsToChangeState = [];
  }

  setCVStateToArchived(){
    this.cvsToChangeState.forEach(cv=>{
      if(cv.consultantDTO && cv.authorDTO){
      let oldCV:CV = new CV(cv.bookedHours, cv.maxHours, cv.description, cv.techBackground, cv.cvStateString, cv.cvOriginalID,
        cv.consultantDTO, cv.authorDTO, cv.cv_knowledgeList, cv.jobs, cv.practisedLanguages, cv.coursesCertifications, cv.educations, cv.projects, cv.title);
      cv.cvStateString=CVState.archived;
      this.cvsService.updateCV(oldCV,cv).subscribe((cvUpdated) => {console.log(cvUpdated);});
      }
    });
    this.cvsToChangeState = [];
  }

  deleteCVs(){
    this.cvsToChangeState.forEach(cv=>{
      this.deleteCV(cv);
    })
    this.cvsToChangeState = [];
  }

  deleteCurrentCV(){
    if(this.originalCV != undefined){
    this.deleteCV(this.originalCV);
    }
  }

  deleteCV(cv:CV){
    this.cvsService.deleteCV(cv).subscribe(() => {
      const indexOfCV = this.originalCVs.findIndex(x => {
       return x == cv;
      });
    this.originalCVs.splice(indexOfCV,1);
    this.originalCV = undefined;
    this.addCVToAddTo(undefined);
    if(this.originalCVs.length == 0){
      this.noCVsMessage = "Du har ikke flere CV'er"
    }else {
      this.originalCVs = [];
      this.getAllUserCVs(this.userLoggedIn);
    }
  });
  }

  addCVToAddTo(cv:CV|undefined){
    this.cvToAddTo.emit(cv);
  }
}
