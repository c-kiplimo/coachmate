import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CoachService {

    baseURL: string = environment.apiURL + '/api';

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

  onboardBaker(bakerDeatils: any): Observable<any> {
    return this.httpClient.post<any>(this.baseURL + '/coach/onboard', bakerDeatils, { observe: 'response' });
  }

  editcoachProfile(bakerProfile: any): Observable<any> {
    return this.httpClient.put<any>(this.baseURL + '/coach/' + bakerProfile.id, bakerProfile, { observe: 'response' });
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


}
