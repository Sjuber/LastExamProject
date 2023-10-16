import { Component, Inject, OnInit } from '@angular/core';
import { FormArray, FormControl, FormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgSelectConfig } from '@ng-select/ng-select';
import * as moment from 'moment';
import { CvsService } from 'src/app/cvs.service';
import { Broker } from 'src/app/objects/broker';
import { Category } from 'src/app/objects/category';
import { Company } from 'src/app/objects/company';
import { CV } from 'src/app/objects/cv';
import { Knowledge } from 'src/app/objects/knowledge';
import { Project } from 'src/app/objects/project';
import { Project_Knowledge } from 'src/app/objects/project_knowledge';

@Component({
  selector: 'app-add-project',
  templateUrl: './add-project.component.html',
  styleUrls: ['./add-project.component.css']
})
export class AddProjectComponent implements OnInit {

  cvForAddingProject:CV;
  allKnowledgeFromDB:Knowledge[] = [];
  cvAddProjectForm:FormGroup;
  description:String;
  startDate:Date|undefined;
  endDate:Date|undefined;
  role:String;
  costumer:Company|undefined;
  knowledgeList:Knowledge[];
  browserDefault = navigator;
  knowledgeName:String;
  projectKnowledgeListForProject:Project_Knowledge[];

  currentDescription:String;
  currentStartDate: string;
  currentEndDate: string;
  currentRole:String;
  currentCustomer:string;
  currentBroker:String;
  note:String;
  editingCVMode:boolean|undefined;

  knowledgeNameError:String;
  descriptionError:String;
  costumerError:String;
  endDateError:String;
  startDateError:String;
  brokerError:String;
  roleError:String;
  httpError:String;
  
