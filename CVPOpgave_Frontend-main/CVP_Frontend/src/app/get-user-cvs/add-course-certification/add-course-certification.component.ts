import { Component, Inject, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import * as moment from 'moment';
import { CvsService } from 'src/app/cvs.service';
import { CourseCertification } from 'src/app/objects/courseCertification';
import { CV } from 'src/app/objects/cv';

@Component({
  selector: 'app-add-course-certification',
  templateUrl: './add-course-certification.component.html',
  styleUrls: ['./add-course-certification.component.css']
})
export class AddCourseCertificationComponent implements OnInit {

  browserDefault = navigator;
  cvForCourseCertificationAdding:CV;
  coursesCertifications:CourseCertification[];
  cvAddCourseCertificationForm:FormGroup;
  currentTitle:String|undefined;
  currentStartDate:string|undefined;
  currentEndDate:string|undefined;

  httpError:String;
  titleError:String;
  dateError:String;

  constructor(private dialogRef: MatDialogRef<AddCourseCertificationComponent>, @Inject(MAT_DIALOG_DATA) public data: any, 
  private cvsService: CvsService) {
    this.cvForCourseCertificationAdding = data.cv;
    this.coursesCertifications = this.cvForCourseCertificationAdding.coursesCertifications;
    this.titleError="";
    this.httpError="";
    this.dateError="";
    this.currentTitle="";
    this.currentStartDate = "";
    this.currentEndDate = "";
    this.cvAddCourseCertificationForm = new FormGroup({
      title: new FormControl(''),
      startDate: new FormControl(''),
      endDate: new FormControl('')
    });
    this.cvAddCourseCertificationForm.valueChanges.subscribe(form => {
      this.currentTitle = form.title,
      this.currentStartDate = form.startDate,
      this.currentEndDate = form.endDate
    });
   }

  ngOnInit(): void {
  }
  close() {
    this.dialogRef.close();
 }

 formattingDate(dateValue:any){
  moment.locale(this.browserDefault.language);
  return moment(dateValue).format("DD-MMMM-yyyy");
}

onSubmitAddCourseCertification() {
  let courseCertification:CourseCertification|undefined = undefined;
  this.titleError = "";
  this.dateError = "";
  this.httpError = "";
  let startDate:Date;
  let endDate:Date;

  if(!this.currentTitle || this.currentTitle==""){
    this.titleError = "Du skal angive en titel på kursus eller certificering";
  }
  if(!this.currentStartDate || this.currentStartDate==""||!this.currentEndDate || this.currentEndDate==""){
    this.dateError= "Du skal angive både en startdato og slut dato";
  }
  if(this.currentStartDate && this.currentEndDate){
    startDate = new Date(this.currentStartDate);
    endDate = new Date(this.currentEndDate);
  
    if(startDate.getTime()>endDate.getTime()){
      this.dateError = "Du kan ikke angive en startsmåned efter slutmåned";
    } 
    else if(startDate.getUTCFullYear()<1900 || endDate.getUTCFullYear()<1900){
      this.dateError = "For gammel dato (kan ikke være før år 1900)";
    } 

    if(this.titleError=="" && this.dateError=="" && this.currentTitle){
      courseCertification = new CourseCertification(this.currentTitle, startDate, endDate);
      
      this.cvsService.getCVIDForCV(this.cvForCourseCertificationAdding).subscribe((cvID)=>{
        this.cvsService.addCourseCertification(cvID,courseCertification).subscribe({
          next: (coursesCertificationsDB) => {
            this.coursesCertifications = coursesCertificationsDB;
            if(this.coursesCertifications){
              this.cvForCourseCertificationAdding.coursesCertifications = this.coursesCertifications;
              this.cvsService.cvToShow.next(this.cvForCourseCertificationAdding);
            }
           },
           error: (e) => {
            if(e.error.text = "The course or certification already exist i database"){
              this.titleError = "Kurset eller certifikationen eksistere allerede"
              }else{
            this.httpError = e.status + ": " + e.statusText;
          }
           console.error(e);
           }
        })
      })
    }
  }
}

}
