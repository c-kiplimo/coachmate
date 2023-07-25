import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, VirtualTimeScheduler } from 'rxjs';
import { map, catchError, tap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { ToastrService } from 'ngx-toastr';

@Injectable({
    providedIn: 'root',
})

export class NotificationsService {
    baseURL: string = environment.apiURL + '/api/';
    deleteClient: any;

    constructor(private http: HttpClient,
        private toastr: ToastrService) {

    }

    getNotifications(options: any): Observable<any> {
        return this.http.get(this.baseURL + 'notification/filter',
            {
                params: options,
                observe: 'response',
            }
        );

    }

    contactUsMessage(message: any): Observable<any> {
        return this.http.post<any>(
          this.baseURL + 'registration/contact',
          message,
          {
            observe: 'response',
          }
        );
      }

}
