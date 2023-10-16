import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DashboardComponent} from './dashboard/dashboard.component';
import { LoginComponent } from './login/login.component';
import { AddUserComponent } from './add-user/add-user.component';
import { GetUsersComponent } from './get-users/get-users.component';
import { UsersProfileStartsiteComponent } from './users-profile-startsite/users-profile-startsite.component';
import { SortingSearchForCvsComponent } from './sorting-search-for-cvs/sorting-search-for-cvs.component';


const routes: Routes = [
    { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
    { path: 'dashboard', component: DashboardComponent},
    { path: 'login', component: LoginComponent},
    { path: 'add_user', component: AddUserComponent},
    { path: 'profile', component: UsersProfileStartsiteComponent},
    { path: 'get_users', component: GetUsersComponent},
    { path: 'search_cvs', component: SortingSearchForCvsComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {

}
