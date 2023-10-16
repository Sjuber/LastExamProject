import { Component, Inject, Input, OnInit } from '@angular/core';
import { FormArray, FormControl, FormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ErrorsComponent } from 'src/app/errors/errors.component';
import { Role } from 'src/app/objects/role';
import { RoleUser } from 'src/app/objects/roleUser';
import { User } from 'src/app/objects/user';
import { UsersService } from 'src/app/users.service';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css']
})
export class EditUserComponent implements OnInit {
  clickedRows = new Set<User>();
  userUpdateForm: FormGroup | undefined;
  toUpdateName: string | undefined;
  toUpdatePhone: string | undefined;
  toUpdateEmail: string | undefined;
  toUpdatetitle: string | undefined;
  toUpdateRoleTitles: FormGroup | undefined;
  userRoleList: RoleUser[] = [];
  currentUsers: User[] = [];
  currentRoles: Role[] = [];
  userToUpdate: User | undefined;
  possibleRoles: Role[] = [];
  rolesFromDB: Role[] = [];
  possibleRolesTmp: Role[] = [];
  userRolesTmp: RoleUser[] = [];
  formRoleInfo: FormGroup | undefined;
  roleTitle: String | undefined;
  roleStartDate: Date | undefined;
  roleEndDate: Date | undefined;
  userOriginal: User | undefined;
  rolesUserHas: Role[] = [];
  toUpdateMaxWeeklyHours: number | undefined;

  errorMessage: string | undefined;
  errormaxWeeklyHoursMessage = "";
  errorName = "";
  errorPhone = "";
  errorEmail = "";
  errorRoles = "";

  @Input() user: User | undefined;
  @Input() roles: Role[] | undefined;

  constructor(private userService: UsersService, private dialogRef: MatDialogRef<ErrorsComponent>, @Inject(MAT_DIALOG_DATA) data: any) {
    this.user = data.user;
    this.roles = data.roles;
  }

  removeRoleForCurrentUser(indexUserRole: number, e: any) {
    if (this.userToUpdate != undefined && this.roleTitles.length > 1) {
      if (e.target.checked) {
        this.possibleRoles.push(new Role(this.roleTitles.controls[indexUserRole].value.title));
        this.roleTitles.removeAt(indexUserRole);
      }
    } else if (this.errorRoles && !e.target.checked) {
      this.errorRoles = "";
    }
    else {
      this.errorRoles = "Brugeren skal mindst have en rolle";
    }
  }

  addCurrentUserRole(indexUserRole: number, e: any) {
    if (e.target.checked) {
      if (this.userToUpdate != undefined) {
        const userRoleAdded = new RoleUser(new Date(), null, this.possibleRoles[indexUserRole]);
        this.addRoleInfoGroup(userRoleAdded);
        this.possibleRolesTmp = this.possibleRoles.filter((role) => {
          return role != this.possibleRoles[indexUserRole];
        });
        this.possibleRoles = this.possibleRolesTmp;
      }
      if (this.errorMessage != null || this.errorMessage != undefined) {
        this.errorMessage = "";
      }
    }
  }

  errorHandlingFromUIInput(): boolean {
    let failed = true;
    this.toUpdateName = this.userUpdateForm?.value.name;
    this.toUpdatePhone = this.userUpdateForm?.value.phone;
    this.toUpdateEmail = this.userUpdateForm?.value.email;
    this.toUpdateMaxWeeklyHours = this.userUpdateForm?.value.maxWeeklyHours;

    if (this.userUpdateForm != undefined && this.userToUpdate != undefined) {
      if (this.toUpdateName != undefined) {
        if (/\d/.test(this.toUpdateName) || this.toUpdateName == "" || (/[@]/.test(this.toUpdateName))) {
          this.errorName = "Ugyldigt navn";
          this.scrollToError(<HTMLElement>document.getElementById("name"));
          return failed;
        }

      }else{
        this.errorName = "Personen har brug for et navn";
        this.scrollToError(<HTMLElement>document.getElementById("name"));
        return failed;
      }

      if (this.toUpdatePhone != undefined && /[a-zA-Z]/.test(this.toUpdatePhone)) {
        this.errorPhone = "Telefon nummer kan kun indeholde tal";
        this.scrollToError(<HTMLElement>document.getElementById("phone"));
        return failed;
      } else if(this.toUpdatePhone == undefined || this.toUpdatePhone == ""){
        this.errorPhone = "Personen har brug for et telefonnummer";
        this.scrollToError(<HTMLElement>document.getElementById("phone"));
        return failed;
      }

      if (this.toUpdateEmail != undefined && (!(/[@]/.test(this.toUpdateEmail))) ){
        this.errorEmail = "Dette er ikke en gyldig e-mail";
        this.scrollToError(<HTMLElement>document.getElementById("email"));
        return failed;
      } else if(this.toUpdateEmail == undefined){
        this.errorEmail = "Personen har brug for en e-mail";
        this.scrollToError(<HTMLElement>document.getElementById("email"));
        return failed;
      }

      if (this.toUpdateMaxWeeklyHours != undefined && this.toUpdateMaxWeeklyHours <= 0) {
        this.errormaxWeeklyHoursMessage = "Timeantallet skal være større end 0";
        this.scrollToError(<HTMLElement>document.getElementById("maxWeeklyHours"));
        return failed;
      } else if(this.toUpdateMaxWeeklyHours == undefined){
        this.errormaxWeeklyHoursMessage = "Personen har brug arbejdstimer";
        this.scrollToError(<HTMLElement>document.getElementById("maxWeeklyHours"));
        return failed;
      }

      if (this.roleTitles == undefined || this.roleTitles.length < 1) {
        this.errorRoles = "Du skal mindst vælge en rolle";
        this.scrollToError(<HTMLElement>document.getElementById("addRole"));
        return failed;
      }
      failed = false;
    }
    return failed;
  }

