import { Component, Inject, OnInit } from '@angular/core';
import { FormArray, FormControl, FormGroup } from '@angular/forms';
import { MatDialog, MatDialogConfig, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NgSelectConfig } from '@ng-select/ng-select';
import { CvsService } from 'src/app/cvs.service';
import { Broker } from 'src/app/objects/broker';
import { Category } from 'src/app/objects/category';
import { Company } from 'src/app/objects/company';
import { CourseCertification } from 'src/app/objects/courseCertification';
import { Education } from 'src/app/objects/education';
import { CV } from 'src/app/objects/cv';
import { CVState } from 'src/app/objects/CVState';
import { CV_knowledge } from 'src/app/objects/cv_knowledge';
import { Job } from 'src/app/objects/job';
import { Knowledge } from 'src/app/objects/knowledge';
import { PractisedLanguage } from 'src/app/objects/practisedLanguage';
import { Project } from 'src/app/objects/project';
import { Project_Knowledge } from 'src/app/objects/project_knowledge';
import { User } from 'src/app/objects/user';
import { AddJobComponent } from '../add-job/add-job.component';
import { AddCVKnowledgeComponent } from '../add-cvknowledge/add-cvknowledge.component';
import { AddCourseCertificationComponent } from '../add-course-certification/add-course-certification.component';
import { AddEducationComponent } from '../add-education/add-education.component';
import { AddPractisedLanguageComponent } from '../add-practised-language/add-practised-language.component';
import { AddProjectComponent } from '../add-project/add-project.component';

@Component({
  selector: 'app-editing-cv',
  templateUrl: './editing-cv.component.html',
  styleUrls: ['./editing-cv.component.css']
})
export class EditingCVComponent implements OnInit {
  cvBeforeEditing: CV;
  jobsToEdit: Job[];
  cvEditForm: FormGroup;
  currentDescription: string | undefined;
  currentTechBackground: string | undefined;
  currentWeeklyMaxHours: number | undefined;
  currentBookedHours: number | undefined;
  currentJobs: Job[] | undefined;
  userLoggedIn: User | undefined;
  knowlegdeDBList: Knowledge[]|undefined;
  cvKnowledgeListToBe: CV_knowledge[] = [];
  allKnowledgeList: CV_knowledge[]=[];
  cvIDToUpate:number;
  languageName:String|undefined;
  levelWritting:String|undefined;
  levelReading:String|undefined;
  languagesToBe: PractisedLanguage[]=[]

  languageNameError:String;
  languageLevelError:String;
  httpError: string;
  loginError: string;
  maxHoursError: string;
  bookedHoursError: string;
  descriptionError: string;
  techBackgroundError: string;
  knowledgeNameError:string;
  levelSkillError:string;
  jobError: string;
  courseCertificationError:string;
  educationError:string;
  knowledgeProjectError:string;
  knowledgeProjectErrorNumber:number;
  
  inDraft = CVState.inDraft;
  published = CVState.published;
  archived = CVState.archived;

