import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormArray } from '@angular/forms';
import { Router } from '@angular/router';
import { ErrorsComponent } from '../errors/errors.component';
import { Role } from '../objects/role';
import { RoleUser } from '../objects/roleUser';
import { User } from '../objects/user';
import { UsersService } from '../users.service';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';


@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent implements OnInit {
  userAddForm: FormGroup;
  possibleRoles: Role[] = [];
  currentName: string | undefined;
  currentPhone: string | undefined;
  currentEmail: string | undefined;
  title: string | undefined;
  currentRoleTitles: string[] = [];
  currentmaxWeeklyHours: number | undefined;
  userRoleList: RoleUser[] = [];
  errormaxWeeklyHoursMessage = "";
  errorName = "";
  errorPhone = "";
  errorEmail = "";
  errorRoles = "";

  constructor(private userService: UsersService, private formBuilder: FormBuilder, private router: Router, private dialog: MatDialog) {
    this.userAddForm = new FormGroup({
      name: new FormControl(''),
      phone: new FormControl(''),
      email: new FormControl(''),
      maxWeeklyHours: new FormControl(''),
      roleTitles: new FormArray([])
    });
    this.userAddForm.valueChanges.subscribe(form => {
      this.currentName = form.name;
      this.currentPhone = form.phone;
      this.currentEmail = form.email;
      this.currentmaxWeeklyHours = form.maxWeeklyHours;
      this.currentRoleTitles = this.titleGroup();
    })

    this.getAllPossibleRoles();
  }

  get roleTitlesForm(): any {
    return this.userAddForm.get('roleTitles') as FormArray;
  }

  titleGroup(): any {
    return new FormGroup({
      title: new FormControl(''),
    });
  }

  ngOnInit() {
  }

  onSubmit() {
    this.errorName = "";
    this.errorPhone = "";
    this.errorEmail = "";
    this.errorRoles = "";
    this.errormaxWeeklyHoursMessage = "";
    if (this.userAddForm.value.roleTitles) {
              
      for (let index = 0; index < this.userAddForm.value.roleTitles.length; index++) {
        this.userRoleList.push(new RoleUser(new Date(), null, new Role(this.userAddForm.value.roleTitles[index])))
      }

      if (this.currentmaxWeeklyHours != undefined && Number(this.currentmaxWeeklyHours) <= 0) {
        this.errormaxWeeklyHoursMessage = "Du kan ikke arbejde mindre end 0 timer om ugen";
        this.openDialogError(this.errormaxWeeklyHoursMessage);
        this.scrollToError(<HTMLInputElement>document.getElementById("maxWeeklyHours"));

      } 
        let error = "Alle brugerdetaljer skal udfyldes";
        if (this.currentName!=undefined && (/\d/.test(this.currentName) || this.currentName == "" || /[@]/.test(this.currentName))) {
          this.errorName = "Ugyldigt navn";
          this.openDialogError(this.errorName);
          this.scrollToError(<HTMLElement>document.getElementById("name"));
        }
        else if(!this.currentName){
          this.errorName=error;
        }
        if(!this.currentPhone){
          this.errorPhone=error;
          this.scrollToError(<HTMLElement>document.getElementById("phone"));
        } else if (this.currentPhone != undefined 
          && 
          Number(this.currentPhone).toLocaleString() == ""
          ) {
          this.errorPhone = "Telefon nummer kan kun indeholde tal";
          this.scrollToError(<HTMLElement>document.getElementById("phone"));
        } 
        if (this.currentEmail != undefined && (!(/^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/.test(this.currentEmail))) ){
          this.errorEmail = "Dette er ikke en gyldig e-mail";
          this.openDialogError(this.errorEmail);
          this.scrollToError(<HTMLElement>document.getElementById("email"));
        } else if(!this.currentEmail){
          this.errorEmail=error;
        }
        if(!this.currentmaxWeeklyHours){
          this.errormaxWeeklyHoursMessage=error;
        }
        
        if(this.userAddForm.value.roleTitles.length<=0){
          this.errorRoles = "Du skal vælge mindst en rolle for brugeren";
           this.userRoleList.splice(0);
           this.openDialogError(this.errorRoles);
           this.scrollToError(<HTMLInputElement>document.getElementById("roles"));
      }
      if(this.errorName != "" &&
      this.errorPhone != "" &&
      this.errorEmail != "" &&
      this.errorRoles != "")
      {
        this.openDialogError(error);
      }
        if(this.errorName == ""&& this.errorPhone == "" && this.errorEmail == "" && this.errorRoles == "" &&
        this.currentName && this.currentPhone && this.currentEmail && this.currentmaxWeeklyHours && this.currentmaxWeeklyHours > 0 && this.userRoleList.length > 0) {
          const userObeservable = this.userService.addUser(new User(this.currentName, this.currentPhone, this.currentEmail, this.userRoleList, this.currentmaxWeeklyHours));
          userObeservable.subscribe({
            next: (user) => {
              this.userRoleList.splice(0);
              this.router.navigate(['/get_users'])
            },
            error: (e) => {
              console.error(e);
              this.userRoleList.splice(0);
              this.openDialogError(e.error);
  
            },
            complete: () => {
              console.info('complete')
              this.userRoleList.splice(0);
            }
  
          });
        }   this.userRoleList.splice(0);   
  }
 
}

  openDialogError(errorMessage: string) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      error: errorMessage
    };
    dialogConfig.width = '40%';
    dialogConfig.height = '40%';
    let dialogRef = this.dialog.open(ErrorsComponent, dialogConfig);

  }

  getAllPossibleRoles() {
    this.userService.getRolesTypes().subscribe(roles => {
      this.possibleRoles = roles;
    })

  }

  onCheckboxChange(e: any) {
    const roleTitles: FormArray = this.userAddForm.get('roleTitles') as FormArray;

    if (e.target.checked) {
      roleTitles.push(new FormControl(e.target.value));
    } else {
      const rolteToRemove = roleTitles.controls.findIndex(x => x.value == e.target.value);
      roleTitles.removeAt(rolteToRemove);
    }
  }

  scrollToError(error: HTMLElement) {
    error.scrollIntoView({
      block: 'center',
      behavior: 'smooth',
    });
  }

}