  onSubmitUpdateUser() {
    this.errormaxWeeklyHoursMessage = "";
    this.errorName = "";
    this.errorPhone = "";
    this.errorEmail = "";
    this.errorRoles = "";

    let failed = this.errorHandlingFromUIInput();

    if (failed==false) {
      if (this.toUpdateName != undefined &&
        this.toUpdatePhone != undefined &&
        this.toUpdateEmail != undefined &&
        this.toUpdateMaxWeeklyHours != undefined &&
        this.userOriginal != undefined &&
        this.userToUpdate != undefined) {

        this.userToUpdate.name = this.toUpdateName;
        this.userToUpdate.phone = this.toUpdatePhone;
        this.userToUpdate.email = this.toUpdateEmail;
        this.userToUpdate.maxWeeklyHours = this.toUpdateMaxWeeklyHours;

        if (this.roleTitles != undefined || this.roleTitles.length >= 1) {
          let userRolesTmp: RoleUser[] = [];
          for (let roleInfo of this.roleTitles.controls) {
            let endDate = null;
            if (roleInfo.value.endDate) {
              endDate = new Date(roleInfo.value.endDate);
            }

            let userRole: RoleUser = new RoleUser(new Date(roleInfo.value.startDate), endDate, new Role(roleInfo.value.title));
            userRolesTmp.push(userRole);
          }
          this.userToUpdate.userRoleDTOs = userRolesTmp;

          this.userService.updateUser(this.userToUpdate, this.userOriginal).subscribe(user => {
            console.log(user);
          });
          this.possibleRoles = this.usersNotSelectedRoles(this.userToUpdate);
          this.userToUpdate = undefined;
        this.close();
      }
      }
    }
  }


  get roleTitles(): any {
    if (this.userUpdateForm != undefined)
      return this.userUpdateForm.get('roleTitles') as FormArray;
  }

  setAllUsersCurrentRoles() {
    if (this.userUpdateForm != undefined) {
      const roleTitles: FormArray = this.userUpdateForm.get('roleTitles') as FormArray;
      this.userToUpdate?.userRoleDTOs.forEach(userRoleDTO => {
        roleTitles.push(new FormGroup({
          title: new FormControl(userRoleDTO.roleDTO.title),
          startDate: new FormControl(userRoleDTO.startDate),
          endDate: new FormControl(userRoleDTO.endDate)
        }));
      })
    }
  }

  formatDateToString(date: any): String {
    if (date != null) {
      const tmpDate = new Date(date);
      const finalYear = tmpDate.getUTCFullYear();
      let finalDay = "";
      let finalMonth = "";
      const month = tmpDate.getUTCMonth() + 1;
      const day = tmpDate.getUTCDate();

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

      return finalYear + "-" + finalMonth + "-" + finalDay;
    } else {
      return "";
    }
  }

  addRoleInfoGroup(userRoleDTO: RoleUser) {
    this.roleTitles.push(new FormGroup({
      title: new FormControl(userRoleDTO.roleDTO.title),
      startDate: new FormControl(this.formatDateToString(userRoleDTO.startDate)),
      endDate: new FormControl(this.formatDateToString(userRoleDTO.endDate))
    }));
  }


  ngOnInit(): void {
    this.userOriginal = Object.assign({}, this.user);
    this.userToUpdate = this.user;
    this.possibleRoles = [];

    if (this.userToUpdate != undefined && this.possibleRoles != undefined) {


      this.userUpdateForm = new FormGroup({
        name: new FormControl(this.userToUpdate.name),
        phone: new FormControl(this.userToUpdate.phone),
        email: new FormControl(this.userToUpdate.email),
        roleTitles: new FormArray([]),
        maxWeeklyHours: new FormControl(this.userToUpdate.maxWeeklyHours)
      });
      this.userToUpdate.userRoleDTOs.forEach(userRole => {
        this.addRoleInfoGroup(userRole);
      })

      this.userUpdateForm.valueChanges.subscribe(form => {
        this.toUpdateName = form.name;
        this.toUpdatePhone = String(form.phone);
        this.toUpdateEmail = form.email;
        this.toUpdateRoleTitles = this.formRoleInfo;
        this.toUpdateMaxWeeklyHours = form.maxWeeklyHours;
      });

      if (this.formRoleInfo != undefined) {
        this.formRoleInfo.valueChanges.subscribe(form => {
          this.roleTitle = form.title;
          this.roleStartDate = form.startDate;
          this.roleEndDate = form.endDate;

        });
      }
      this.possibleRoles = this.usersNotSelectedRoles(this.userToUpdate);

    }
  }

  usersNotSelectedRoles(user: User) {
    let rolesToSelect: Role[] = [];
    let tmpList: Role[] = [];
    if (this.roles != undefined) {
      tmpList = rolesToSelect.concat(this.roles);

      if (user != undefined) {
        console.log(user);
        console.log(tmpList);

        for (let userRole of user.userRoleDTOs) {
          tmpList.forEach(role => {
            if (role.title == userRole.roleDTO.title) {
              let i = tmpList.indexOf(role);
              let tmp = tmpList.splice(i, 1);
            }
          })
        }
      }
    }
    return tmpList;
  }

  scrollToError(error: HTMLElement) {

    error.scrollIntoView({
      block:"center",
      behavior: 'smooth',
    });
  }

  close() {
    this.dialogRef.close();
  }

}