  constructor(private dialog: MatDialog, @Inject(MAT_DIALOG_DATA) data: any, private cvsService: CvsService, private config: NgSelectConfig) {
    this.cvBeforeEditing = data.cv;
    /*The value -1 is a check so it is sure that we don't have a wrong CV while opdating - there is no CV in database that has 
    this ID value*/ 
    this.cvIDToUpate = -1; 
    this.jobsToEdit = data.jobsForCV;
    this.httpError = "";
    this.loginError = "";
    this.maxHoursError = "";
    this.bookedHoursError = "";
    this.descriptionError = "";
    this.techBackgroundError = "";
    this.knowledgeNameError="";
    this.levelSkillError="";
    this.jobError=""; 
    this.languageLevelError = "";
    this.languageNameError="";
    this.courseCertificationError="";
    this.educationError="";
    this.knowledgeProjectError="";
    this.knowledgeProjectErrorNumber=-1;
    this.config.notFoundText = 'Intet resultat';


    //TODO!!!!!!!!!!!!!! Make cv title editable
    this.cvEditForm = new FormGroup({
      description: new FormControl(this.cvBeforeEditing.description),
      techBackground: new FormControl(this.cvBeforeEditing.techBackground),
      bookedHours: new FormControl(this.cvBeforeEditing.bookedHours),
      maxHours: new FormControl(this.cvBeforeEditing.maxHours),
      jobs: new FormArray([]),
      cvKnowledgeList: new FormArray([]),
      practisedLanguages: new FormArray([]),
      coursesCertifications: new FormArray([]),
      educations: new FormArray([]),
      projects: new FormArray([])
    });
    this.jobsToEdit.forEach(job => {
      this.addJobGroup(job);
    });
     this.cvBeforeEditing.cv_knowledgeList.forEach((cvKnowledge) => {
      this.addCVKnowledge(cvKnowledge);      
    });
    this.cvBeforeEditing.practisedLanguages.forEach((practisedLanguage) => {
      this.addLanguage(practisedLanguage);    
    });
    this.cvBeforeEditing.coursesCertifications.forEach((courseCertification) => {
      this.addCourseCertification(courseCertification);    
    });
    this.cvBeforeEditing.educations.forEach((education) => {
      this.addEducation(education);    
    });
    this.cvBeforeEditing.projects?.forEach((project) => {
      this.addProjectGroup(project);    
    });
  
    this.cvsService.getAllKnowlegdeFromDB().subscribe((knowledgeList)=>{
      this.knowlegdeDBList = knowledgeList;      
    })

    this.cvEditForm.valueChanges.subscribe(form => {
      this.currentDescription = form.description;
      this.currentTechBackground = form.techBackground;
      this.currentBookedHours = form.bookedHours;
      this.currentWeeklyMaxHours = form.maxHours;
   });
  }

  get jobs(): any {
    if (this.cvEditForm != undefined){
      return this.cvEditForm.get('jobs') as FormArray;
    }
  }

  addJobGroup(job: Job) {
    this.jobs.push(new FormGroup({
      startDate: new FormControl(this.formatDateToMonthString(job.startDate)),
      endDate: new FormControl(this.formatDateToMonthString(job.endDate)),
      jobTitle: new FormControl(job.title),
      companyName: new FormControl(job.company.name)
    }));
  }


  getProjects_knowledgeList(projectFormArray:FormArray): any {
      return projectFormArray.get('projects_knowledgeList');
  }

  addProjectsKnowledgeListGroup(formGroup:FormGroup, project_knowledge: Project_Knowledge) {
    let project_knowledgeArray:FormArray = formGroup.get('projects_knowledgeList') as FormArray;
    let knowledgeName = project_knowledge.knowledge.name;
    if(project_knowledge.note){
      knowledgeName = 'Andet'
    }
    let project_knowledgeGroup:FormGroup = new FormGroup({
      noteForProjectKnowledge: new FormControl(project_knowledge.note),
      categoryForProjectKnowledge: new FormControl(project_knowledge.knowledge.knowCategoryDTO.name),
      knowledgeNameForProject: new FormControl(knowledgeName)
    });
    project_knowledgeArray.push(project_knowledgeGroup);
  }


  get projects(): any {
    if (this.cvEditForm != undefined){
      return this.cvEditForm.get('projects') as FormArray;
    }
  }

  addProjectGroup(project: Project) {
      let projectGroup:FormGroup = new FormGroup({
      startDate: new FormControl(this.formatDateToDateString(project.dateStart)),
      endDate: new FormControl(this.formatDateToDateString(project.dateEnd)),
      project_description: new FormControl(project.description),
      roleForProject: new FormControl(project.roleForProject),
      customer: new FormControl(project.customer.name),
      broker: new FormControl(project.broker?.name),
      projects_knowledgeList: new FormArray([])
    })
    project.projectknowledgeList.forEach(project_knowledge => {this.addProjectsKnowledgeListGroup(projectGroup, project_knowledge);});
    this.projects.push(projectGroup);
  }

  get cvKnowledgeList(): any {
    if (this.cvEditForm != undefined){
      return this.cvEditForm.get('cvKnowledgeList') as FormArray;
    }
  }

  addCVKnowledge(cvKnowledge: CV_knowledge) {
    let knowledgeName: String|undefined;
    if(cvKnowledge.note=="" || cvKnowledge.note == null){
      knowledgeName = cvKnowledge.knowledge.name;
    }else {
      knowledgeName = cvKnowledge.note;
    }    
    this.cvKnowledgeList.push(new FormGroup({
      knowledgeName: new FormControl(knowledgeName),
      levelSkill: new FormControl(cvKnowledge.levelSkill),
      cvKnowledgeYears: new FormControl(cvKnowledge.years),
      categoryName: new FormControl(cvKnowledge.knowledge.knowCategoryDTO.name)
    }));
  }

