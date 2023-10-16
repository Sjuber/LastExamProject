import { Component, OnInit} from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { User } from '../objects/user';
import { UsersService } from '../users.service';
import { ActivatedRoute,  Router } from '@angular/router';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginCredentialsForm:FormGroup;
  currentUser:User |  undefined;
  loggedIn:boolean = false;
  loginError:string;
  

  constructor(private userService: UsersService, private router:Router, private activedRoute:ActivatedRoute) {
    this.loginError = "";
    this.loginCredentialsForm = new FormGroup ({
      email: new FormControl(''),
      password: new FormControl('')
  });
}

  ngOnInit(): void {
    
  } 

  onSubmit(){
    this.loginError = "";
    let email:string = "";
    email = this.loginCredentialsForm.value.email
    if(this.loginCredentialsForm.value.email != undefined && this.loginCredentialsForm.value.password!= undefined){
    this.userService.postLoginRequest(email.trim(),this.loginCredentialsForm.value.password).subscribe({  
      next: (getResult) => { 
      this.currentUser = getResult;
             if(this.currentUser != undefined){
              this.router.navigate(['/dashboard/'], {queryParams:{phone:getResult.phone}});
             } else {
              this.currentUser = undefined;
             }
           
     },
     error: (e) => {
      this.loginError = "Login lykkedes ikke"
      console.error(e);
    }
  }
     )    
}
    //TODO - if login failed throw error at let user try again
    //TODO - close login window if click is without the box and after login is well done
    //TODO - keeped login for a sudden time?
  }


}
