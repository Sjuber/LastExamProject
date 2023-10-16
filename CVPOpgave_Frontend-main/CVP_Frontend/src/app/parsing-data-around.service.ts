import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { User } from './objects/user';

@Injectable({
  providedIn: 'root'
})
export class ParsingDataAroundService {

  private loggedInUser = new Subject<User>();

  loggedInUser$ = this.loggedInUser.asObservable();

  setLoggedInUser(user:User){
    this.loggedInUser.next(user);
  }
}
