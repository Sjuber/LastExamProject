                                    import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CvsService } from '../cvs.service';
import { CV } from '../objects/cv';
import { Job } from '../objects/job';
import { SimpleCV } from '../objects/simpleCV';
import * as moment from 'moment';
import { Project } from '../objects/project';

@Component({
  selector: 'app-show-single-cv',
  templateUrl: './show-single-cv.component.html',
  styleUrls: ['./show-single-cv.component.css']
})
export class ShowSingleCVComponent implements OnInit {
  browserDefault = navigator;
  error="";
  cv:SimpleCV;
  cvDB:CV|undefined;
  jobs: Job[];

  constructor(private dialogRef: MatDialogRef<ShowSingleCVComponent>, @Inject(MAT_DIALOG_DATA) data:any, private cvsService:CvsService) {
    this.jobs = [];
    this.error = data.error; 
    this.cv = data.cv;
    if(this.cv.consultantPhone!= undefined){
      this.cvsService.getCVByUserPhoneAndSimpleCV(this.cv, this.cv.consultantPhone).subscribe((cv)=>{
        this.cvDB = cv;
        this.getSortedJobsForCV(this.cvDB);
        this.cvDB.projects = this.sortedProjectsForCV(this.cvDB);
    });
    } 
  }

  ngOnInit(): void {
    
  }

  close() {
    this.dialogRef.close();
}
getSortedJobsForCV(cv: CV){
    this.jobs = cv.jobs.sort(function(a:Job, b:Job){
      return new Date(b.startDate).getTime() - new Date(a.startDate).getTime();  
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
}
