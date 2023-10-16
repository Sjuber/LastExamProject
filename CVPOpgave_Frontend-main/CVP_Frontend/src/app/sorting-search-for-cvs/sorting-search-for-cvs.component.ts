import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { CvsService } from '../cvs.service';
import { Category } from '../objects/category';
import { CV } from '../objects/cv';
import { CV_knowledge } from '../objects/cv_knowledge';
import { Knowledge } from '../objects/knowledge';
import { LevelSkill } from '../objects/LevelSkill';
import { PractisedLanguage } from '../objects/practisedLanguage';
import { SimpleCV } from '../objects/simpleCV';
import { User } from '../objects/user';
import { ShowSingleCVComponent } from '../show-single-cv/show-single-cv.component';

@Component({
  selector: 'app-sorting-search-for-cvs',
  templateUrl: './sorting-search-for-cvs.component.html',
  styleUrls: ['./sorting-search-for-cvs.component.css']
})
export class SortingSearchForCvsComponent implements OnInit {
  doneLoading: boolean;
  clickedShowList: boolean;
  users: User[];
  consultants: User[];
  allKnowledgeListFromDB: Knowledge[];
  allKnowledgeListFromDBTmp: Knowledge[];
  chosedCVKnowledgeList: CV_knowledge[];
  chosedCVFromDB?: CV;
  levelSkillRating?: number;
  years: number;
  searchGroup: FormGroup;
  knowledgeName: string;
  simpleCVs: SimpleCV[];
  countGreen: number;
  countYellow: number;
  uniqePhoneNumbersForConsultants: Set<String | undefined>;
  simpleCVsToBeSorted: Map<SimpleCV | undefined, number>;
  countPages: number;
  simpleCVsToAdd?: SimpleCV[];
  colorsOn: boolean;
  notBooked: number;
  levelWriting?: string;
  levelSpeaking?: string;
  levelWritingRating?: number;
  levelSpeakingRating?: number;
  languageName?: string;
  workedForCompany?: string;
  addedCompany: boolean;
  priorityCVKnowledge:string;
  priorityIndustry:string;
  priorityLanguage:string;
  priorityFreeHours:string;
  priorityLetters: string[];
  allLanguageFromDB?: PractisedLanguage[];
  error: string;
  languageNameError:string;
  priorityNamesList: string[];
  amountCVKnowledge: number;
  helpImportantBox: boolean;
  

  constructor(private cvsService: CvsService, private dialog: MatDialog) {
    this.simpleCVsToBeSorted = new Map();
    this.uniqePhoneNumbersForConsultants = new Set();
    this.countGreen = 0;
    this.countYellow = 0;
    this.error = "";
    this.languageNameError = "";
    this.clickedShowList = false;
    this.users = [];
    this.simpleCVs = [];
    this.colorsOn = false;
    this.consultants = [];
    this.chosedCVKnowledgeList = [];
    this.allKnowledgeListFromDB = [];
    this.allKnowledgeListFromDBTmp = [];
    this.countPages = 0;
    this.doneLoading = false;
    this.priorityLetters = ['.A', '.B','.C','.D'];
    this.priorityCVKnowledge = this.priorityLetters[0];
    this.priorityIndustry = this.priorityLetters[1];
    this.priorityLanguage = this.priorityLetters[2];
    this.priorityFreeHours = this.priorityLetters[3];
    this.amountCVKnowledge = 0;
    this.helpImportantBox = false;
    
    //Beacuse minus years is not possible - so we make it to be sure it goes wrong if it doesn't change
    this.years = -1;
    this.notBooked = -1;
    this.cvsService.getAllKnowlegdeFromDB().subscribe({
      next: (allKnowledge) => {
        this.allKnowledgeListFromDB = allKnowledge;
        this.allKnowledgeListFromDB.forEach(knowledge => this.allKnowledgeListFromDBTmp.push(knowledge));
      },
      error: (e) => {
        console.error(e);
      }
    });
    this.priorityNamesList = this.getPriorityNameList();
    this.loadingMoreConsultants(); //TODO to change to just loadingConsultants?
    this.searchGroup = new FormGroup({
      levelSkillRating: new FormControl(),
      years: new FormControl(),
      knowledgeName: new FormControl(''),
      notBooked: new FormControl(),
      levelWriting: new FormControl(),
      levelSpeaking: new FormControl(),
      languageName: new FormControl(),
      workedForCompany: new FormControl('[Branche]'),
      priorityCVKnowledge: new FormControl(this.priorityCVKnowledge),
      priorityIndustry: new FormControl(this.priorityIndustry),
      priorityLanguage: new FormControl(this.priorityLanguage),
      priorityFreeHours: new FormControl(this.priorityFreeHours),
    });
    this.knowledgeName = "";
    this.addedCompany = false;
    this.searchGroup.valueChanges.subscribe(form => {
      this.levelSkillRating = form.levelSkillRating;
      this.years = form.years;
      this.knowledgeName = form.knowledgeName;
      this.notBooked = form.notBooked;
      this.levelWriting = form.levelWriting;
      this.levelSpeaking = form.levelSpeaking;
      this.languageName = form.languageName;
      this.workedForCompany = form.workedForCompany;
      this.colorsOn = false;
      this.priorityCVKnowledge = form.priorityCVKnowledge;
      this.priorityIndustry = form.priorityIndustry;
      this.priorityLanguage = form.priorityLanguage;
      this.priorityFreeHours = form.priorityFreeHours;
    });
    this.doneLoading = false;
  }