  addLanguage(language:PractisedLanguage){
    this.practisedLanguages.push(new FormGroup({
      languageName: new FormControl(language.name),
      levelWritting: new FormControl(language.levelWritting),
      levelReading: new FormControl(language.levelReading)
      }));
  }

  get practisedLanguages(): any {
    if (this.cvEditForm != undefined){
      return this.cvEditForm.get('practisedLanguages') as FormArray;
    }
  }

  addCourseCertification(courseCertification:CourseCertification){
    this.coursesCertifications.push(new FormGroup({
      courseCertificationStartDate: new FormControl(this.formatDateToDateString(courseCertification.startDate)),
      courseCertificationEndDate: new FormControl(this.formatDateToDateString(courseCertification.endDate)),
      courseCertificationTitle: new FormControl(courseCertification.title)
    }));
  }

  addEducation(education:Education){
    this.educations.push(new FormGroup({
      educationStartYear: new FormControl(education.startYear),
      educationEndYear: new FormControl(education.endYear),
      educationTitle: new FormControl(education.title)
    }));
  }

  get coursesCertifications(): any {
    if (this.cvEditForm != undefined){
      return this.cvEditForm.get('coursesCertifications') as FormArray;
    }
  }

  get educations(): any {
    if (this.cvEditForm != undefined){
      return this.cvEditForm.get('educations') as FormArray;
    }
  }

  formatDateToMonthString(date: any): String {
    if (date != null) {
      const tmpDate = new Date(date);
      const finalYear = tmpDate.getUTCFullYear();
      let finalMonth = "";
      const month = tmpDate.getUTCMonth() + 1;

      if (month < 10) {
        finalMonth = "0" + month;
      } else {
        finalMonth = month + "";
      }

      return finalYear + "-" + finalMonth;
    } else {
      return "";
    }
  }

  formatDateToDateString(date: any): String {
    const tmpDate = new Date(date);
    const finalYear = tmpDate.getUTCFullYear();
    const month = tmpDate.getUTCMonth() + 1;
    const day = tmpDate.getUTCDate();
      if(finalYear<1900){
        this.courseCertificationError = "Datoen er ikke gyldig"
        return "";
      }
    else if (date != null) {  
      let finalMonth = "";
      let finalDay = "";
      
      if (month < 10) {
        finalMonth = "0" + month;
      } else {
        finalMonth = month + "";
      }

      if (day < 10) {
        finalDay = "0" + day;
      } else {
        finalDay = day + "";
      }

      return finalYear + "-" + finalMonth + "-"+finalDay;
    } else {
      return "";
    }
  }

  ngOnInit(): void {
  }

   close() {
  this.dialog.closeAll();
   }

   onSubmit() {
     this.updateCVInDB();
   }

