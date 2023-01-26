import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable, throwError, VirtualTimeScheduler } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  baseURL: string = environment.apiURL + '/api/';

  constructor(private http: HttpClient) {}

  getClients(): Observable<any> {
    return this.http.get(`${this.baseURL}clients`);
  }

  //client status
  // ACTIVE,
  // SUSPENDED,
  // CLOSED
  addClient(client: any): Observable<any> {
    console.log(client);
    return this.http.post(`${this.baseURL}clients`, {
      name: client.name,
      type: client.clientType,
      msisdn: client.phone,
      email_address: client.email,
      physical_address: client.address,
      profession: client.profession,
      payment_mode: client.PaymentMode,
      status: 'NEW',
      reason: client.reason,
      createdBy: JSON.parse(sessionStorage.getItem('userDetails') as any).user
        .id,
      lastUpdatedBy: JSON.parse(sessionStorage.getItem('userDetails') as any)
        .user.fullName,
    });
  }
  deleteClient() {
    return this.http.delete(`${this.baseURL}clients`);
  }

  getSessions(): Observable<any> {
    return this.http.get(`${this.baseURL}sessions`);
  }

  addSession(session: any): Observable<any> {
    console.log(session);
    return this.http.post(`${this.baseURL}sessions`, {
      name: session.name,
      type: session.type,
      session_date: session.session_date,
      session_venue: session.session_venue,
      goals: session.goals,
      createdBy: JSON.parse(sessionStorage.getItem('userDetails') as any).user
        .id,
      lastUpdatedBy: JSON.parse(sessionStorage.getItem('userDetails') as any)
        .user.fullName,
    });
  }

  // Add new Contract
  addNewContract(newContract: any): Observable<any> {
    return this.http.post<any>(this.baseURL + 'contracts', newContract, {
      observe: 'response',
    });
  }

  // Get Contracts
  getContracts(): Observable<any> {
    return this.http.get(`${this.baseURL}contract`);
  }

  //get otp code for new password service
  getResetCode(options:any): Observable<any> {
    return this.http.get<any>(this.baseURL + '/registration/reset' , 
    {
      params:options,
      observe: 'response',
    });
  }
  //set new password service
  setNewPassword(newPassword: any): Observable<any> {
    return this.http.post<any>(this.baseURL + 'registration/forgot', newPassword, {
      observe: 'response',
    });
  }

  //confirm registration service
  confirmMobileNumber(token: any): Observable<any> {
    return this.http.get<any>(
      this.baseURL + 'registration/confirm?token=' + token,
      {
        observe: 'response',
      }
    );
  }
}
// {
//     headers: {
//         Authorization: `Bearer ${JSON.parse(sessionStorage.getItem('userDetails') as any).token}`
//       }
// })
