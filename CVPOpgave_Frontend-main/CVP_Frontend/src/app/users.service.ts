import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { BehaviorSubject, catchError, Observable, throwError} from 'rxjs';
import { User } from './objects/user';
import { Role } from './objects/role';
import { Url_credentials } from 'src/URL_Credentials';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  constructor(private http:HttpClient) {}

  private url:Url_credentials = new Url_credentials();
  private baseURL= this.url.webserviceURL;
  private userURL:string= this.baseURL + 'users/v1/users';
  private userByPhoneURL:string= this.baseURL + 'users/v1/user/';
  private rolesURL:string= this.baseURL + 'users/v1/roles';
  private verifyLoginURL:string = this.baseURL + 'users/v1/login';
  private createUserURl:string = this.baseURL + 'users/v1/user';
  private updateUserURl:string = this.baseURL + 'users/v1/user/';

  public httpError: BehaviorSubject<String> = new BehaviorSubject<String>("");


  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.userURL).pipe(catchError((error,src) => {
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

  getUserByPhone(phone:number): Observable<User> {
    return this.http.get<User>(this.userByPhoneURL+phone).pipe(catchError((error,src) => {
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


  getRolesTypes(): Observable<Role[]> {
    return this.http.get<Role[]>(this.rolesURL).pipe(catchError((error,src) => {
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

  updateUser(userEdtied:User, userOriginal:User):Observable<User>{
    return this.http.put<User>(this.updateUserURl+userOriginal.phone,userEdtied).pipe(catchError((error,src) => {
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

  postLoginRequest(email:string, password:string): Observable<User>{
    let loginRequestedAttr: String[] = [];
    loginRequestedAttr.push(email);
    loginRequestedAttr.push(password);
    
    return this.http.post<User>(this.verifyLoginURL,loginRequestedAttr).pipe(catchError((error,src) => {
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

  addUser(userInput:User): Observable<User>{
    return this.http.post<User>(this.createUserURl,userInput).pipe(catchError((error,src) => {
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

    
}