  updateCVInDB() {
    let cvToBeUpdatedTo: CV;
    this.userLoggedIn = history.state.user;
    this.httpError = "";
    this.loginError = "";
    this.maxHoursError = "";
    this.bookedHoursError = "";
    this.descriptionError = "";
    this.techBackgroundError = "";
    this.knowledgeNameError = "";
    this.jobError="";
    this.courseCertificationError = "";
    this.knowledgeProjectError="";

    this.currentDescription = this.cvEditForm.value.description;
    this.currentTechBackground = this.cvEditForm.value.techBackground;
    this.currentBookedHours = this.cvEditForm.value.bookedHours;
    this.currentWeeklyMaxHours = this.cvEditForm.value.maxHours;
    if (this.currentDescription != undefined && this.currentDescription != ""
      && this.currentTechBackground != undefined && this.currentTechBackground != ""
      && this.currentWeeklyMaxHours != undefined && this.currentWeeklyMaxHours >= 1 && this.cvEditForm.value.weeklyHours != ""
      && this.currentBookedHours != undefined && this.currentBookedHours >= 0 && this.cvEditForm.value.weeklyHours != ""
      && this.userLoggedIn != undefined
      && this.cvBeforeEditing != undefined
    ) {
      if (this.currentBookedHours > this.currentWeeklyMaxHours) {
        this.bookedHoursError = "Du kan ikke sætte timeantal mere end du MAX ønsker at arbejde ugentligt";
        this.scrollToError(<HTMLInputElement>document.getElementById("bookedHours"));
      } else {
        cvToBeUpdatedTo = new CV(this.currentBookedHours, this.currentWeeklyMaxHours, this.currentDescription,
        this.currentTechBackground, this.cvBeforeEditing.cvStateString, null, this.userLoggedIn, 
        this.userLoggedIn, this.cvBeforeEditing.cv_knowledgeList, this.cvBeforeEditing.jobs, this.cvBeforeEditing.practisedLanguages,
        this.cvBeforeEditing.coursesCertifications, this.cvBeforeEditing.educations);
        this.cvsService.updateCV(this.cvBeforeEditing, cvToBeUpdatedTo).subscribe({
          next: (cv) => {            
            if (cv == null || cv == undefined) {
              this.httpError = "Vi kunne ikke opdatere dit CV - genindlæs siden eller prøv igen";
              this.scrollToError(<HTMLInputElement>document.getElementById("httpErrorID"));
            } else {
              this.httpError = "";
              this.cvBeforeEditing = cv;
              this.cvsService.getCVIDForCV(this.cvBeforeEditing).subscribe((cvID)=>{
                this.cvIDToUpate = cvID.valueOf();
                if(this.cvIDToUpate>-1){
                  this.updateCVKnowledgeList();
                  this.updateJobs();
                  this.updatePractisedLanguages();
                  this.updateCoursesCertifications();
                  this.updateEducations();
                  this.updateProjects();
                }else{
                    this.httpError = "CV mangler at blive registreret - prøv igen"
                    this.scrollToError(<HTMLInputElement>document.getElementById("httpErrorID"));
                }
              if(this.knowledgeNameError==""
                    && this.jobError == ""
                    && this.httpError == ""
                    && this.loginError == ""
                    && this.maxHoursError == ""
                    && this.bookedHoursError == ""
                    && this.descriptionError == ""
                    && this.techBackgroundError == ""
                    && this.courseCertificationError ==""
                    ){        
                      this.cvsService.cvToShow.next(this.cvBeforeEditing);
                       this.close();
                }
            });
            }
          },
          error: (e) => {
            console.error(e);
          }
        }); 
      }
    }
  }

scrollToError(error: HTMLElement) {
    error.scrollIntoView({
      block: 'center',
      behavior: 'smooth',
    });
}

updateProjects(){
  let projects:Project[] = [];
  
  let knowledgeToAdd: Knowledge|undefined;
  const projectsGroup: FormArray = this.cvEditForm.get('projects') as FormArray;
  
  projectsGroup.controls.forEach(project=>{
    let project_knowledgeList: Project_Knowledge[]=[];
    let projects_knowledgeList:FormArray = project.get('projects_knowledgeList') as FormArray;
    projects_knowledgeList.controls.forEach(project_knowledge => {
    if(project_knowledge.value.knowledgeNameForProject == "Andet"){
      project_knowledgeList.push(new Project_Knowledge(new Knowledge(new Category("Andet", "")),project_knowledge.value.noteForProjectKnowledge));
    }
    knowledgeToAdd = this.knowlegdeDBList?.find(knowledge =>{ 
      if(project_knowledge.value.knowledgeNameForProject == undefined || project_knowledge.value.knowledgeNameForProject == ""){
        this.knowledgeProjectError="Dette er en ugyldig kompetence";
          return;
          }
      return knowledge.name == project_knowledge.value.knowledgeNameForProject
    });
    if(knowledgeToAdd!=undefined){
    project_knowledgeList.push(new Project_Knowledge(knowledgeToAdd, project_knowledge.value.noteForProjectKnowledge));
  } 
  });
  
  projects.push(new Project(project.value.project_description,new Date(project.value.startDate),new Date (project.value.endDate),
    project.value.roleForProject, new Company (project.value.customer), project_knowledgeList, new Broker (project.value.broker,new Date(project.value.startDate),new Date(project.value.endDate))))
  });
  
  this.cvsService.updateProjects(this.cvIDToUpate,projects).subscribe((projectsDB) => {
    if(projectsDB !=null && projectsDB){
    this.cvBeforeEditing.projects = projectsDB;  
    console.log(this.cvBeforeEditing.projects);
    
    }else {
        this.httpError = "Kan ikke opdatere: projekter"
        this.scrollToError(<HTMLInputElement>document.getElementById("httpError"));
    }
  });
}

