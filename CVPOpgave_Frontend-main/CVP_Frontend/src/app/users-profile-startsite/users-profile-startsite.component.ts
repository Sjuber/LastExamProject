import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { CvsService } from '../cvs.service';
import { AddCourseCertificationComponent } from '../get-user-cvs/add-course-certification/add-course-certification.component';
import { AddEducationComponent } from '../get-user-cvs/add-education/add-education.component';
import { AddCVKnowledgeComponent } from '../get-user-cvs/add-cvknowledge/add-cvknowledge.component';
import { AddJobComponent } from '../get-user-cvs/add-job/add-job.component';
import { AddPractisedLanguageComponent } from '../get-user-cvs/add-practised-language/add-practised-language.component';
import { AddProjectComponent } from '../get-user-cvs/add-project/add-project.component';
import { EditingCVComponent } from '../get-user-cvs/editing-cv/editing-cv.component';
import { CV } from '../objects/cv';
import { Job } from '../objects/job';
import { User } from '../objects/user';

@Component({
  selector: 'app-users-profile-startsite',
  templateUrl: './users-profile-startsite.component.html',
  styleUrls: ['./users-profile-startsite.component.css'],
  providers: [{ provide: Window, useValue: window }]
})
export class UsersProfileStartsiteComponent implements OnInit {
  pageID = 0;
  visibleToCreateCV:boolean;
  visibleToShowCVs:boolean;
  cvToAddTo:CV|undefined;
  jobs:Job[]=[];
  userLoggedIn:User;

  constructor(private dialog: MatDialog,private cvsService:CvsService) { 
    this.visibleToCreateCV = false;
    this.visibleToShowCVs = true;
    this.cvToAddTo = undefined;
    this.userLoggedIn = history.state.user; 
  }

  ngOnInit(): void {
  }

  getJobsForCVToAddTo(jobs:Job[]|undefined){
    if(jobs !=undefined){
      this.jobs = [];
      jobs.forEach(job=>{this.jobs.push(job)});
    }
  }

  pageToShow(page:string){
    this.cvToAddTo = undefined;
    const pages = ['visibleToShowCVs','createCV'];
    if(this.pageID == pages.indexOf(page)){
        this.pageID = 0; 
    }else{
       this.pageID = pages.indexOf(page);
    } 
  }

  showAddJob(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = { 
            cv:this.cvToAddTo
          };
          dialogConfig.width = '70%';
          dialogConfig.height = '90%';
         this.dialog.open(AddJobComponent, dialogConfig); 
  }

  showAddCVKnowledge(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = { 
            cv:this.cvToAddTo
          };
          dialogConfig.width = '70%';
          dialogConfig.height = '90%';
         this.dialog.open(AddCVKnowledgeComponent, dialogConfig);
  }

  showAddPractisedLanguage(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = { 
            cv:this.cvToAddTo
          };
          dialogConfig.width = '70%';
          dialogConfig.height = '90%';
          this.dialog.open(AddPractisedLanguageComponent, dialogConfig);
  }

  showAddCourseCertificaction(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = { 
            cv:this.cvToAddTo
          };
          dialogConfig.width = '70%';
          dialogConfig.height = '90%';
         this.dialog.open(AddCourseCertificationComponent, dialogConfig);
  }

  showAddEducation(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = { 
            cv:this.cvToAddTo
          };
          dialogConfig.width = '70%';
          dialogConfig.height = '90%';
         this.dialog.open(AddEducationComponent, dialogConfig);
  }

  showAddProject(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = { 
            cv:this.cvToAddTo
          };
          dialogConfig.width = '90%';
          dialogConfig.height = '90%';
         this.dialog.open(AddProjectComponent, dialogConfig);
  }

  getJobsForCV(cv: CV){
    this.cvsService.cvToShow.subscribe((cv) =>{
      if(cv){
      this.jobs = cv.jobs.sort(function(a:Job, b:Job){
        return new Date(b.startDate).getTime() - new Date(a.startDate).getTime();    
      })
    }
    });
  }

getCVToAddTo(cv:CV|undefined){
    this.cvToAddTo = cv;
}

  showhEditingCV(){
    this.jobs = [];
    if(this.cvToAddTo != undefined){
        this.getJobsForCV(this.cvToAddTo);
    
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = { 
            cv:this.cvToAddTo,
            jobsForCV:this.jobs
          };
          dialogConfig.width = '95%';
          dialogConfig.height = '90%';
          this.dialog.open(EditingCVComponent, dialogConfig);
    }     
  }


}
