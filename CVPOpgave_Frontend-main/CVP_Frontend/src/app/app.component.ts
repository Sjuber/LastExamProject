import { Component } from '@angular/core';
import { UsersService } from './users.service';
import { User } from './objects/user';
import { ActivatedRoute, Router } from '@angular/router';
import { ParsingDataAroundService } from './parsing-data-around.service';
import { CvsService } from './cvs.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'CVP_Frontend';
  isAdmin: boolean = false;
  isSales: boolean = false;
  isConsultant: boolean = false;
  loggedIn: boolean|undefined;
  userName:string | undefined;
  userLoggedIn :User|undefined;
  serverError:String;
  
  
  constructor(private userService:UsersService, private router:Router, 
    private activatedroute:ActivatedRoute, private parsingDataAroundService:ParsingDataAroundService, 
    private cvService:CvsService){
    this.userName = "";
    this.loggedIn = false;
    this.serverError = "";
    this.cvService.httpError.subscribe((error)=>{
      this.serverError = error;
      if(this.serverError!=null && this.serverError !=""){
      this.scrollToError(<HTMLElement>document.getElementById("httpError"));  
    }
    });
    
    this.userService.httpError.subscribe((error)=>{
      this.serverError = error;
      if(this.serverError!=null && this.serverError !=""){
      this.scrollToError(<HTMLElement>document.getElementById("httpError"));
      }
    });

    if(localStorage.getItem("phone")!=null){
       userService.getUserByPhone(Number(localStorage.getItem("phone"))).subscribe({
        next: (user) => {
          this.userLoggedIn = user;
          history.state.user = this.userLoggedIn;
        this.userLoggedIn?.userRoleDTOs.forEach(userRole =>{
        if(userRole.roleDTO.title == "Administrator"){
          this.isAdmin = true;
        }
        if(userRole.roleDTO.title == "Konsulent"){
          this.isConsultant = true;  
        }  
        if(userRole.roleDTO.title == "Sælger"){
          this.isSales = true;
        }
        
        this.userName = this.userLoggedIn?.name;
      });
    },
        error: (e) => {
          console.error(e);
        }
      });
      this.loggedIn = true;
      
    }else{
   this.activatedroute.queryParams
      .subscribe(params => { 
        if(params['phone'] != null){
          this.userService.getUserByPhone(Number(params['phone'])).subscribe(user =>{
            this.userLoggedIn = user;
            this.parsingDataAroundService.setLoggedInUser(user);
            this.userName = this.userLoggedIn?.name;
          this.userLoggedIn?.userRoleDTOs.forEach(userRole =>{
      if(userRole.roleDTO.title == "Administrator"){
        this.isAdmin = true;
      }
      if(userRole.roleDTO.title == "Konsulent"){
        this.isConsultant = true;
      }
      if(userRole.roleDTO.title == "Sælger"){
        this.isSales = true;
      }
      
    })
        this.loggedIn =true;
      localStorage.setItem("phone", params['phone']);
  });
    }else if(params['phone'] != null && this.loggedIn == false){
      //TODO Error to occur from here 
    }         
  });
 }
  
  }
 logOut(){
  window.localStorage.removeItem("phone");
  this.loggedIn = false;
  this.isAdmin = false;
  this.isConsultant = false;
  this.isSales = false;
  this.userName = "";
  this.router.navigate(['/dashboard/'], {queryParams:{phone:null}});
  window.location.href = '';
}

scrollToError(error: HTMLElement) {
  error.scrollIntoView({
    block:"center",
    behavior: 'smooth',
  });
}

}
