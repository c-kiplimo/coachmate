import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable, throwError, VirtualTimeScheduler } from 'rxjs';
import { map, catchError} from 'rxjs/operators';
import { environment } from 'src/environments/environment';
 
 
@Injectable({
    providedIn:'root'
})


export class ClientService {
    

    baseURL: string = environment.apiURL + '/api/';
    
    constructor(private http: HttpClient) {

    }

    getClient(options: any): Observable<any> {
        return this.http.get(`${this.baseURL}clients`,
        {
            params: options,
            observe: 'response',
        })
    }
    getOneClient(id: number): Observable<any> {
        return this.http.get<any>(this.baseURL + '/order/find/' + id, {
          observe: 'response',
        });
      } 
    
    //client status
    // ACTIVE,
    // SUSPENDED,
    // CLOSED
    addClient(client: any): Observable<any> {
        console.log(client)
        return this.http.post(`${this.baseURL}clients`, client)
    } 
    deleteClient() {
        return this.http.delete(`${this.baseURL}clients`)
    }

    // Get Contracts
    getContracts() : Observable<any> {
        return this.http.get(`${this.baseURL}contracts`)
    }

    getSessions(): Observable<any> {
        return this.http.get(`${this.baseURL}sessions`)
    }
    
    addSession(session: any): Observable<any> {
        console.log(session)
        return this.http.post(`${this.baseURL}sessions`,session
            
        )
    }

     
}
// {
//     headers: {
//         Authorization: `Bearer ${JSON.parse(sessionStorage.getItem('userDetails') as any).token}`
//       }
// })