  ngOnInit(): void {
  }

  getPriorityNameList(){
    let priorityNamesList = ['','','',''];

    priorityNamesList[this.priorityLetters.indexOf(this.priorityCVKnowledge)] = "cvs_knowlegde";
    priorityNamesList[this.priorityLetters.indexOf(this.priorityIndustry)] = "industry";
    priorityNamesList[this.priorityLetters.indexOf(this.priorityLanguage)] = "language";
    priorityNamesList[this.priorityLetters.indexOf(this.priorityFreeHours)] = "freeHours";
    
    return priorityNamesList;
  }

  selectOption(knowledge: Knowledge) {
    this.searchGroup.get('knowledgeName')?.setValue(knowledge.name);
  }

  loadingMoreConsultants() {
    this.doneLoading = false;
    this.loadingConsultants(this.countPages, this.priorityNamesList);
    this.countPages = this.countPages + 1;
      
  }

  loadingConsultants(pageNumber: number, priorityNamesList:string[]) {
    this.cvsService.httpError.next('');
    this.languageNameError = "";
    this.error = "";
    let practisedLanguage: PractisedLanguage | undefined;
    
      if(this.languageName){
        
        if(this.levelSpeaking == ''){
          this.levelSpeaking = "IkkeRelevant";
        }

        if(this.levelWriting == ''){
          this.levelWriting = "IkkeRelevant";
        }
      practisedLanguage = new PractisedLanguage(this.languageName, this.levelWriting, this.levelSpeaking);
    }
    if (this.workedForCompany == '[Branche]' || this.workedForCompany == '') {
      this.workedForCompany == null
    }

    if(this.languageNameError == ""){
      console.log(this.amountCVKnowledge);
      
        this.cvsService.getAllSimpleCVsPublishedBySearchFeatures(
          priorityNamesList, this.chosedCVKnowledgeList, practisedLanguage, this.notBooked, this.workedForCompany,
          pageNumber, 8, this.amountCVKnowledge).subscribe({
            next: (pagedSimpleCVs) => {
              this.simpleCVsToAdd = pagedSimpleCVs;
              this.simpleCVs = this.simpleCVs.concat(this.simpleCVsToAdd);
              this.doneLoading = true;
              this.clickedShowList = false;
            },
            error: (error: Error) => {
              this.doneLoading = true;
            }

          });
        return this.simpleCVs; 
        }
    
}
  sortSimpleCVsBySearchFeatues() {
    if ((!this.languageName) && this.levelSpeaking 
      || (!this.languageName) && this.levelWriting 
      || this.languageName == "" && this.levelSpeaking 
      || this.languageName == "" && this.levelWriting 
      ) {
      this.languageNameError = "Du skal vælge navn på sprog før niveau'er"
    }else {
    this.priorityNamesList = this.getPriorityNameList();
      if(this.priorityNamesList.filter(name => {return name === ''}).length != 0){
        this.error = "Der er flere med samme prioritet";
      }else{
        this.simpleCVs = [];
    this.doneLoading = false;
    this.simpleCVsToAdd = [];
    this.countPages = 0;
    this.error = "";
    this.loadingMoreConsultants();
    }
    this.colorsOn = true;
  }
  }

