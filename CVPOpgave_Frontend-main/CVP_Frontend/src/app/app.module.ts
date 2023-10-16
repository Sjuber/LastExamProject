import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { NgSelectModule } from '@ng-select/ng-select';
import { ReactiveFormsModule,FormsModule } from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatTableModule } from '@angular/material/table';
import { MatSliderModule } from '@angular/material/slider';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { LoginComponent } from './login/login.component';
import { AddUserComponent } from './add-user/add-user.component';
import { GetUsersComponent } from './get-users/get-users.component';
import { CreateSimpleCvComponent } from './create-simple-cv/create-simple-cv.component';
import { UsersProfileStartsiteComponent } from './users-profile-startsite/users-profile-startsite.component';
import { ParsingDataAroundService } from './parsing-data-around.service';
import { GetUserCVsComponent } from './get-user-cvs/get-user-cvs.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ErrorsComponent } from './errors/errors.component';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { EditUserComponent } from './get-users/edit-user/edit-user.component';
import { ShowSingleCVComponent } from './show-single-cv/show-single-cv.component';
import { AddJobComponent } from './get-user-cvs/add-job/add-job.component';
import { EditingCVComponent } from './get-user-cvs/editing-cv/editing-cv.component';
import { AddCVKnowledgeComponent } from './get-user-cvs/add-cvknowledge/add-cvknowledge.component';
import { AddPractisedLanguageComponent } from './get-user-cvs/add-practised-language/add-practised-language.component';
import { AddCourseCertificationComponent } from './get-user-cvs/add-course-certification/add-course-certification.component';
import { AddEducationComponent } from './get-user-cvs/add-education/add-education.component';
import { AddProjectComponent } from './get-user-cvs/add-project/add-project.component';
import { SortingSearchForCvsComponent } from './sorting-search-for-cvs/sorting-search-for-cvs.component';
import { SearchFilterPipe } from './SearchKnowledgeFilterPipe';


@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    LoginComponent,
    AddUserComponent,
    GetUsersComponent,
    CreateSimpleCvComponent,
    UsersProfileStartsiteComponent,
    GetUserCVsComponent,
    ErrorsComponent,
    EditUserComponent,
    ShowSingleCVComponent,
    AddJobComponent,
    EditingCVComponent,
    AddCVKnowledgeComponent,
    AddPractisedLanguageComponent,
    AddCourseCertificationComponent,
    AddProjectComponent,
    AddEducationComponent,
    SortingSearchForCvsComponent,
    SearchFilterPipe
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule,
    NgSelectModule,
    BrowserAnimationsModule,
    MatTableModule,
    MatSliderModule,
    NgbModule,
    MatDialogModule
  ],
  providers: [ParsingDataAroundService, {
    provide: MatDialogRef,
    useValue: {}
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