  constructor(private dialogRef: MatDialogRef<AddProjectComponent>, @Inject(MAT_DIALOG_DATA) public data: any, private cvsService: CvsService, private config: NgSelectConfig) {
    this.cvForAddingProject = data.cv;  
    this.editingCVMode = data.editMode;
    if(this.editingCVMode == undefined){
      this.editingCVMode = false;
    }  
    this.description = "";
    this.startDate = undefined;
    this.endDate = undefined;
    this.role = "";
    this.note = "";
    this.costumer = undefined;
    this.knowledgeList=[];
    this.knowledgeName="";
    this.projectKnowledgeListForProject = [];
    this.knowledgeNameError="";
    this.descriptionError="";
    this.costumerError="";
    this.endDateError="";
    this.startDateError="";
    this.brokerError = "";
    this.roleError= "";
    this.currentDescription ="";
    this.currentStartDate = "";
    this.currentEndDate="";
    this.currentRole="";
    this.currentCustomer="";
    this.currentBroker="";
    this.httpError = "";
    this.cvsService.getAllKnowlegdeFromDB().subscribe({
      next: (allKnowledgeDB) => {
        this.allKnowledgeFromDB = allKnowledgeDB;
      },
      error: (e) => {
        console.error(e);
      }
    });
    this.config.notFoundText = 'Intet resultat';
    this.cvForAddingProject.projects?.reverse();    
    this.cvAddProjectForm = new FormGroup({
      description: new FormControl(''),
      startDate: new FormControl(''),
      endDate: new FormControl(''),
      role: new FormControl(''),
      customer: new FormControl(''),
      broker:new FormControl(''),
      note: new FormControl('')
    });
    this.cvAddProjectForm.valueChanges.subscribe(form => {
      this.currentDescription = form.description,
      this.currentStartDate = form.startDate,
      this.currentEndDate = form.endDate,
      this.currentRole = form.role,
      this.currentBroker = form.broker,
      this.currentCustomer = form.customer,
      this.note = form.note
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

  removeKnowledgeForCurrentCV(knowledgeNameToRemove:String|undefined) {
    this.projectKnowledgeListForProject = this.projectKnowledgeListForProject.filter(project_knowledge => {return project_knowledge.knowledge.name != knowledgeNameToRemove;});
  }

  addCurrentKnowledgeForCurrentCV() {
    let project_knowledge:Project_Knowledge | undefined;
    let knowledgeDB:Knowledge|undefined = undefined;
    this.knowledgeNameError="";
    project_knowledge = this.projectKnowledgeListForProject.find(project_knowledge => {return project_knowledge.knowledge.name == this.knowledgeName;});
    if(project_knowledge==undefined ){  
      knowledgeDB = this.allKnowledgeFromDB.find(knowledge => {return knowledge.name == this.knowledgeName;});
      if(knowledgeDB!=undefined && this.knowledgeName!=null){
        this.projectKnowledgeListForProject.push(new Project_Knowledge(knowledgeDB));
      }else if(this.note!=null){
        if(this.projectKnowledgeListForProject.find(project_knowledge => {return project_knowledge.note == this.note;})){
          this.knowledgeNameError = "Denne kompetence er allerede tilføjet";
          this.scrollToError(<HTMLInputElement>document.getElementById("knowledgeNameError"));
        }else{
          this.projectKnowledgeListForProject.push(new Project_Knowledge( new Knowledge(new Category('Andet', '')), this.note));
        }
      }
    } else {
      this.knowledgeNameError="Denne kompentence er allerede tilføjet";
      this.scrollToError(<HTMLInputElement>document.getElementById("knowledgeNameError"));
    }
    
  }

  onSubmitAddProject(){
    let newProjectToAdd:Project;
    this.costumerError = "";
    this.descriptionError = "";
    this.startDateError = "";
    this.roleError = "";
    this.knowledgeNameError = "";
    this.httpError = "";
    if(this.currentCustomer == ""
    || this.currentDescription == ""
    || this.currentStartDate == ""
    || this.currentRole == ""
    || this.projectKnowledgeListForProject.length <= 0
    ){
      if(this.currentCustomer == ""){
        this.costumerError = "Du skal indtaste en kunde";
        this.scrollToError(<HTMLInputElement>document.getElementById("costumerError"));
      }if(this.currentDescription == ""){
        this.descriptionError = "Du skal indtaste en beskrivelse";
        this.scrollToError(<HTMLInputElement>document.getElementById("descriptionError"));
      }if(this.currentStartDate == ""){
        this.startDateError = "Du skal indtaste en startdato";
        this.scrollToError(<HTMLInputElement>document.getElementById("startDateError"));
      }if(this.currentRole == ""){
        this.roleError = "Du skal indtaste den titel på rolle som du besad ift. projektet";
        this.scrollToError(<HTMLInputElement>document.getElementById("roleError"));
      }
      if(this.projectKnowledgeListForProject.length<=0){
        this.knowledgeNameError = "Du skal mindst vælge en kompetence til projektet";
        this.scrollToError(<HTMLInputElement>document.getElementById("knowledgeNameError"));
      }
    }else{
      if(this.currentEndDate!="" && new Date(this.currentEndDate).getTime()<new Date(this.currentStartDate).getTime()){
        this.startDateError = "Startdato må ikke være tidligere end slutdato";
        this.scrollToError(<HTMLInputElement>document.getElementById("startDateError"));
      }else{
      newProjectToAdd = new Project(this.currentDescription, new Date(this.currentStartDate), new Date(this.currentEndDate),
      this.currentRole,new Company(this.currentCustomer), this.projectKnowledgeListForProject,new Broker(this.currentBroker,new Date (this. currentStartDate), 
      new Date(this.currentEndDate))); 
      if(this.editingCVMode==false){
      this.cvsService.getCVIDForCV(this.cvForAddingProject).subscribe(cvID=>{
        this.cvsService.addProjectForCV(cvID, newProjectToAdd).subscribe({
          next: (project) => {
            let projectToAdd:Project;
            projectToAdd = project;
            this.cvForAddingProject.projects?.push(projectToAdd);
            this.cvForAddingProject.projects?.reverse();
            this.cvsService.cvToShow.next(this.cvForAddingProject);
           },
           error: (e) => {
           console.error(e.error.text);
           this.httpError = e.error.text;
           this.scrollToError(<HTMLInputElement>document.getElementById("httpErrorID"));
           }
        });
      });
    }else {
      this.cvForAddingProject.projects?.push(newProjectToAdd);
      this.cvForAddingProject.projects?.reverse();
      this.cvsService.cvToEdit.next(this.cvForAddingProject);
    }
    }
    }
  }

  scrollToError(error: HTMLElement) {
    error.scrollIntoView({
      block: 'center',
      behavior: 'smooth',
    });
  }
}
