import { Injectable } from '@angular/core';
import { Observable, Subject, from } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})

export class GoogleSignInService {
  private auth2: any;
  private auth2Initialized: Subject<boolean> = new Subject<boolean>();
  private userData: any;
  private user: any;

  baseURL: string = environment.apiURL + '/api/';


  constructor(private toastrService: ToastrService, private http: HttpClient) {

  }

  initializeAuth2() {
    gapi.load('client', () => {
      console.log('loaded client');

      gapi.client.init({
        apiKey: 'AIzaSyBvIRJQSm4oiRCE7K9cFKf4j9FAhQPEn6o', // Replace with your API key
        clientId: '400095806902-202t6v03drgvvt0p8l9p1e5jmo71pot2.apps.googleusercontent.com', // Replace with your client ID
        // discoveryDocs for calendar and people API
        discoveryDocs: [
          'https://www.googleapis.com/discovery/v1/apis/calendar/v3/rest'
        ],
        plugin_name: 'CoachMatePro',
        // scope for calendar and people API
        scope: 'profile email https://www.googleapis.com/auth/calendar'
      }).then(() => {
        console.log('Google API client initialized successfully.');

        // Load the Calendar API
        gapi.client.load('calendar', 'v3', () => {
          console.log('Loaded Calendar API');
          // You can start making API requests here once the Calendar API is loaded.
        });

      }).catch((error: any) => {
        console.error('Error initializing Google API client:', error);
      });
    });
  }


  async signIn() {
    const googleAuth = gapi.auth2.getAuthInstance();
    await googleAuth.signIn({ prompt: 'consent' }).then((res: any) => {

      //get access tokens from google
      const googleUser = googleAuth.currentUser.get();
      const authResponse = googleUser.getAuthResponse(true);
      const token = authResponse.id_token;

      //user session data update google auth
      this.userData = sessionStorage.getItem('user');
      this.user = JSON.parse(this.userData);
      this.user.google = token;
      sessionStorage.setItem('user', JSON.stringify(this.user));

      this.toastrService.success('Successfully signed in to Google');

      // this.http.post(this.baseURL + 'users/google-auth', token).subscribe((response: any) => {
      //   console.log(response);
      //   this.toastrService.success('Successfully signed in to Google');
      // }
      // );



    }).catch((error: any) => {
      console.error(error);
    }
    );
  }

  signOut() {
    this.auth2Initialized.subscribe(() => {
      this.auth2.signOut().then(() => {

        // this.http.delete(this.baseURL + 'users/google-auth').subscribe((response: any) => {
        //   console.log(response);

        //   this.userData = sessionStorage.getItem('user');
        //   this.user = JSON.parse(this.userData);
        //   this.user.google = null;
        //   sessionStorage.setItem('user', JSON.stringify(this.user));

        //   this.toastrService.success('Successfully signed out of Google');
        // }
        // );

        this.toastrService.success('Successfully signed out of Google');

        window.location.reload();
      });
    });
  }

  isSignedIn = () => {
    return gapi.auth2.getAuthInstance().isSignedIn.get();
  }


  getCalendarEvents(): Observable<any> {

    //check if gapiauth2 is initialized
    if (!this.auth2Initialized) {
      setTimeout(() => {
        this.getCalendarEvents();
      }
        , 1000);
    }
    return from(
      gapi.client.calendar.events.list({
        calendarId: 'primary',
        timeMin: new Date().toISOString(),
        showDeleted: false,
        singleEvents: true,
        maxResults: 100,
        orderBy: 'startTime',
      }).then((response: any) => {
        const events = response.result.items;
        // Return the events to the caller
        return events;
      })
    );
  }

  addEventToGoogleCalendar(event: any): Observable<any> {
    return from(
      gapi.client.calendar.events.insert({
        calendarId: 'primary',
        resource: event,
      }).then((response: any) => {
        return response;
      })
    );
  }


}
