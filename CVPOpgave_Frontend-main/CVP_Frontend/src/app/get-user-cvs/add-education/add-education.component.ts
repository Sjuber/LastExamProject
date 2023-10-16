import { Component, Inject, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import * as moment from 'moment';
import { CvsService } from 'src/app/cvs.service';
import { Education } from 'src/app/objects/education';
import { CV } from 'src/app/objects/cv';

@Component({
  selector: 'app-add-education',
  templateUrl: './add-education.component.html',
  styleUrls: ['./add-education.component.css']
})
export class AddEducationComponent implements OnInit {

  browserDefault = navigator;
  cvForEducationAdding:CV;
  educations:Education[];
  cvAddEducationForm:FormGroup;
  currentTitle:String|undefined;
  currentStartYear:number|undefined;
  currentEndYear:number|undefined;

  httpError:String;
  titleError:String;
  dateError:String;

  constructor(private dialogRef: MatDialogRef<AddEducationComponent>, @Inject(MAT_DIALOG_DATA) public data: any, 
  private cvsService: CvsService) {
    this.cvForEducationAdding = data.cv;
    this.educations = this.cvForEducationAdding.educations;
    this.titleError="";
    this.httpError="";
    this.dateError="";
    this.currentTitle="";
    this.currentStartYear = 0;
    this.currentEndYear = 0;
    this.cvAddEducationForm = new FormGroup({
      title: new FormControl(''),
      startYear: new FormControl(''),
      endYear: new FormControl('')
    });
    this.cvAddEducationForm.valueChanges.subscribe(form => {
      this.currentTitle = form.title,
      this.currentStartYear = form.startYear,
      this.currentEndYear = form.endYear
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

onSubmitAddEducation() {
  let education:Education|undefined = undefined;
  this.titleError = "";
  this.dateError = "";
  this.httpError = "";
  let startYear:number;
  let endYear:number;

  if(!this.currentTitle || this.currentTitle==""){
    this.titleError = "Du skal angive en titel på uddannelsen";
  }
  if(!this.currentStartYear || this.currentStartYear== 0 ||!this.currentEndYear || this.currentEndYear== 0){
    this.dateError= "Du skal angive både en startdato og slutdato";
  }

  if(this.currentStartYear && this.currentEndYear){
    startYear = this.currentStartYear;
    endYear = this.currentEndYear;
  
    if(startYear > endYear){
      this.dateError = "Du kan ikke angive en startår efter slutår";
    } 
    else if(startYear < 1900 || endYear < 1900){
      this.dateError = "For gammel dato (kan ikke være før år 1900)";
    } 

    if(this.titleError=="" && this.dateError=="" && this.currentTitle) {
      education = new Education(this.currentTitle, startYear, endYear);
      console.log(education);
      
      
      this.cvsService.getCVIDForCV(this.cvForEducationAdding).subscribe((cvID)=>{
        this.cvsService.addEducation(cvID,education).subscribe({
          next: (educationsDB) => {
            this.educations = educationsDB;
            if(this.educations){
              this.cvForEducationAdding.educations = this.educations;
              this.cvsService.cvToShow.next(this.cvForEducationAdding);
            }
           },
           error: (e) => {
            if(e.error.text = "The education already exists in database"){
              this.titleError = "Uddannelsen eksistere allerede"
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