  updateJobs() {
    let jobsToBe: Job[] = []
    let endDate:Date|null = null;
    const jobs: FormArray = this.cvEditForm.get('jobs') as FormArray;
    
    jobs.controls.forEach(jobInfo => {
        if(jobInfo.value.jobTitle==""|| jobInfo.value.jobTitle==undefined){
          this.jobError="Der er angivet en eller flere tomme jobtitler";
          return;
        }else if(jobInfo.value.companyName==""|| jobInfo.value.companyName==undefined){
          this.jobError ="Der er angivet en eller flere tommme firmaer";
          return;
        }else if(jobInfo.value.startDate==""|| jobInfo.value.startDate==undefined){
          this.jobError="Der er angivet en eller flere tomme startdatoer";
          return;
        }else{
          if(jobInfo.value.endDate != null && jobInfo.value.endDate != undefined && jobInfo.value.endDate != ""){
          endDate = new Date(jobInfo.value.endDate+"-01");
          }else {
            endDate = null;
          }
          if( jobInfo.value.startDate =="" ||(endDate && endDate!=null && endDate?.getTime()<new Date(jobInfo.value.startDate+"-01").getTime())){   
            this.jobError="Der er angivet et eller flere steder slutmåneder som slutter før startmåned"
          }
          if(this.jobError==""){
          jobsToBe.push(new Job(null, new Date(jobInfo.value.startDate+"-01"), endDate, jobInfo.value.jobTitle,
          new Company(jobInfo.value.companyName)))
          return;
          }
          return;
      }
    });
    if(this.jobError==""){
      this.cvsService.updateJobsForCV(this.cvIDToUpate, jobsToBe).subscribe((jobs) => {
        this.cvBeforeEditing.jobs = jobs;
     });
    }else{
      this.scrollToError(<HTMLInputElement>document.getElementById("jobErrorID"));
    }
}

updatePractisedLanguages(){
  this.languagesToBe=[];
  const languagesForm: FormArray = this.cvEditForm.get('practisedLanguages') as FormArray;
  languagesForm.controls.forEach(languageInput=>{ 
    if(languageInput.value.languageName == "" || languageInput.value.languageName == undefined){
      this.languageNameError = "Dette er et ugyldigt sprog"
      this.scrollToError(<HTMLInputElement>document.getElementById("languageNameError"));
      return;
    }else{
    this.languagesToBe.push(new PractisedLanguage(languageInput.value.languageName, 
      languageInput.value.levelWritting, languageInput.value.levelReading));
    }
  })
  if(this.languageLevelError=="" && this.languageNameError ==""){
    this.cvsService.updateLanguages(this.cvIDToUpate,this.languagesToBe).subscribe((languagesDB) => {
      if(languagesDB !=null && languagesDB){
      this.cvBeforeEditing.practisedLanguages = languagesDB;  
      }else {
          this.httpError = "Kan ikke opdatere: sprog"
          this.scrollToError(<HTMLInputElement>document.getElementById("httpError"));
      }
    });
  }
}

updateCVKnowledgeList() {
  this.cvKnowledgeListToBe = [];
  const cv_knowledgeList: FormArray = this.cvEditForm.get('cvKnowledgeList') as FormArray;
  let knowledgeToAdd: Knowledge|undefined;

  cv_knowledgeList.controls.forEach(cvKnowledge => {
    knowledgeToAdd = this.knowlegdeDBList?.find(knowledge =>{ 
      if(cvKnowledge.value.knowledgeName == undefined || cvKnowledge.value.knowledgeName == ""){
        this.knowledgeNameError="Dette er en ugyldig kompetence";
          return;
          }
      return knowledge.name == cvKnowledge.value.knowledgeName
    });
    if(knowledgeToAdd?.knowCategoryDTO.name == "Andet"){
      knowledgeToAdd = new Knowledge( new Category("Andet",""));
      this.cvKnowledgeListToBe.push(new CV_knowledge(cvKnowledge.value.levelSkill,cvKnowledge.value.cvKnowledgeYears,cvKnowledge.value.knowledgeName,knowledgeToAdd));
    }else if(knowledgeToAdd != undefined){
        this.cvKnowledgeListToBe.push(new CV_knowledge(cvKnowledge.value.levelSkill,cvKnowledge.value.cvKnowledgeYears,"",knowledgeToAdd));
    } 
  });

  if(this.knowledgeNameError==""){
          this.cvsService.updateCVKnowledgeForCV(this.cvIDToUpate,this.cvKnowledgeListToBe).subscribe((knowledgeListUpdated) => {
            this.cvBeforeEditing.cv_knowledgeList = knowledgeListUpdated;  
      });
  }else {
    this.scrollToError(<HTMLInputElement>document.getElementById("knowledgeNameErrorID"));
  }
}

updateCoursesCertifications() {
  let courseCertificationToBe:CourseCertification[] = [];
  const coursesCertifications: FormArray = this.cvEditForm.get('coursesCertifications') as FormArray;
  
  coursesCertifications.controls.forEach(courseCertification => {
      if(courseCertification.value.courseCertificationTitle==""|| courseCertification.value.courseCertificationTitle==undefined){
        this.courseCertificationError="Der er angivet en eller flere tomme kursus eller certfikation";
      }if(courseCertification.value.courseCertificationStartDate==""|| courseCertification.value.courseCertificationStartDate==undefined){
        this.courseCertificationError="Der er angivet en eller flere tomme startdatoer";
      }if(courseCertification.value.courseCertificationEndDate==""|| courseCertification.value.courseCertificationEndDate==undefined){
        this.courseCertificationError="Der er angivet en eller flere tomme slutdatoer";
      }if(courseCertification.value.courseCertificationEndDate!=null && courseCertification.value.courseCertificationStartDate!= null && 
        new Date(courseCertification.value.courseCertificationEndDate).getTime()<new Date(courseCertification.value.courseCertificationStartDate).getTime()){   
          this.courseCertificationError="Der er angivet et eller flere steder hvor slutdato slutter før startdato";
      }
      if( new Date(courseCertification.value.courseCertificationEndDate).getUTCFullYear() <1900 || new Date(courseCertification.value.courseCertificationStartDate).getUTCFullYear() <1900){
        this.courseCertificationError="For gammel dato (kan ikke være før år 1900)";
      }
      if(this.courseCertificationError==""){
          courseCertificationToBe.push(new CourseCertification(courseCertification.value.courseCertificationTitle, new Date(courseCertification.value.courseCertificationStartDate), 
          new Date(courseCertification.value.courseCertificationEndDate)));
      }
  });
  if(this.courseCertificationError==""){
    this.cvsService.updateCoursesCertificationsForCV(this.cvIDToUpate, courseCertificationToBe).subscribe((coursesCertificationsDB) => {
     this.cvBeforeEditing.coursesCertifications = coursesCertificationsDB;
      this.cvsService.cvToShow.next(this.cvBeforeEditing);
   });
  }else{
    this.scrollToError(<HTMLInputElement>document.getElementById("courseCertificationErrorID"));
  }
}

updateEducations() {
  let educationToBe:Education[] = [];
  const educations: FormArray = this.cvEditForm.get('educations') as FormArray;
  
  educations.controls.forEach(education => {
      if(education.value.educationTitle==""|| education.value.educationTitle==undefined){
        this.educationError="Der er angivet en eller flere tomme felter";
      }if(education.value.educationStartYear==""|| education.value.educationStartYear==undefined){
        this.courseCertificationError="Der er angivet en eller flere tomme start år";
      }if(education.value.educationEndYear==""|| education.value.educationEndYear==undefined){
        this.courseCertificationError="Der er angivet en eller flere tomme slut år";
      }if(education.value.educationEndYear!=null && education.value.educationStartYear!= null && 
        education.value.educationEndYear < education.value.educationStartYear){   
          this.educationError="Der er angivet et eller flere steder hvor slutdato slutter før startdato";
      }
      if( education.value.educationEndYear < 1900 || education.value.educationStartYear < 1900 ){
        this.educationError="For gammel dato (kan ikke være før år 1900)";
      }
      if(this.educationError==""){
        educationToBe.push(new Education(education.value.educationTitle, education.value.educationStartYear, 
          education.value.educationEndYear));
      }
  });
  if(this.educationError==""){
    this.cvsService.updateEducationsForCV(this.cvIDToUpate, educationToBe).subscribe((educationsDB) => {
     this.cvBeforeEditing.educations = educationsDB;
      this.cvsService.cvToShow.next(this.cvBeforeEditing);
   });
  }else{
    this.scrollToError(<HTMLInputElement>document.getElementById("educationErrorID"));
  }
}

removeJob(jobToDelete: any) {
  const jobs: FormArray = this.cvEditForm.get('jobs') as FormArray;
  const indexForJob = jobs.controls.findIndex(job => job.value.jobTitle == jobToDelete.jobTitle && job.value.companyName == jobToDelete.companyName && job.value.startDate == jobToDelete.startDate);
  jobs.removeAt(indexForJob);
}
removeCVKnowledge(indexForCVKnowledge:number) {
  const cvKnowlegdeList: FormArray = this.cvEditForm.get('cvKnowledgeList') as FormArray;
  cvKnowlegdeList.removeAt(indexForCVKnowledge);
}
removeLanguage(indexForLanguage:number) {
  const languages: FormArray = this.cvEditForm.get('practisedLanguages') as FormArray;
  languages.removeAt(indexForLanguage);
}
removeCourseCertification(indexForCourseCertification:number) {
  console.log("index: '" + indexForCourseCertification + "' deleted");
  const courseCerts: FormArray = this.cvEditForm.get('coursesCertifications') as FormArray;
  console.log(courseCerts);
  courseCerts.removeAt(indexForCourseCertification);
}

removeEducation(indexForEducation:number) {
  console.log("index: '" + indexForEducation + "' deleted");
  const educations: FormArray = this.cvEditForm.get('educations') as FormArray;
  console.log(educations);
  educations.removeAt(indexForEducation);
}

removeProjectKnowledge(indexForProject_Knowledge:number, indexForProject:number, project_knowledgeList: FormArray){
  this.knowledgeProjectError="";
  if(project_knowledgeList.length>1){
    project_knowledgeList.removeAt(indexForProject_Knowledge);
  }
  else {
    this.knowledgeProjectError = "Du må ikke have mindre end 1 kompentence for projektet";
    this.scrollToError(<HTMLInputElement>document.getElementById("knowledgeProjectErrorID"+indexForProject));
    this.knowledgeProjectErrorNumber = indexForProject; 
  }
}

