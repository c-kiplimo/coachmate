import { Injectable } from '@angular/core';
import { Observable, Subject, from } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class GoogleSignInService {
  private auth2: any;
  private auth2Initialized: Subject<boolean> = new Subject<boolean>();


  constructor(private toastrService: ToastrService, private http: HttpClient) { 
    this.initializeAuth2();
  }

private initializeAuth2() {
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
    sessionStorage.setItem('googleUser', JSON.stringify(res));
    console.log(res);

    //use this data when getting google calendar events and adding events to google calendar
    
  } ).catch((error: any) => {
    console.error(error);
  }
  );
}

signOut() {
  this.auth2Initialized.subscribe(() => {
    this.auth2.signOut().then(() => {
      sessionStorage.removeItem('googleUser');
      window.location.reload();
    });
  });
}

getCalendarEvents(): Observable<any> {

  return from(
    gapi.client.calendar.events.list({
      calendarId: 'primary',
      timeMin: new Date().toISOString(),
      showDeleted: false,
      singleEvents: true,
      maxResults: 10,
      orderBy: 'startTime',
    }).then((response: any) => {
      const events = response.result.items;
      // Return the events to the caller
      return events;
    })
  );
}


}
