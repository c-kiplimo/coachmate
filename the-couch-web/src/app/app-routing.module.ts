import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ClientsComponent } from './clients/clients.component';
import { AddClientPageComponent } from './add-client-page/add-client-page.component';
import { SignInComponent } from './sign-in/sign-in.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { SessionsComponent } from './sessions/sessions.component';
import { AddSessionComponent } from './add-session/add-session.component';
import { SchedulesComponent } from './schedules/schedules.component';
import { ClientViewComponent } from './client-view/client-view.component';
import { contractViewComponent } from './contract-view/contract-view.component';
import { contractComponent } from './contract/contract.component';
import { sessionViewComponent } from './session-view/session-view.component';
import { ForgotPaswordComponent } from './forgot-pasword/forgot-pasword.component';
import { ConfirmedViewComponent } from './confirmed-view/confirmed-view.component';
import { AddTrainingComponent } from './coach-education/add-training/add-training.component';
import { TrainingsListComponent } from './coach-education/trainings-list/trainings-list.component';
import { FeebackFormComponent } from './feeback-form/feeback-form.component';
import { ClientConfirmComponent } from './client-confirm/client-confirm.component';
import { ContractDetailsComponent } from './contract-details/contract-details.component';
import { AddCoachComponent } from './add-coach/add-coach.component';
import { SupportComponent } from './support/support.component';
import { HomeComponent } from './home/home.component';
import { RecordPaymentComponent } from './payment/payment/record-payment/record-payment.component';
import { AccountSummaryComponent } from './payment/payment/account-summary/account-summary.component';
import { FeedbackComponent } from './feedback/feedback.component';
import { TermsAndConditionsPageComponent } from './terms-and-conditions-page/terms-and-conditions-page.component';
import { AddAvailableSlotsComponent } from './add-available-slots/add-available-slots.component';
import { SettingsComponent } from './account-settings/settings/settings.component';
import { ReceiptsComponent } from './payment/payment/receipts/receipts.component';
import { CoachConfirmComponent } from './coach-confirm/coach-confirm.component';
import { CoachViewComponent } from './coach-view/coach-view.component';
import { CoachesComponent } from './coaches/coaches.component';
import { NotificationsComponent } from './account-settings/notifications/notifications.component';
import { PaymentsComponent } from './account-settings/payments/payments.component';
import { ProfileComponent } from './account-settings/profile/profile.component';
import { TemplatesComponent } from './account-settings/templates/templates.component';
import { ContactUsComponent } from './contact-us/contact-us.component';


const routes: Routes = [
  {
    path:'home',
   component:HomeComponent
  },
  {
    path: 'dashboard',
    component: DashboardComponent
  },
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  {
    path: 'clients',
    component: ClientsComponent
  },
  {
    path:'coaches',
    component:CoachesComponent
  },
  {
    path: 'addclient',
    component: AddClientPageComponent
  },
  {
    path:'sessionView/:id',
    component:sessionViewComponent
  },
  {
    path: 'signin',
    component: SignInComponent
  },
  {
    path: 'support',
    component: SupportComponent
  },
  {
    path: 'signup',
    component: SignUpComponent
  },
  {
    path: 'addSession',
    component: AddSessionComponent
  },
  {
    path: 'schedules',
    component: SchedulesComponent
  },

  {
    path: 'education',
    component:TrainingsListComponent
  },
  {
    path: 'sessions',
    component: SessionsComponent
  },
  {
    path: 'clientView/:id',
    component: ClientViewComponent
  },
  {
    path: 'coachView/:id',
    component: CoachViewComponent
  },
  {
    path: 'contracts',
    component: contractViewComponent

  },
  {
     path:'contract',
     component:contractComponent
  },
  {
    path:'payment',
    component:ReceiptsComponent
 },
  {
    path:'feedback1',
   component:FeebackFormComponent
 },
 { path: 'feedback/:sessionId', 
 component: FeebackFormComponent },

  {
    path: 'AddTraining',
    component: AddTrainingComponent
  },
  { path: 'reset/request', component: ForgotPaswordComponent },
  
  { path: 'registration/confirm', 
    component: ConfirmedViewComponent },

    { path: 'contractDetail/:id', 
    component: ContractDetailsComponent },

  {
      path: 'confirmclient/:id/:token',
      component: ClientConfirmComponent
  },
  {
    path: 'confirmcoach/:id/:token',
    component: CoachConfirmComponent
},
  {
    path: 'addCoach',
    component: AddCoachComponent
  },
  {
    path: 'feedback',
    component: FeedbackComponent
  },
  {
    path: 'feedback',
    component: FeedbackComponent
  },
  {
    path: 'terms/:id',
    component: TermsAndConditionsPageComponent
  },
  {
    path: 'addAvailableSlots',
    component: AddAvailableSlotsComponent
  },
  { path: 'settings', component: SettingsComponent },
  { path: 'contact', component: ContactUsComponent },
  { path: 'account-settings/profile', component: ProfileComponent },
  { path: 'account-settings/notification', component: NotificationsComponent },
  { path: 'account-settings/templates', component: TemplatesComponent },
  { path: 'account-settings/payments', component: PaymentsComponent },

  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }