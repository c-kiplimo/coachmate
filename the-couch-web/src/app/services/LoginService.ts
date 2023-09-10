import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable, throwError, VirtualTimeScheduler } from 'rxjs';
import { map, catchError} from 'rxjs/operators';
import { environment } from 'src/environments/environment';
 
 
@Injectable({
    providedIn:'root'
})



export class LoginService {
 
    baseURL = environment.apiURL + '/api/';
    constructor(private http: HttpClient) {
    }
    getAccount(): Observable<any> {
      return this.http.get<any>(
        this.baseURL + '/account', { observe: 'response' }
      );
    }

    // login service
    login(user: any) {
        return this.http.post<any>(this.baseURL + 'token', user);
      }
      


    // signUp service
    signUp(signupObject: any): Observable<any> {
        return this.http.post<any>(
          this.baseURL+ 'registration',
          signupObject,
          { observe: 'response' }
        );
      }


    // register coach
    // signUp service
    registerCoach(signupObject: any): Observable<any> {
      return this.http.post<any>(
        this.baseURL+ 'coach',
        signupObject,
        { observe: 'response' }
      );
    }

    addCoach(coach: any): Observable<any> {
      return this.http.post<any>(
        this.baseURL+ 'users/coach',
        coach,
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
        //Update Password and complete coach registration
        confirmAndUpdateCoachPassword(signupObject: any): Observable<any> {
          return this.http.post<any>(
            this.baseURL+ 'registration/confirmCoachToken',
            signupObject,
            { observe: 'response' }
          );
        }

        setCalendlyUsername(username: any): Observable<any> {
          return this.http.post<any>(
            this.baseURL+ 'users/calendly-username',
            username,
            { observe: 'response' }
          );
        }

        getAddedBy(id: any): Observable<any> {
          return this.http.get<any>(
            this.baseURL+ 'users/added-by/'+id,
            { observe: 'response' }
          );
        }
}

