import { Component, Inject, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgSelectConfig } from '@ng-select/ng-select';
import { CvsService } from 'src/app/cvs.service';
import { Category } from 'src/app/objects/category';
import { CV } from 'src/app/objects/cv';
import { CV_knowledge } from 'src/app/objects/cv_knowledge';
import { Knowledge } from 'src/app/objects/knowledge';
import { AddJobComponent } from '../add-job/add-job.component';


@Component({
  selector: 'app-add-cvknowledge',
  templateUrl: './add-cvknowledge.component.html',
  styleUrls: ['./add-cvknowledge.component.css']
})
export class AddCVKnowledgeComponent implements OnInit {

 cvForAddingCVKnowledge:CV;
 cvKnowledgeList:CV_knowledge[] | null;
 allKnowledgeList:Knowledge[] = [];
 cvAddCVKnowledgeForm:FormGroup;
 levelSkill:String | undefined;
 years:number | undefined;
 note:String | null;
 knowledgeName:String;
 values = '';

 levelSkillError:String | undefined;
 yearsError:String | undefined;
 knowledgeNameError:String | undefined;

  constructor(private dialogRef: MatDialogRef<AddJobComponent>, @Inject(MAT_DIALOG_DATA) public data: any, private cvsService: CvsService, private config: NgSelectConfig) { 
    this.cvForAddingCVKnowledge = data.cv;
    this.knowledgeName = "";
    this.note = "";
    this.cvsService.getAllKnowlegdeFromDB().subscribe({
      next: (allKnowledge) => {
        this.allKnowledgeList = allKnowledge;
      },
      error: (e) => {
        console.error(e);
      }
    });
    this.cvAddCVKnowledgeForm = new FormGroup({
      levelSkill: new FormControl(''),
      years: new FormControl(0),
      note: new FormControl(''), 
    });
    this.cvAddCVKnowledgeForm.valueChanges.subscribe(form => {
      this.levelSkill = form.levelSkill;
      this.years = form.years;
      this.note = form.note;
    });    
    this.config.notFoundText = 'Intet resultat';
    this.cvKnowledgeList = this.cvForAddingCVKnowledge.cv_knowledgeList;
  }

  ngOnInit(): void {
  }

  close() {
    this.dialogRef.close();
 }

 onSubmitAddCV_Knowledge() {
  let cv_knowledge: CV_knowledge;
  let knowledge:Knowledge | undefined = undefined;
  let cvID: Number;
  this.levelSkillError= undefined;
  this.knowledgeNameError=undefined;

  if (this.levelSkill != undefined
    &&this.years != undefined
    &&this.knowledgeName != undefined
    &&this.years>=0
    &&this.levelSkill != ""
    &&this.knowledgeName != ""
  ){
     knowledge = this.allKnowledgeList.find(knowledge => {return knowledge.name == this.knowledgeName;});
    if(this.note != ""){
      knowledge = new Knowledge(new Category("Andet",""));
    }
      if(knowledge){
      cv_knowledge = new CV_knowledge(this.levelSkill,this.years,this.note,knowledge);
      this.cvsService.getCVIDForCV(this.cvForAddingCVKnowledge).subscribe({
        next: (cvIDFromDB) => {
          cvID = cvIDFromDB;
          this.cvsService.addCVKnowledgeForCV(cvID,cv_knowledge).subscribe({
            next: (cv_knowledgeDBList) => {
              this.cvKnowledgeList = cv_knowledgeDBList;
              this.cvForAddingCVKnowledge.cv_knowledgeList = this.cvKnowledgeList;
              this.cvsService.cvToShow.next(this.cvForAddingCVKnowledge);
              this.cvForAddingCVKnowledge.cv_knowledgeList = this.cvKnowledgeList;
              this.knowledgeName="";
            },
            error: (e) => {
              if(e.status == 409){
              this.knowledgeNameError="Denne kompetence er allerede valgt";
              }
              console.error(e);
            }
          });
      },
      error: (e) => {
        console.error(e);
      }
    });
  }
}else{ if(this.knowledgeName == "" || this.knowledgeName==undefined){  
    this.knowledgeNameError="Dette er en ugyldig kompetence";
  }
  if(this.levelSkill==undefined || this.levelSkill==""){
    this.levelSkillError = "Vælg et niveau for kompetence som du tænker er passende";
  }
}
}

}
