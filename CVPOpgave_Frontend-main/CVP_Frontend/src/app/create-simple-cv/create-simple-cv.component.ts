import { Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import { FormBuilder,FormControl, FormGroup, FormArray } from '@angular/forms';
import { CV } from '../objects/cv';
import { User } from '../objects/user';
import { CvsService } from '../cvs.service';
import { CVState } from '../objects/CVState';

@Component({
  selector: 'app-create-simple-cv',
  templateUrl: './create-simple-cv.component.html',
  styleUrls: ['./create-simple-cv.component.css']
})
export class CreateSimpleCvComponent implements OnInit {

  cvAddForm:FormGroup;
  currentDescription:string|undefined;
  currentTechBackground:string|undefined;
  currentWeeklyMaxHours:number|undefined;
  currentBookedHours:number|undefined;
  submitted: boolean;
  originalCV:CV |undefined;
  userLoggedIn:User | undefined;
  cvCreated:CV|undefined;
  currentTitle:string|undefined;
  
  httpError:string;
  loginError:string;
  maxHoursError:string;
  bookedHoursError:string;
  descriptionError:string;
  techBackgroundError:string;
  titleError:string;

  inDraft:CVState;
published:CVState;
archived:CVState;

  constructor(private cvsService: CvsService) {
    
  this.inDraft= CVState.inDraft;
  this.published = CVState.published;
  this.archived = CVState.archived;
    this.submitted = false; 
  
    this.userLoggedIn = history.state.user;

  this.cvAddForm = new FormGroup ({
      description: new FormControl(''),
      techBackground: new FormControl(''),
      bookedHours: new FormControl(''),
      weeklyHours: new FormControl(''),
      title: new FormControl('CV (Uden titel)')
      });  
  this.cvAddForm.valueChanges.subscribe(form =>{
    this.currentDescription = form.description;
    this.currentTechBackground = form.techBackground;
     this.currentBookedHours = form.bookedHours;
    this.currentWeeklyMaxHours = form.weeklyHours;
    this.currentTitle = form.title;
    
   })
   this.httpError="";
   this.loginError="";
   this.maxHoursError="";
   this.bookedHoursError="";
   this.descriptionError="";
   this.techBackgroundError="";
   this.titleError = "";
}
  ngOnInit(): void {
    
  }

  onSubmit(state: CVState){
    this.httpError="";
   this.loginError="";
   this.maxHoursError="";
   this.bookedHoursError="";
   this.descriptionError="";
   this.techBackgroundError="";
   this.titleError = "";

   let cvsDBFromLoggedInUser:CV[];
    
    this.currentDescription = this.cvAddForm.value.description;
    this.currentTechBackground = this.cvAddForm.value.techBackground;
    this.currentBookedHours = this.cvAddForm.value.bookedHours;
    this.currentWeeklyMaxHours = this.cvAddForm.value.weeklyHours;

    if(this.userLoggedIn!=undefined){
      this.cvsService.getUserCVs(this.userLoggedIn).subscribe({
        next: (cv) => {
          cvsDBFromLoggedInUser = cv;
          cvsDBFromLoggedInUser = cvsDBFromLoggedInUser.filter(cv =>{return cv.title == this.currentTitle});
          if(this.currentTitle == ""||this.currentTitle === "CV (Uden titel)"|| this.currentTitle == undefined || this.currentTitle == null || cvsDBFromLoggedInUser == undefined){
            this.titleError = "Dette er en ugyldig titel eller blevet brugt før!"
            this.scrollToError(<HTMLInputElement>document.getElementById("title"));
          }
    if(this.currentDescription!=undefined && this.currentDescription != ""
      && this.currentTechBackground!=undefined && this.currentTechBackground != ""
      && this.currentWeeklyMaxHours!=undefined && this.currentWeeklyMaxHours>=1 && this.cvAddForm.value.weeklyHours != ""
      && this.currentBookedHours!= undefined && this.currentBookedHours>=0 && this.cvAddForm.value.weeklyHours != ""
      && this.titleError == "" 
      && this.userLoggedIn != undefined
      ){
        if(this.currentBookedHours>this.cvAddForm.value.weeklyHours){
          this.bookedHoursError = "Du kan ikke sætte timeantal mere end du MAX ønsker at arbejde ugentligt";
           this.scrollToError(<HTMLInputElement>document.getElementById("bookedHours"));
        }
        else
        {
          this.bookedHoursError = "";
          
        this.originalCV = new CV(this.currentBookedHours,this.currentWeeklyMaxHours,this.currentDescription,
        this.currentTechBackground,state,null,this.userLoggedIn,this.userLoggedIn, [], [], [], [], [], [], this.currentTitle);
        this.cvsService.addOriginalCV(this.originalCV).subscribe({
            next: (cv) => {
              this.cvCreated = cv;
            },
            error: (e) => {
            this.httpError = e.error.status + " HTTP : "+ e.error;              
            },
            complete: () => console.info('complete')
           });

        this.submitted = true;
        }
      }  },
        error: (e) => {
        this.httpError = e.error.status + " HTTP : "+ e.error;              
        },
        complete: () => console.info('complete')
      }); 
     }
      else if (this.userLoggedIn == undefined){
        this.loginError = "Du skal være logget ind for at kunne oprette et CV!";
        this.scrollToError(<HTMLInputElement>document.getElementById("cvTitel"));
      }else{
        if(this.currentBookedHours != undefined && this.cvAddForm.value.bookedHours !="" && this.currentBookedHours<0){
          this.bookedHoursError = "Arbejdstimer må ikke være mindre end 0";
          this.scrollToError(<HTMLInputElement>document.getElementById("bookedHours"));
        } else if(this.currentBookedHours==undefined && this.cvAddForm.value.bookedHours!=0 || this.cvAddForm.value.bookedHours =="" && this.cvAddForm.value.bookedHours!=0){
          this.bookedHoursError = "Udfyld dine nuværende arbejdstimer i denne type job her";
          this.scrollToError(<HTMLInputElement>document.getElementById("bookedHours"));
        }        
        
        if(this.currentWeeklyMaxHours!= undefined && this.cvAddForm.value.weeklyHours == 0 && this.currentWeeklyMaxHours<1){
          this.maxHoursError = "Arbejdstimer må ikke være mindre end 1";
          this.scrollToError(<HTMLInputElement>document.getElementById("weeklyHours"));
          }else if((this.currentWeeklyMaxHours==undefined || this.cvAddForm.value.weeklyHours =="") && this.currentWeeklyMaxHours!=0){    
          this.maxHoursError = "Udfyld dine max antal ønskede arbejdstimer om ugen for denne type job her";
          this.scrollToError(<HTMLInputElement>document.getElementById("weeklyHours"));
          }
        if(this.currentDescription==undefined || this.cvAddForm.value.description ==""){
          this.descriptionError ="Udfyld beskrivelse af dine personlige færdigheder her, såsom 'Effektiv', 'Ansvarlig', mm og beskriv hvordan"
          this.scrollToError(<HTMLInputElement>document.getElementById("description"));
        }
        if(this.currentTechBackground==undefined || this.cvAddForm.value.techBackground ==""){
          this.techBackgroundError ="Udfyld din tekniske baggrund her såsom hvilken teknisk erfaring fra førhen har du"
          this.scrollToError(<HTMLInputElement>document.getElementById("techBackground"));
        }
      }      
  }

  saveForlater(){
    this.onSubmit(CVState.inDraft);
  }

  setSubmittedFalse(){
    this.submitted = false;
   this.httpError="";
   this.loginError="";
   this.maxHoursError="";
   this.bookedHoursError="";
   this.titleError = "";
  }

  scrollToError(error: HTMLElement) {
    error.scrollIntoView({
      block: 'center',
      behavior: 'smooth',
    });
  }

}
