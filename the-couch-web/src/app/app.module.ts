import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SidnavComponent } from './sidnav/sidnav.component';
import { AddClientPageComponent } from './add-client-page/add-client-page.component';
import { AddCoachPageComponent } from './add-coach-page/add-coach-page.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { SignInComponent } from './sign-in/sign-in.component';
import { FooterComponent } from './footer/footer.component';

@NgModule({
  declarations: [
    AppComponent,
    SidnavComponent,
    FooterComponent,
    AddClientPageComponent,
    AddCoachPageComponent,
    SignUpComponent,
    SignInComponent,

  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
