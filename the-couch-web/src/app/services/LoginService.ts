import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable, throwError, VirtualTimeScheduler } from 'rxjs';
import { map, catchError} from 'rxjs/operators';
import { environment } from 'src/environments/environment';
 
 
@Injectable({
    providedIn:'root'
})



export class LoginService {

    baseURL: string = environment.apiURL + '/api/';
    constructor(private http: HttpClient) {

    }


    // login service
    login(user: any): Observable<any> {
        return this.http.post(`${this.baseURL}token`, {
            username: user.email,
            password: user.password
        })
    }

    // signUp service
    signUp(user: any): Observable<any> {
        return this.http.post<any>(`${this.baseURL}registration`, {
            fullName: user.fullName,
            businessName: user.businessName,
            msisdn: user.msisdn,
            email: user.email,
            password: user.password,
        })
    }
}

