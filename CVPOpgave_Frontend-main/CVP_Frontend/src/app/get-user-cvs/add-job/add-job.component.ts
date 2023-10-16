import { Component, Inject, OnInit } from '@angular/core';
import { FormArray, FormControl, FormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CvsService } from 'src/app/cvs.service';
import { Company } from 'src/app/objects/company';
import { CV } from 'src/app/objects/cv';
import { Job } from 'src/app/objects/job';
import * as moment from 'moment';

@Component({
  selector: 'app-add-job',
  templateUrl: './add-job.component.html',
  styleUrls: ['./add-job.component.css']
})
export class AddJobComponent implements OnInit {
  browserDefault = navigator;
  cvForJobAdding: CV | undefined;
  cvAddJobForm: FormGroup;
  jobs: Job[] = [];

  currentTitle: String | undefined;
  currentStartDate: String | undefined;
  currentEndDate: String | undefined | null;
  currentCompanyName: string | undefined;

  titleError: String | undefined;
  dateError: String | undefined;
  companyNameError: String | undefined;

  constructor(private dialogRef: MatDialogRef<AddJobComponent>, @Inject(MAT_DIALOG_DATA) public data: any, private cvsService: CvsService) {
    this.jobs = [];
    this.cvForJobAdding = undefined;
    this.cvForJobAdding = data.cv;
    this.cvAddJobForm = new FormGroup({
      title: new FormControl(''),
      startDate: new FormControl(''),
      endDate: new FormControl(''), 
      companyName: new FormControl('')
    });
    this.cvAddJobForm.valueChanges.subscribe(form => {
      this.currentCompanyName = form.companyName;
      this.currentStartDate = form.startDate;
      this.currentEndDate = form.endDate;
      this.currentTitle = form.title;
    })
    if(this.cvForJobAdding){
    this.sortJobs(this.cvForJobAdding);
    }
  }

  ngOnInit(): void {
  }

  close() {
     this.dialogRef.close();
  }

  sortJobs(cv:CV){
    this.jobs = cv.jobs.sort(function(a:Job, b:Job){
      return new Date(b.startDate).getTime() - new Date(a.startDate).getTime();      
    });
  }

  onSubmitAddJob() {
    let job: Job | undefined = undefined;
    let company: Company;
    let endDate: Date | null = null;

    this.companyNameError="";
    this.dateError="";
    this.titleError="";

    if (this.currentCompanyName 
      && this.currentStartDate
      && this.currentTitle
      && this.cvForJobAdding
      && this.currentCompanyName != ""
      && this.currentStartDate != ""
      && this.currentTitle != ""
      ) {

      company = new Company(this.currentCompanyName); // TODO Change this from a list of Companies (in database)
    
      if(this.currentEndDate != null && this.currentEndDate != undefined && this.currentEndDate != ""){
        endDate = new Date(this.currentEndDate+"-01");
        }
      
      job = new Job(null,new Date(this.currentStartDate+"-01"), endDate, this.currentTitle, company);
      if(job.endDate && job.startDate.getTime()>job.endDate?.getTime()){
        this.dateError = "Du kan ikke angive en startsmåned efter slutmåned";
      }else{
        this.cvsService.getCVIDForCV(this.cvForJobAdding).subscribe((cvID)=>{
          if(job){
          this.cvsService.addJobForCV(cvID,job).subscribe({
            next: (jobsDB) => {
              this.jobs = jobsDB;
              if(this.cvForJobAdding){
              this.cvForJobAdding.jobs = this.jobs;
            }
              this.cvsService.cvToShow.next(this.cvForJobAdding);
            
              if(endDate){
              this.cvAddJobForm.patchValue({
                title: "",
                startDate: this.currentEndDate,
                endDate: "", 
                companyName: ""
                });
              }
             },
             error: (e) => {
             console.error(e);
             }
           });
          }
        });
      }
    }else{
      if(this.currentTitle==""|| this.currentTitle==undefined){
        this.titleError="Du skal angive en job titel";
      }
      if(this.currentCompanyName==""|| this.currentCompanyName==undefined){
        this.companyNameError ="Du skal angive det firma du arbejde for";
      }
      if(this.currentStartDate==""|| this.currentStartDate==undefined){
        this.dateError="Du skal angive en startmåned for ansættelsen";
      }
  }

  }

  formattingMonth(dateValue:any){
    moment.locale(this.browserDefault.language);
    return moment(dateValue).format("MMMM-yyyy");
  }
  

}
