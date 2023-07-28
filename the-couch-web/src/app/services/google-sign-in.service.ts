import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GoogleSignInService {
  private auth2: any;
  private auth2Initialized: Subject<boolean> = new Subject<boolean>();


  constructor() { 
    this.initializeAuth2();
  }

  private initializeAuth2() {
    gapi.load('client', () => {
      console.log('loaded client');

      gapi.client.init({
        apiKey: 'AIzaSyBvIRJQSm4oiRCE7K9cFKf4j9FAhQPEn6o',
        client_id: '400095806902-202t6v03drgvvt0p8l9p1e5jmo71pot2.apps.googleusercontent.com',
        //discoveryDocs for calendar and sign in
        discoveryDocs: ['https://www.googleapis.com/discovery/v1/apis/calendar/v3/rest',
          'https://people.googleapis.com/$discovery/rest?version=v1'],
        //scope for calendar and sign in
        scope: 'https://www.googleapis.com/auth/calendar https://www.googleapis.com/auth/calendar.events https://www.googleapis.com/auth/calendar.events.readonly https://www.googleapis.com/auth/calendar.readonly https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile'
      })

      gapi.client.load('calendar', 'v3', () => console.log('loaded calendar'));
      gapi.client.load('people', 'v1', () => console.log('loaded people'));
  });
}

async signIn() {
  const googleAuth = gapi.auth2.getAuthInstance();
  const googleUser = googleAuth.currentUser.get();

  const profile = googleUser.getBasicProfile();

  const idToken = googleUser.getAuthResponse().id_token;
  const accessToken = googleUser.getAuthResponse().access_token;

  sessionStorage.setItem('googleUser', JSON.stringify({
    profile: profile,
    idToken: idToken,
    accessToken: accessToken
  }));

  

}

signOut() {
  this.auth2Initialized.subscribe(() => {
    this.auth2.signOut().then(() => {
      sessionStorage.removeItem('googleUser');
      window.location.reload();
    });
  });
}


}
