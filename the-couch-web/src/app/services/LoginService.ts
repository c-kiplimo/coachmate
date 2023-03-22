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
    login(user: any) {
        return this.http.post<any>(this.baseURL + 'token', user);
      }
      


    // signUp service
    signUp(signupObject: any): Observable<any> {
        return this.http.post<any>(
          this.baseURL+ '/api/coach',
          signupObject,
          { observe: 'response' }
        );
      }

      //Update Password and complete client registration
      confirmAndUpdateClientPassword(signupObject: any): Observable<any> {
        return this.http.post<any>(
          this.baseURL+ 'registration/confirmClientToken',
          signupObject,
          { observe: 'response' }
        );
      }
}