  showList() {
    if (this.clickedShowList == false) {
      this.clickedShowList = true;
    } else {
      this.clickedShowList = false;
    }
  }

  addCVKnowledgeForSortingSearch() {
    let knowledge: Knowledge | undefined;
    let levelSkill: string;
    this.error = "";
    if (this.knowledgeName == null || this.knowledgeName == "") {
      this.error = "Du skal vælge en kompentence og evt. et niveau og/eller et antal års erfaring for kompetencen";
    } else {
      knowledge = this.allKnowledgeListFromDB.find(knowledge => { return knowledge.name == this.knowledgeName; });
      if (this.levelSkillRating && this.levelSkillRating == -100 || this.levelSkillRating == undefined) {
        levelSkill = "";
      } else {
        levelSkill = LevelSkill[this.levelSkillRating];
      }
      if (knowledge) {
        this.chosedCVKnowledgeList.push(new CV_knowledge(levelSkill, this.years, null, knowledge, this.levelSkillRating));
      } else {
        knowledge = new Knowledge(new Category("Andet", ""));
        this.chosedCVKnowledgeList.push(new CV_knowledge(levelSkill, this.years, this.knowledgeName.trim().toLowerCase(), knowledge, this.levelSkillRating))
      }

      this.allKnowledgeListFromDBTmp = this.allKnowledgeListFromDBTmp.filter(knowledge => { return knowledge.name != this.knowledgeName });
      this.searchGroup.get('knowledgeName')?.setValue("");
    }

  }

