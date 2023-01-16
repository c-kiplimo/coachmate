import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SidnavComponent } from './sidnav/sidnav.component';

import { FooterComponent } from './footer/footer.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { BodyComponent } from './body/body.component';
import { UserComponent } from './user/user.component';
import { ClientsComponent } from './clients/clients.component';

import { AddClientPageComponent } from './add-client-page/add-client-page.component';
import { AddCoachPageComponent } from './add-coach-page/add-coach-page.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { SignInComponent } from './sign-in/sign-in.component';
import { SessionsComponent } from './sessions/sessions.component';
import { AddSessionComponent } from './add-session/add-session.component';
import { SchedulesComponent } from './schedules/schedules.component';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { FormsModule } from '@angular/forms';
import { FlatpickrModule } from 'angularx-flatpickr';
import { ReactiveFormsModule } from '@angular/forms';

import { ClientService } from './services/ClientService';
import { SessionsService } from './services/SessionsService';
import { HttpClientModule } from '@angular/common/http';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { contractComponent } from './contract/contract.component';
import { AddObjectiveComponent } from './add-objective/add-objective.component';
import { ClientViewComponent } from './client-view/client-view.component';

import { FeebackFormComponent } from './feeback-form/feeback-form.component';



 import { contractViewComponent } from './contract-view/contract-view.component';
import { ApiService } from './services/ApiService';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpTokenInterceptor } from './interceptors/http.token.interceptor';
import { sessionViewComponent } from './session-view/session-view.component';
import { ForgotPaswordComponent } from './forgot-pasword/forgot-pasword.component';
import { ConfirmedViewComponent } from './confirmed-view/confirmed-view.component';
import { ToastrModule } from 'ngx-toastr';



@NgModule({
  declarations: [
    AppComponent,
    SidnavComponent,
    FooterComponent,
    contractViewComponent,
    sessionViewComponent,
    DashboardComponent,
    BodyComponent,
    UserComponent,
    ClientsComponent,
    AddClientPageComponent,
    AddCoachPageComponent,
    SignUpComponent,
    SignInComponent,
    SessionsComponent,
    AddSessionComponent,
    SchedulesComponent,
    contractComponent,
    AddObjectiveComponent,
    ClientViewComponent,

    FeebackFormComponent,



    ForgotPaswordComponent,
    ConfirmedViewComponent,

  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    FontAwesomeModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    FormsModule,
    FlatpickrModule,
    HttpClientModule,
    CalendarModule.forRoot({
      provide: DateAdapter,
      useFactory: adapterFactory,
    }),
    ToastrModule.forRoot({
      positionClass: 'toast-top-center',
      timeOut: 5000, // 5 seconds
      closeButton: false,
      progressBar: false,
    }),
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: HttpTokenInterceptor, multi: true },
    ClientService,
    SessionsService,
    ApiService,
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
