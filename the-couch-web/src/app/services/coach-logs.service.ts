import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from "@angular/common/http";
import { Observable, throwError, VirtualTimeScheduler } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: 'root'
})
export class CoachLogsService {
  baseURL: string = environment.apiURL + "/api/";

  constructor(private http: HttpClient) { }

  addCoachLogs(coachLogs: any): Observable<any> {
    console.log(coachLogs);
    return this.http.post(`${this.baseURL}coaching-log`, coachLogs);
  }

  getCoachLogs(options: any): Observable<any> {
    return this.http.get<any>(this.baseURL + "coaching-log", {
      params: options,
      observe: "response"
    });
  }


}
