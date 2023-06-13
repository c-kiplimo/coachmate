import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable, throwError, VirtualTimeScheduler } from 'rxjs';
import { map, catchError} from 'rxjs/operators';
import { environment } from 'src/environments/environment';
 


@Injectable({
    providedIn:'root'
})

export class SessionsService {

 

    baseURL: string =environment.apiURL + '/api/';
    
    constructor(private http: HttpClient) {

    }

    getSessions(options: any) {
      return this.http.get<any>(this.baseURL + 'sessions/filter', {
        observe: 'response',
        params: options,
      });     
    }
    getOneSession(id: number): Observable<any> {
      return this.http.get<any>(this.baseURL + '/sessions/' + id, {
        observe: 'response',
      });
    } 
    changeSessionStatus(id: number, status: string): Observable<any> {
      const data = { status: status };
      return this.http.put<any>(this.baseURL + 'sessions/change-status/' + id + '/status', data);
    }
}

// ,
//         {
//             headers: {
//                         Authorization: `Bearer ${JSON.parse(sessionStorage.getItem('userDetails') as any).token}`
//                       }
//         }