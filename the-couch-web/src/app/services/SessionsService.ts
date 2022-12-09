import { Injectable } from '@angular/core';
 
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable, throwError, VirtualTimeScheduler } from 'rxjs';
import { map, catchError} from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn:'root'
})


export class SessionsService {
    getSessions() {
      throw new Error('Method not implemented.');
    }

    baseURL: string =environment.apiURL + '/api/';
    
    constructor(private http: HttpClient) {

    }
 
}
// ,
//         {
//             headers: {
//                         Authorization: `Bearer ${JSON.parse(sessionStorage.getItem('userDetails') as any).token}`
//                       }
//         }