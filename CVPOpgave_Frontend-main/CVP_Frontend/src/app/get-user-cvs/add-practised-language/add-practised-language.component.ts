import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { BehaviorSubject } from 'rxjs';
import { CvsService } from 'src/app/cvs.service';
import { CV } from 'src/app/objects/cv';
import { PractisedLanguage } from 'src/app/objects/practisedLanguage';

@Component({
  selector: 'app-add-practised-language',
  templateUrl: './add-practised-language.component.html',
  styleUrls: ['./add-practised-language.component.css']
})
export class AddPractisedLanguageComponent implements OnInit {

  cvForLanguageAdding:CV|undefined;
  cvAddLanguageForm: FormGroup;
  name:String|undefined;
  levelWritting:String|undefined;
  levelReading:String|undefined;
  nameError:String;
  levelWrittingError:String;
  levelReadingError:String;
  cvError:String; 

  constructor(private dialogRef: MatDialogRef<AddPractisedLanguageComponent>, @Inject(MAT_DIALOG_DATA) public data: any, 
  private cvsService: CvsService) { 
    this.cvForLanguageAdding = data.cv;
    this.nameError="";
    this.levelWrittingError="";
    this.levelReadingError="";
    this.cvError="";
    this.cvAddLanguageForm = new FormGroup({
      name: new FormControl(''),
      levelWritting: new FormControl(''),
      levelReading: new FormControl('')
    });
    this.cvAddLanguageForm.valueChanges.subscribe(form => {
      this.name = form.name;
      this.levelWritting = form.levelWritting;
      this.levelReading = form.levelReading;
    })
   }

  ngOnInit(): void {
  }

  close() {
    this.dialogRef.close();
 }

 onSubmitAddLanguage(){
  this.nameError="";
  this.levelWrittingError="";
  this.levelReadingError="";
  this.cvError = "";
  let emptyFiledErrorMessage:String="Feltet må ikke være tomt";
  let newLanguage:PractisedLanguage;

  if(this.name && this.name!="" && this.levelWritting && this.levelWritting!="" && this.levelReading && this.levelReading!=""){
    newLanguage = new PractisedLanguage(this.name, this.levelWritting, this.levelReading);
    if(this.cvForLanguageAdding){
      this.cvsService.getCVIDForCV(this.cvForLanguageAdding).subscribe((cvID)=>{
      this.cvsService.addLanguage(cvID, newLanguage).subscribe({
        next: (languageDB) => {
          this.cvForLanguageAdding?.practisedLanguages.push(languageDB);
          this.cvsService.cvToShow = new BehaviorSubject<CV|undefined>(this.cvForLanguageAdding);
          this.cvsService.httpError.next("");
        },
        error: (e) => {
          if(e.error.text == "The language already exist i database"){
            this.cvError = "Sproget eksistere allerede på dette CV";
          }else{
          this.cvError = e.status + ": " + e.error;
        }
      }
      });   
    });
    }else {
      this.cvError = "CV'et er ikke registreret";
    }
  }else {
    if (!this.name|| this.name==""){
    this.nameError = emptyFiledErrorMessage;
    }
    if (!this.levelWritting || this.levelWritting==""){
      this.levelWrittingError= emptyFiledErrorMessage;
    } 
    if (!this.levelReading || this.levelReading==""){
      this.levelReadingError = emptyFiledErrorMessage;
    }
  }

 }

}
