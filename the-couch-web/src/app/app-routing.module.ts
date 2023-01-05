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
import { AddObjectiveComponent } from './add-objective/add-objective.component';
import { ClientViewComponent } from './client-view/client-view.component';
import { contractViewComponent } from './contract-view/contract-view.component';
import { contractComponent } from './contract/contract.component';
import { sessionViewComponent } from './session-view/session-view.component';
import { ForgotPaswordComponent } from './forgot-pasword/forgot-pasword.component';
import { ConfirmedViewComponent } from './confirmed-view/confirmed-view.component';


const routes: Routes = [
  {
    path: 'dashboard',
    component: DashboardComponent
  },
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  {
    path: 'clients',
    component: ClientsComponent
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
    path: 'notifications',
    component: ContractComponent
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
    path: 'contracts',
    component: contractViewComponent

  },
  {
    path:'contracts',
    component:SessionsComponent
  },
  {
     path:'contract',
     component:contractComponent
  },
  { path: 'reset/request', component: ForgotPaswordComponent },
  
  { path: 'registration/confirm', component: ConfirmedViewComponent },
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }