import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable, throwError, VirtualTimeScheduler } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ContractsService {

  baseURL: string = environment.apiURL + '/api/';

  constructor(private http: HttpClient) { }

  // Add new Contract
  addNewContract(newContract: any): Observable<any> {
    return this.http.post<any>(this.baseURL + 'contracts', newContract, {
      observe: 'response',
    });
  }

  getContracts(options: any): Observable<any> {
    return this.http.get(`${this.baseURL}contracts/filter`, {
      observe: 'response',
      params: options,
    }
    );
  }

}
