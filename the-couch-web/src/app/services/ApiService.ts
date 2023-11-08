import { Injectable } from '@angular/core';
import {
  HttpClient,
  HttpParams,
  HttpHeaders,
  HttpRequest,
} from '@angular/common/http';
import { Observable, throwError, VirtualTimeScheduler } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  baseURL: string = environment.apiURL + '/api';

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

  // Get Contracts
  getContracts(): Observable<any> {
    return this.http.get(`${this.baseURL}contract`);
  }

  //get otp code for new password service
  getResetCode(options: any): Observable<any> {
    return this.http.get<any>(this.baseURL + '/registration/reset', {
      params: options,
      observe: 'response',
    });
  }
  //set new password service
  setNewPassword(newPassword: any): Observable<any> {
    return this.http.post<any>(
      this.baseURL + '/registration/forgot',
      newPassword,
      {
        observe: 'response',
      }
    );
  }
  // get OTP resend code
  getResendOtpCode(options: any): Observable<any> {
    return this.http.get<any>(this.baseURL + '/v1/registration/resend', {
      params: options,
      observe: 'response',
    });
  }

  //confirm registration service
  confirmMobileNumber(token: any): Observable<any> {
    return this.http.get<any>(
      this.baseURL + '/registration/confirm?token=' + token,
      {
        observe: 'response',
      }
    );
  }
  editOrganizationProfile(organization: any): Observable<any> {
    return this.http.put<any>(this.baseURL + '/users/organization/' + organization.id, organization, {
      observe: 'response',
    });
  }
  editCoachProfile(coach: any): Observable<any> {
    return this.http.put<any>(this.baseURL + '/users/coaches/' + coach.id, coach, {
      observe: 'response',
    });
  }
  editClientProfile(client: any): Observable<any> {
    return this.http.put<any>(this.baseURL + '/users/clients/' + client.id, client, {
      observe: 'response',
    });
  }
  //send feedback
  sendFeedback(feedback: any): Observable<any> {
    return this.http.post<any>(this.baseURL + '/response', feedback, {
      observe: 'response',
    });
  }

  //contact us  message
  support(message: any): Observable<any> {
    return this.http.post<any>(
      this.baseURL + '/registration/support',
      message,
      {
        observe: 'response',
      }
    );
  }
  addNewPayment(newPayment: any): Observable<any> {
    return this.http.post<any>(this.baseURL + '/payments', newPayment, {
      observe: 'response',
    });
  }

  // edit payment details
  editPayment(id: number, editedPaymentObject: any): Observable<any> {
    return this.http.put<any>(
      this.baseURL + '/payments/' + id,
      editedPaymentObject,
      { observe: 'response' }
    );
  }
  // filter payments by client Id
  filterPaymentsByclientId(options: any): Observable<any> {
    return this.http.get<any>(this.baseURL + '/payments/filter-by-client-id', {
      params: options,
      observe: 'response',
    });
  }
  filterPaymentsBySessionId(options: any): Observable<any> {
    return this.http.get<any>(this.baseURL + '/payments/filter-by-session-id', {
      params: options,
      observe: 'response',
    });
  }
  getOnePayment(id: number): Observable<any> {
    return this.http.get<any>(this.baseURL + '/payments/' + id, {
      observe: 'response',
    });
  }
  //payment reminder
  paymentReminder(id: number): Observable<any> {
    return this.http.post<any>(this.baseURL + '/payments/payRemind/' + id, {
      observe: 'response',
    });
  }

  // filter notifications by client id
  filterNotificationsByclientId(options: any): Observable<any> {
    return this.http.get<any>(this.baseURL + '/v1/notification', {
      params: options,
      observe: 'response',
    });
  }

  //client actions(Edit,suspend,activate,close)
  editclient(id: number, editedclientObject: any): Observable<any> {
    return this.http.put<any>(
      this.baseURL + '/client/' + id,
      editedclientObject,
      { observe: 'response' }
    );
  }
  clientAction(id: number, client: any, options: any): Observable<any> {
    return this.http.put<any>(
      this.baseURL + '/client/change-status/' + id,
      client,
      { params: options, observe: 'response' }
    );
  }
  closeclient(id: number): Observable<any> {
    return this.http.put<any>(this.baseURL + '/client/close-client/' + id, {
      observe: 'response',
    });
  }

  //COACH SESSION SLOTS
  addSlot(slot: any, options: any): Observable<any> {
    return this.http.post<any>(this.baseURL + '/sessionSchedules', slot, {
      //to check
      params: options,
      observe: 'response',
    });
  }

  getCoachSlots(options: any): Observable<any> {
    return this.http.get<any>(this.baseURL + '/sessionSchedules/filter', {
      params: options,
      observe: 'response',
    });
  }

  deleteSlot(options: any): Observable<any> {
    return this.http.delete<any>(this.baseURL + '/sessionSchedules', {
      params: options,
      observe: 'response',
    });
  }

  //get days of the week
  getDaysOfWeek(options: any): Observable<any> {
    return this.http.get<any>(
      this.baseURL + '/sessionSchedules/daysOfTheWeek',
      {
        params: options,
        observe: 'response',
      }
    );
  }

  //update days of the week
  updateDayOfTheWeek(day: any) {
    return this.http.put<any>(
      this.baseURL + '/sessionSchedules/daysOfTheWeek/' + day.id,
      day,
      {
        observe: 'response',
      }
    );
  }

  //update slot
  updateSlot(slot: any): Observable<any> {
    return this.http.put<any>(
      this.baseURL + '/sessionSchedules/' + slot.id,
      slot,
      {
        observe: 'response',
      }
    );
  }
  uploadLogo(logo: File): Observable<any> {
    const formData: FormData = new FormData();

    formData.append('file', logo);

    const req = new HttpRequest(
      'POST',
      this.baseURL + '/users/logo',
      formData,
      {
        reportProgress: true,
      }
    );

    return this.http.request(req);
  }
}