  removeProject(indexForProject:number){
    const projects: FormArray = this.cvEditForm.get('projects') as FormArray; 
    projects.removeAt(indexForProject);
  }

  showAddJob(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = { 
            cv:this.cvBeforeEditing
          };
          dialogConfig.width = '70%';
          dialogConfig.height = '90%';
         this.dialog.open(AddJobComponent, dialogConfig); 
  }

  showAddCVKnowledge(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = { 
            cv:this.cvBeforeEditing
          };
          dialogConfig.width = '70%';
          dialogConfig.height = '90%';
         this.dialog.open(AddCVKnowledgeComponent, dialogConfig);
  }

  showAddPractisedLanguage(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = { 
            cv:this.cvBeforeEditing
          };
          dialogConfig.width = '70%';
          dialogConfig.height = '90%';
          this.dialog.open(AddPractisedLanguageComponent, dialogConfig);
  }

  showAddCourseCertificaction(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = { 
            cv:this.cvBeforeEditing
          };
          dialogConfig.width = '70%';
          dialogConfig.height = '90%';
         this.dialog.open(AddCourseCertificationComponent, dialogConfig);
  }

  showAddEducation(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = { 
            cv:this.cvBeforeEditing
          };
          dialogConfig.width = '70%';
          dialogConfig.height = '90%';
         this.dialog.open(AddEducationComponent, dialogConfig);
  }

  showAddProject(){
    let addedCV:CV | undefined;
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = { 
            cv:this.cvBeforeEditing,
            editMode:true
          };
          dialogConfig.width = '90%';
          dialogConfig.height = '90%';
         this.dialog.open(AddProjectComponent, dialogConfig);
        this.cvsService.cvToEdit.subscribe(cv =>{
            addedCV = cv;
            if(this.cvBeforeEditing.projects 
              && addedCV?.projects
              && addedCV?.projects?.length > this.cvBeforeEditing?.projects?.length){
              this.cvBeforeEditing = addedCV;
              let projects:Project[]|undefined = this.cvBeforeEditing.projects;
              this.cvBeforeEditing.projects = [];
              projects?.forEach((project) => {
                this.addProjectGroup(project);    
              });
            }
        });
  }
}
