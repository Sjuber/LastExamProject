import { Component, OnInit } from '@angular/core';
import { User } from '../objects/user'
import { FormGroup } from '@angular/forms';
import { UsersService } from '../users.service';
import { Role } from '../objects/role';
import { RoleUser } from '../objects/roleUser';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { EditUserComponent } from './edit-user/edit-user.component';

@Component({
  selector: 'app-add-user',
  templateUrl: './get-users.component.html',
  styleUrls: ['./get-users.component.css']
})
export class GetUsersComponent implements OnInit {
  clickedRows = new Set<User>();
  userUpdateForm: FormGroup | undefined;
  toUpdateName: string | undefined;
  toUpdatePhone: string | undefined;
  toUpdateEmail: string | undefined;
  toUpdatetitle: string | undefined;
  toUpdateRoleTitles: FormGroup | undefined;
  errorMessage: string | undefined;
  currentUsers: User[] = [];
  currentRoles: Role[] = [];
  displayedColumns: string[] = ['name', 'email', 'phone', 'maxWeeklyHours'];
  userToUpdate: User | undefined;
  possibleRoles: Role[] = [];
  rolesFromDB:Role[] = [];
  possibleRolesTmp: Role[] = [];
  userRolesTmp: RoleUser[] = [];
  formRoleInfo: FormGroup | undefined;
  roleTitle: String | undefined;
  roleStartDate: Date | undefined;
  roleEndDate: Date | undefined;
  userOriginal: User |undefined;
  rolesUserHas:Role[] = []; 

  constructor(private userService: UsersService, private dialog: MatDialog) {
    
  }

  ngOnInit() {
    this.getCurrentUsers();
     this.getAllRoles();
  }

  getCurrentUsers() {
    this.userService.getUsers().subscribe(getResult => {
      this.currentUsers = getResult.reverse();
    });
  }

  removeClickedRow(user: User) {
    this.clickedRows.delete(user);
    if (user.email == this.userToUpdate?.email) {
      this.userToUpdate = undefined;
    }
  }
  

  openDialogUpdateUser(userToChange:User){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = { 
            user: userToChange,
            roles: this.rolesFromDB
          };
          dialogConfig.width = '50%';
          dialogConfig.height = '90%';
          let dialogRef = this.dialog.open(EditUserComponent, dialogConfig);
          
  }

  showUpdateForUser(user: User) {
    this.openDialogUpdateUser(user);

  }

  getAllRoles() {
    this.userService.getRolesTypes().subscribe(roles => {
      this.rolesFromDB = roles;
    })
  }
  
}
