import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, ReplaySubject } from 'rxjs';
import { CV } from './objects/cv';
import { Job } from './objects/job';
import { SimpleCV } from './objects/simpleCV';
import { User } from './objects/user';
import { BehaviorSubject } from 'rxjs';
import { CV_knowledge } from './objects/cv_knowledge';
import { Knowledge } from './objects/knowledge';
import { Url_credentials } from 'src/URL_Credentials';
import { PractisedLanguage } from './objects/practisedLanguage';
import { CourseCertification } from './objects/courseCertification';
import { Education } from './objects/education';
import { Project } from './objects/project';
import { SearchFeatures } from './objects/searchFeatures';

@Injectable({
  providedIn: 'root'
})
export class CvsService {

  constructor(private http:HttpClient) {}

  private url:Url_credentials = new Url_credentials();
  private baseURL= this.url.webserviceURL+'users/v1/cvs/';
  private cvURL:string= this.baseURL + 'cv';
  private cvsForUser:string = this.baseURL + 'user/';
  private cvsForUserPhone:string = this.baseURL + 'cv/user/';
  private cvsPublished:string = this.baseURL + 'published';
  private jobsByCV:string = this.baseURL + 'cv/jobs';
  private addJobToCV:string = this.baseURL + 'cv/job';
  private addPractisedLanguage:string = this.baseURL+ 'cv/practisedLanguage';
  private addProject:string = this.baseURL+'cv/project';
  private deleteJobForCV:string = this.addJobToCV;
  private cvToUpdate:string = this.cvURL;
  private getAllKnowledge:string = this.baseURL+'knowlegde';
  private addCVKnowledge:string = this.baseURL + 'cv/cv_knowledge';
  private addCourseCertificationURL:string = this.baseURL + 'cv/course_certification'
  private addEducationURL:string = this.baseURL + 'cv/education'
  private getCVID:string = this.baseURL+'cv_id';
  private deleteCVKnowledgeFromCV = this.addCVKnowledge;
  private updateCVKnowledges = this.addCVKnowledge+"s";
  private updateCoursesCertifications = this.baseURL + 'cv/courses_certifications';
  private updateEducations = this.baseURL + 'cv/educations';
  private updateJobs = this.baseURL + 'cv/jobs';
  private updateProjectsURL = this.baseURL + 'cv/projects';
  private updatePractisedLanguage = this.addPractisedLanguage;
  private simpleCVsByConsultant = this.baseURL + 'simple_cvs/not_draft/';
  private print_PDF_URL = this.baseURL + 'cvToPrint/';


