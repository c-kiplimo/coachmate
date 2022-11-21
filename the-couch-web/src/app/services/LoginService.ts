import { Injectable } from '@angular/core';
 
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable, throwError, VirtualTimeScheduler } from 'rxjs';
import { map, catchError} from 'rxjs/operators';
import { environment } from 'src/environments/environment';
 
 
@Injectable({
    providedIn:'root'
})

export class LoginService {
    baseURL: string = environment.apiURL;
    
    constructor(private http: HttpClient) {

    }

    login(user: any): Observable<any> {
        return this.http.post(`${this.baseURL}token`, {
            username: user.email,
            password: user.password
        })
    }
}