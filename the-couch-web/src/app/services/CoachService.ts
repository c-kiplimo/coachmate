import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpRequest } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CoachService {

    baseURL: string = environment.apiURL + '/api/';

  constructor(private httpClient: HttpClient) { }

  saveSettings(settingsObject: any): Observable<any> {
    return this.httpClient.put<any>(
      this.baseURL + '/notification-settings',
      settingsObject,
      { observe: 'response' }
    );
  }

  uploadLogo(logo: File): Observable<any> {
    const formData: FormData = new FormData();

    formData.append('file', logo);

    const req = new HttpRequest('POST', this.baseURL + '/coach/logo', formData, {
      reportProgress: true
    });

    return this.httpClient.request(req);
  }

  onboardBaker(coachDeatils: any): Observable<any> {
    return this.httpClient.post<any>(this.baseURL + '/coach/onboard', coachDeatils, { observe: 'response' });
  }

  editcoachProfile(coachProfile: any): Observable<any> {
    return this.httpClient.put<any>(this.baseURL + '/coach/' + coachProfile.id, coachProfile, { observe: 'response' });
  }

  editLocationDetails(locationDetails: any): Observable<any> {
    return this.httpClient.post<any>(this.baseURL + '/locations', locationDetails, { observe: 'response' });
  }

  editcoachLogo(logo: any): Observable<any> {
    return this.httpClient.post<any>(this.baseURL + '/coach-settings', logo, { observe: 'response' });
  }

  editPaymentDetails(paymentDetails: any): Observable<any> {
    return this.httpClient.post<any>(this.baseURL + '/payment-details', paymentDetails, { observe: 'response' });
  }

  getLogo(): Observable<any> {
    return this.httpClient.get<any>(this.baseURL + '/coach-settings/logo', { observe: 'response', responseType: 'blob' as 'json' });
  }

  getCoaches(options: any): Observable<any> {
    return this.httpClient.get(`${this.baseURL}users/coaches`,
      {
        params: options,
        observe: 'response',
      })
  }
  suspendCoach(coachData: any): Observable<any> {
    return this.httpClient.put(`${this.baseURL}coaches`, coachData)
  }
  editCoach(coachToBeUpdated: any, id: any): Observable<any> {
    console.log("edit coach reached")
    console.log("coach  to be updated here", coachToBeUpdated)
    console.log("coach id here", id)
    return this.httpClient.put(`${this.baseURL}users/coach/${id}`, coachToBeUpdated)
  }
  getOneCoach(id: number): Observable<any> {
    return this.httpClient.get<any>(this.baseURL + 'users/coach/' + id, {
      observe: 'response',
    });
  }
  addCoach(coach: any): Observable<any> {
    console.log(coach)
    return this.httpClient.post(`${this.baseURL}users/coach/`, coach,
      { observe: 'response' }).pipe(
        catchError(this.handleError)
      );

  }
  getOrgCoaches(data: any): Observable<any> {
    console.log("get org coaches reached")
    console.log("data", data)
    return this.httpClient.get(`${this.baseURL}organizations/getCoachesByOrgId`, { params: data })
  }
  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An error occurred';
    if (error.error instanceof ErrorEvent) {
      // Client-side errors
      errorMessage = error.error.message;
    } else {
      // Server-side errors
      errorMessage = error.error.message;
    }
    // Return an observable with a user-facing error message
    return throwError(errorMessage);
  }
  changeCoach(coachId: any, status: any, statusForm: any): Observable<any> {
    let options = {
      status: status,
    };
    return this.httpClient.put<any>(this.baseURL + 'coaches/change-status/' + coachId, statusForm,
      {
        params: options,
        observe: "response"

      });

  }
}