  getColorForCVKnowledge(cvKnowledgeToColor?: CV_knowledge, industries?: string) {
    if (this.chosedCVKnowledgeList.filter(cvKnowledgeSearched => {      
      return ((cvKnowledgeSearched.knowledge.name === cvKnowledgeToColor?.knowledge.name
        || cvKnowledgeSearched.note?.toLocaleLowerCase().trim() == cvKnowledgeToColor?.note?.toLocaleLowerCase().trim())
        && (
          (cvKnowledgeSearched?.levelSkillRated
            && cvKnowledgeToColor?.levelSkillRated 
            &&cvKnowledgeSearched?.levelSkillRated <= cvKnowledgeToColor?.levelSkillRated
        && cvKnowledgeSearched.years <= cvKnowledgeToColor.years
        )
         ||
          (cvKnowledgeSearched?.levelSkillRated
            && cvKnowledgeToColor?.levelSkillRated 
            && cvKnowledgeSearched?.levelSkillRated <= cvKnowledgeToColor?.levelSkillRated 
          && cvKnowledgeSearched?.years == 0)
          ||
          (cvKnowledgeSearched?.levelSkill == "" 
          && cvKnowledgeSearched?.years == 0 )
          ||
          (cvKnowledgeSearched?.levelSkill == "" 
          && cvKnowledgeToColor?.years
          && cvKnowledgeSearched.years <= cvKnowledgeToColor.years)
          )  
          && (cvKnowledgeSearched.knowledge.name === cvKnowledgeToColor?.knowledge.name
            || cvKnowledgeSearched.note?.toLocaleLowerCase().trim() == cvKnowledgeToColor?.note?.toLocaleLowerCase().trim())
          );
    }).length > 0
      && this.simpleCVsToBeSorted
      && this.colorsOn == true
    ) {
      return 'totalMatch';
    
    } else if ((this.chosedCVKnowledgeList.filter(cvKnowledgeSearched => {
      return (
        cvKnowledgeSearched.knowledge.name === cvKnowledgeToColor?.knowledge.name
        || cvKnowledgeSearched.note?.toLocaleLowerCase().trim() == cvKnowledgeToColor?.note?.toLocaleLowerCase().trim()
        );
    }).length > 0
      && this.simpleCVsToBeSorted
      && this.colorsOn == true)
      || (cvKnowledgeToColor?.knowledge.knowCategoryDTO.name == 'Industribrancher'
        && cvKnowledgeToColor?.knowledge.name == industries
        && this.simpleCVsToBeSorted
        && this.colorsOn == true)
    ) {
          if(cvKnowledgeToColor?.levelSkillRated 
            && (this.chosedCVKnowledgeList.filter(cvKnowledgeSearched => {
              return ( 
                cvKnowledgeToColor.levelSkillRated
                && cvKnowledgeSearched?.levelSkillRated
                && (
                  cvKnowledgeToColor?.levelSkillRated>=cvKnowledgeSearched?.levelSkillRated
                  || cvKnowledgeSearched.levelSkill == "" 
                  )
                && cvKnowledgeToColor?.knowledge.knowCategoryDTO.name != 'Industribrancher'
                && cvKnowledgeToColor?.years<cvKnowledgeSearched?.years
                );
            }).length > 0)){
              return 'levelMatch';
            }        
          if(cvKnowledgeToColor?.years 
            && (this.chosedCVKnowledgeList.filter( cvKnowledgeSearched =>{
              return (cvKnowledgeToColor?.years
                && cvKnowledgeSearched?.years
                && cvKnowledgeToColor.levelSkillRated
                && cvKnowledgeSearched?.levelSkillRated
                && cvKnowledgeToColor?.levelSkillRated<cvKnowledgeSearched?.levelSkillRated
                && cvKnowledgeToColor?.years>=cvKnowledgeSearched?.years
                && cvKnowledgeToColor?.knowledge.knowCategoryDTO.name != 'Industribrancher'
                );
          }).length > 0) 
          ){
            return 'yearsMatch';
          }
      return 'nameMatch';
    }
    return 'noMatch'
  }

  checkboxChecks(index:number){
    if(this.amountCVKnowledge >= index+1){
      let amountToUncheck:number = this.amountCVKnowledge-(index)
      this.amountCVKnowledge = this.amountCVKnowledge - amountToUncheck
    }else{
      this.amountCVKnowledge = index+1
    }
    console.log(this.amountCVKnowledge);
  }

  removeCVKnowledge(cvKnowledgeToRemove: CV_knowledge) {
    this.chosedCVKnowledgeList = this.chosedCVKnowledgeList.filter(cvKnowledge => { return cvKnowledge.knowledge.name != cvKnowledgeToRemove.knowledge.name });
    this.allKnowledgeListFromDBTmp.push(this.allKnowledgeListFromDB.filter(knowledge => { return knowledge.name == cvKnowledgeToRemove.knowledge.name })[0]);
    this.colorsOn = false;
  }

  scrollTo(htmlE: HTMLElement) {
    htmlE.scrollIntoView({
      block: 'start',
      behavior: 'smooth',
    });
  }

  showOriginalCV(cvChosed: SimpleCV) {
    if (cvChosed != undefined
      && cvChosed.consultantPhone
      && cvChosed.title) {
      const dialogConfig = new MatDialogConfig();
      dialogConfig.data = {
        error: this.error,
        cv: cvChosed
      };
      dialogConfig.width = '70%';
      dialogConfig.height = '90%';
      this.dialog.open(ShowSingleCVComponent, dialogConfig);
    }
  }

  getColorForLanguage(language:PractisedLanguage){
    if(language.name.trim().toLowerCase() == this.languageName?.trim().toLowerCase() 
      && this.simpleCVsToBeSorted
      && this.colorsOn == true){
      return 'totalMatch';
    }else {
      return 'noMatch';
    }
  }

}


