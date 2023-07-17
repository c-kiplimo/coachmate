import { MbscModule } from '@mobiscroll/angular';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatExpansionModule } from '@angular/material/expansion';



import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SidnavComponent } from './shared/sidenav/sidenav.component';
import { ContactUsComponent } from './contact-us/contact-us.component';

import { FooterComponent } from './shared/footer/footer.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { ClientsComponent } from './components/clients/clients.component';

import { AddClientPageComponent } from './components/add-client-page/add-client-page.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { SignInComponent } from './sign-in/sign-in.component';
import { SessionsComponent } from './sessions/sessions.component';
import { AddSessionComponent } from './components/add-session/add-session.component';
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
import { AddContractComponent } from './components/add-contract/contract.component';
import { ClientViewComponent } from './components/client-view/client-view.component';
import { FeebackFormComponent } from './components/feeback-form/feeback-form.component';
import { contractComponent } from './components/contracts/contracts.component';
import { ApiService } from './services/ApiService';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpTokenInterceptor } from './interceptors/http.token.interceptor';
import { sessionViewComponent } from './session-view/session-view.component';
import { ForgotPaswordComponent } from './forgot-pasword/forgot-pasword.component';
import { ConfirmedViewComponent } from './components/confirmed-view/confirmed-view.component';
import { ToastrModule } from 'ngx-toastr';
import { SharedModule } from 'src/shared/shared.module';
import { AddTrainingComponent } from './coach-education/add-training/add-training.component';
import { CoachEducationService } from './services/CoachEducationService';
import { ClientConfirmComponent } from './client-confirm/client-confirm.component';
import { PipesPipe } from './pipes.pipe';
import { ContractDetailsComponent } from './components/contract-details/contract-details.component';

import { AddCoachComponent } from './components/add-coach/add-coach.component';
import { GetPeriodPipe } from './pipes/get-period.pipe';
import { HomeComponent } from './home/home.component';
import { PaymentModule } from './payment/payment.module';
import { FeedbackComponent } from './components/feedback/feedback.component';
import { SupportComponent } from './support/support.component';
import { TermsAndConditionsPageComponent } from './terms-and-conditions-page/terms-and-conditions-page.component';
import { AddAvailableSlotsComponent } from './components/add-available-slots/add-available-slots.component';
import { SettingsComponent } from './account-settings/settings/settings.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { CoachConfirmComponent } from './coach-confirm/coach-confirm.component';
import { CoachViewComponent } from './components/coach-view/coach-view.component';
import { CoachesComponent } from './components/coaches/coaches.component';
import { NotificationsComponent } from './account-settings/notifications/notifications.component';
import { InitialSetupComponent } from './account-settings/initial-setup/initial-setup.component';
import { TemplatesComponent } from './account-settings/templates/templates.component';
import { ProfileComponent } from './account-settings/profile/profile.component';
import { PaymentsComponent } from './account-settings/payments/payments.component';
import { HeaderComponent } from './shared/header/header.component';
import { ContractsService } from './services/contracts.service';
import { FeedbackService } from './services/feedback.service';
import { NotificationsService } from './services/notifications.service';
import { TrainingsListComponent } from './coach-education/trainings-list/trainings-list.component';
import { CoachLogsComponent } from './components/coach-logs/coach-logs.component';
import { CoachLogsService } from './services/coach-logs.service';

@NgModule({
  declarations: [
    AppComponent,
    SidnavComponent,
    FooterComponent,
    AddContractComponent,
    sessionViewComponent,
    DashboardComponent,
    ClientsComponent,
    AddClientPageComponent,
    SignUpComponent,
    SignInComponent,
    SessionsComponent,
    AddSessionComponent,
    SchedulesComponent,
    contractComponent,
    ClientViewComponent,
    ContactUsComponent,
    FeebackFormComponent,
    InitialSetupComponent,
    TemplatesComponent,
    ProfileComponent,
    PaymentsComponent,
    NotificationsComponent,
    AddTrainingComponent,
    ForgotPaswordComponent,
    ConfirmedViewComponent,
    ClientConfirmComponent,
    PipesPipe,
    ContractDetailsComponent,
    GetPeriodPipe,
    AddCoachComponent,
    TrainingsListComponent,
    HomeComponent,
    FeedbackComponent,
    SupportComponent,
    TermsAndConditionsPageComponent,
    AddAvailableSlotsComponent,
    SettingsComponent,
    CoachConfirmComponent,
    CoachViewComponent,
    CoachesComponent,
    NotificationsComponent,
    HeaderComponent,
    CoachLogsComponent


  ],
  imports: [  
    MbscModule, 
    BrowserModule,
    ReactiveFormsModule,
    ReactiveFormsModule,
    FontAwesomeModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    NgxPaginationModule,
    MatPaginatorModule,
    MatSlideToggleModule,
    FormsModule,
    FlatpickrModule,
    HttpClientModule,
    PaymentModule,
    SharedModule,
    MatFormFieldModule,
    MatExpansionModule,
    CalendarModule.forRoot({
      provide: DateAdapter,
      useFactory: adapterFactory,
    }),
    ToastrModule.forRoot({
      positionClass: 'toast-top-center',
      timeOut: 5000, // 5 seconds
      closeButton: false,
      progressBar: false,
      preventDuplicates: true,
    }),
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: HttpTokenInterceptor, multi: true },
    ClientService,
    SessionsService,
    ApiService,
    CoachEducationService,
    ContractsService,
    FeedbackService,
    NotificationsService,
    CoachLogsService
  ],
  bootstrap: [AppComponent],
})
export class AppModule { }