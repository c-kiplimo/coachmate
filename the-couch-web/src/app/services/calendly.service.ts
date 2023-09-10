import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

//key 1: eyJraWQiOiIxY2UxZTEzNjE3ZGNmNzY2YjNjZWJjY2Y4ZGM1YmFmYThhNjVlNjg0MDIzZjdjMzJiZTgzNDliMjM4MDEzNWI0IiwidHlwIjoiUEFUIiwiYWxnIjoiRVMyNTYifQ.eyJpc3MiOiJodHRwczovL2F1dGguY2FsZW5kbHkuY29tIiwiaWF0IjoxNjkyNjA4MzE1LCJqdGkiOiI2ZDM3MjFmOS02MjU1LTQwZDAtODA5OC1mZGU4ODczNDY5MzgiLCJ1c2VyX3V1aWQiOiI3MWY0NTg1Zi0wZTg2LTRiNjEtOTY1Ni04YTVjMzk5NDRkODEifQ.HyvJLcgo8jJftzP4xZUZT4wVxqxNeyPXWM46FAgITX04xFhKjn6PM4yWYmF4HBE-6SZY1M0pkel5h7G6oJyt4A'
export class CalendlyService {

  private personalAccessToken = 'eyJraWQiOiIxY2UxZTEzNjE3ZGNmNzY2YjNjZWJjY2Y4ZGM1YmFmYThhNjVlNjg0MDIzZjdjMzJiZTgzNDliMjM4MDEzNWI0IiwidHlwIjoiUEFUIiwiYWxnIjoiRVMyNTYifQ.eyJpc3MiOiJodHRwczovL2F1dGguY2FsZW5kbHkuY29tIiwiaWF0IjoxNjk0MTEwMTI3LCJqdGkiOiJlMzk5N2U2NC04YTRmLTQ1NWItOWYwNS04NGM1ZDk0MjQ5ZGUiLCJ1c2VyX3V1aWQiOiI3MWY0NTg1Zi0wZTg2LTRiNjEtOTY1Ni04YTVjMzk5NDRkODEifQ.qhw7tS_4VYtXoek0sJiHOcQOgEZOvwWRu1_qooQkDxsVWK9qhx1tP8PKhPSkbSRceLe4EuuGt6TTNppSklHrNg';

//  private personalAccessToken = 'rfyHr57kegwZtw_uRnKePStaxWiwixdGBDI1fAyzTuA';
  private apiUrl = 'https://auth.calendly.com';
  //https://auth.calendly.com/oauth/authorize

  clientId = 'rfyHr57kegwZtw_uRnKePStaxWiwixdGBDI1fAyzTuA';
  clientSecret = 'Vud7L4vu0Mau710rqntapliDf6OLYFPUbLh_OygnuZI';
  webhookSigningKey = '_IXcvR8tgX6kHfxe-eDqeQnrFgq59A5ZS3I1Eg40DBI';

  constructor(private http: HttpClient) { }

  createCalendlyHtml(description: string, calendlyUsername: string) {
    // Step 1: Create an empty string to build your HTML snippet.
    let htmlString = '';

    // Step 2: Add the opening <h3> tag with the specified class and onclick attribute.
    htmlString += '<h3 class="fw-bold calendly" onclick="';

    // Step 3: Add the Calendly.initPopupWidget({url: 'https://calendly.com/gmunywoki'});return false; JavaScript code.
    const calendlyScript = `Calendly.initPopupWidget({url: 'https://calendly.com/${calendlyUsername}'});return false;`;
    htmlString += calendlyScript;

    // Step 4: Close the opening <h3> tag.
    htmlString += '">';

    // Step 5: Add the text "Preview your" within the <h3> tag.
    htmlString += description;

    // Step 6: Add the <img> tag with the specified src and alt attributes.
    htmlString += '<img src="../../assets/images/calendly.png" alt="Calendar" class="calendly-logo">';

    // Step 7: Close the <h3> tag.
    htmlString += '</h3>';

    // Now, the `htmlString` variable contains your HTML snippet.
    return htmlString;

  }




  //NOT USED

  getAuthCode(): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Basic ${btoa(`${this.clientId}:${this.clientSecret}`)}`
    });

    return this.http.get(`${this.apiUrl}/oauth/authorize`, { headers, params: { client_id: this.clientId, response_type: 'code' } })
      .pipe(
        catchError((error: HttpErrorResponse) => {
          console.error('API Request Error:', error);
          return throwError(error);
        })
      );
  }



  getAccessToken(): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
      'Authorization': `Basic ${btoa(`${this.clientId}:${this.clientSecret}`)}`
    });

    const options = {
      grant_type: 'authorization_code',
      code: 'AUTHORIZATION_CODE',
      redirect_uri: 'REDIRECT_URI'
    };

    return this.http.post(`${this.apiUrl}/oauth/token`, options, { headers })
      .pipe(
        catchError((error: HttpErrorResponse) => {
          console.error('API Request Error:', error); 
          return throwError(error);
        })
      );
  }
  
  //login to calendly using client id and client secret and webhook signing key
  login(): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.personalAccessToken}`,
      'Content-Type': 'application/json',
      'client_id': this.clientId,
      'client_secret': this.clientSecret,
    });

    const options = {
      client_id: this.clientId,
      client_secret: this.clientSecret,
      webhook_signing_key: this.webhookSigningKey
    };

    return this.http.get(`https://auth.calendly.com/oauth/token`, { headers, params: options })
      .pipe(
        catchError((error: HttpErrorResponse) => {
          console.error('API Request Error:', error);
          return throwError(error);
        })
      );
  }
   

  getCalendlyData(): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.personalAccessToken}`
    });
  
    return this.http.get(`${this.apiUrl}/v1/endpoint`, { headers })
      .pipe(
        catchError((error: HttpErrorResponse) => {
          console.error('API Request Error:', error);
          return throwError(error);
        })
      );
  }
  

}