  public cvToShow: BehaviorSubject<CV|undefined> = new BehaviorSubject<CV|undefined>(undefined);
  public cvToEdit: BehaviorSubject<CV|undefined> = new BehaviorSubject<CV|undefined>(undefined);
  public httpError: ReplaySubject<String> = new ReplaySubject<String>();
  
  
  print_CV_As_PDF(id:undefined|number):Observable<CV>{
    return this.http.get<CV>(this.print_PDF_URL + id).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error.text);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
      throw new HttpErrorResponse(error)
    }));
  }

  addOriginalCV(cvInput:CV): Observable<CV>{
    return this.http.post<CV>(this.cvURL,cvInput).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error.text);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
  }

  getSimpleCVsNotInDraftModeByAConsultant(consultant:User):Observable<SimpleCV[]>{
    return this.http.get<SimpleCV[]>(this.simpleCVsByConsultant+consultant.phone).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error.text);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
  }

  getAllSimpleCVsPublished(pageSite?:number,amount?:number):Observable<SimpleCV[]>{
    let queryParams = new HttpParams();
    if(pageSite){
       queryParams = queryParams.set('page',pageSite);
    }if(amount){
       queryParams = queryParams.set('size',amount);
    }
    return this.http.post<SimpleCV[]>(this.cvsPublished+"/paged", [],{params:queryParams}).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error.text);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
  }
  getAllSimpleCVsPublishedBySearchFeatures(prioritiesNamesList:string[], cv_knowledgeList?:CV_knowledge[],
    language?:PractisedLanguage, notBookedHours?:number, companyWorkedFor?:string,
    pageSite?:number,amount?:number, mostContainCVKnowledgeAmountLeastInCV?:number):Observable<SimpleCV[]>{

    let queryParams = new HttpParams();

    if(pageSite){
      queryParams = queryParams.set('page',pageSite);
    }if(amount){
      queryParams = queryParams.set('size',amount);
    }
    if(!mostContainCVKnowledgeAmountLeastInCV){
      mostContainCVKnowledgeAmountLeastInCV = 0;
    }
    let searchedFeatures: SearchFeatures = new SearchFeatures(prioritiesNamesList, mostContainCVKnowledgeAmountLeastInCV, cv_knowledgeList,language,notBookedHours, companyWorkedFor);


    return this.http.post<SimpleCV[]>(
      this.cvsPublished+"/paged/by_search", searchedFeatures,
      {params:queryParams}).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
      //Do not push to httpError
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
  }


  getCVIDForCV(cv:CV):Observable<Number>{
    return this.http.post<Number>(this.getCVID, cv).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error.text);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
  }

  getUserCVs(user:User): Observable<CV[]>{
    return this.http.get<CV[]>(this.cvsForUser+user.phone).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error.text);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
  }

  getAllCVsNotInDraftmode(): Observable<SimpleCV[]>{
    return this.http.get<SimpleCV[]>(this.cvsPublished).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error.text);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
  }

  getCVByUserPhoneAndSimpleCV(simpleCV:SimpleCV, phone:String):Observable<CV>{
    return this.http.post<CV>(this.cvsForUserPhone+phone, simpleCV).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error.text);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
  }
  getAllJobsFromCV(cv:CV):Observable<Job[]>{
    return this.http.post<Job[]>(this.jobsByCV, cv).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error.text);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
  }
  getAllKnowlegdeFromDB():Observable<Knowledge[]>{
    return this.http.get<Knowledge[]>(this.getAllKnowledge).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error.text);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
  }

  addJobForCV(cvID:Number,job:Job):Observable<Job[]>{
    return this.http.post<Job[]>(this.addJobToCV+"/"+cvID, job).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error.text);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
  }

  addLanguage(cvID:Number, language:PractisedLanguage):Observable<PractisedLanguage>{
    return this.http.post<PractisedLanguage>(this.addPractisedLanguage+"/"+cvID, language).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error.text);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
  }

  addProjectForCV(cvID:Number, project:Project):Observable<Project>{
    return this.http.post<Project>(this.addProject+"/"+cvID, project).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error.text);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
  }

  deleteJob(job:Job){
    return this.http.delete<Job[]>(this.deleteJobForCV, {body:job}).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error.text);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
  }
  deleteCVKnowledge(cvID:Number, cvKnowledge:CV_knowledge){
    return this.http.delete<CV_knowledge[]>(this.deleteCVKnowledgeFromCV+"/"+cvID, {body:cvKnowledge}).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error.text);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
  }

  deleteCV(cv:CV){
    return this.http.delete<CV>(this.cvURL, {body:cv}).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error.text);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
  }

  updateCV(cvOriginal:CV, cvEdited:CV):Observable<CV>{
    let cvs:CV[] = [];
    cvs.push(cvOriginal);
    cvs.push(cvEdited);

    return this.http.put<CV>(this.cvToUpdate, cvs).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error.text);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
  }

  updateProjects(cvID:Number, newProjects:Project[]):Observable<Project[]>{
    return this.http.put<Project[]>(this.updateProjectsURL+"/"+cvID,newProjects).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error.text);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
  }

  updateJobsForCV(cvID:Number, newJobs:Job[]):Observable<Job[]>{
    return this.http.put<Job[]>(this.updateJobs+"/"+cvID,newJobs).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error.text);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
    }

  updateLanguages(cvID:Number,newLanguages:PractisedLanguage[]):Observable<PractisedLanguage[]>{
    return this.http.put<PractisedLanguage[]>(this.updatePractisedLanguage+"/"+cvID,newLanguages).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error.text);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
  }

  addCVKnowledgeForCV(cv_id:Number,cv_knowlegde: CV_knowledge):Observable<CV_knowledge[]>{
    return this.http.post<CV_knowledge[]>(this.addCVKnowledge+"/"+cv_id, cv_knowlegde).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
  }

  addCourseCertification(cvID: Number, courseCertification:CourseCertification | undefined):Observable<CourseCertification[]> {
    return this.http.post<CourseCertification[]>(this.addCourseCertificationURL+"/"+cvID, courseCertification).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error.text);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
  }

  addEducation(cvID: Number, education:Education | undefined):Observable<Education[]> {
    return this.http.post<Education[]>(this.addEducationURL+"/"+cvID, education).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error.text);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
  }

  updateCVKnowledgeForCV(cvID:Number, newCVKnowlegdeList:CV_knowledge[]):Observable<CV_knowledge[]>{
    return this.http.put<CV_knowledge[]>(this.updateCVKnowledges+"/"+cvID,newCVKnowlegdeList).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
  }

  updateCoursesCertificationsForCV(cvID:Number, courseCertification:CourseCertification[]):Observable<CourseCertification[]>{
    return this.http.put<CourseCertification[]>(this.updateCoursesCertifications+"/"+cvID,courseCertification).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
  }

  updateEducationsForCV(cvID:Number, education:Education[]):Observable<Education[]>{
    return this.http.put<Education[]>(this.updateEducations+"/"+cvID,education).pipe(catchError((error,src) => {
      console.log('Caught in CatchError. Throwing error')
      if(error.statusText=="OK"){
        this.httpError.next(error.error);
      }else{
        this.httpError.next(error.status + " "+ error.statusText);
      }
      console.log(error);
        throw new HttpErrorResponse(error)
      }));
  }
}
