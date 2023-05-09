import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable, throwError, VirtualTimeScheduler } from 'rxjs';
import { map, catchError} from 'rxjs/operators';
import { environment } from 'src/environments/environment';
 


@Injectable({
    providedIn:'root'
})

export class SessionsService {

    getSessions(options: any) {
      return this.http.get<any>(this.baseURL + 'sessions/filter', {
        observe: 'response',
        params: options,
      });     
    }

    baseURL: string =environment.apiURL + '/api/';
    
    constructor(private http: HttpClient) {

    }
    getOneSession(id: number): Observable<any> {
      return this.http.get<any>(this.baseURL + '/sessions/' + id, {
        observe: 'response',
      });
    } 
}

// ,
//         {
//             headers: {
//                         Authorization: `Bearer ${JSON.parse(sessionStorage.getItem('userDetails') as any).token}`
//                       }
//         }