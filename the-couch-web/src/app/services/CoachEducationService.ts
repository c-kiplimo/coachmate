import { Injectable } from "@angular/core";
import { HttpClient, HttpParams, HttpHeaders } from "@angular/common/http";
import { Observable, throwError, VirtualTimeScheduler } from "rxjs";
import { map, catchError } from "rxjs/operators";
import { environment } from "src/environments/environment";

@Injectable({
    providedIn: "root"
})

export class CoachEducationService {
    baseURL: string = environment.apiURL + "/api/";

    constructor(private http: HttpClient) {}

    getCoachEducation(options: any): Observable<any> {
        return this.http.get<any>(this.baseURL + "coachEducation/allCoachEducation", {
            params: options,
            observe: "response"
        });
    }

    getOneCoachEducation(id: number): Observable<any> {
        return this.http.get<any>(this.baseURL + "coachEducation/" + id, {
            observe: "response"
        });
    }

    addCoachEducation(coachEducation: any): Observable<any> {
        console.log(coachEducation);
        return this.http.post(`${this.baseURL}coachEducation`, coachEducation);
    }

    updateCoachEducation(coachEducation: any): Observable<any> {
        return this.http.put(`${this.baseURL}coachEducation`, coachEducation);
    }

    deleteCoachEducation(id: number): Observable<any> {
        return this.http.delete(`${this.baseURL}coachEducation/${id}`);
    }
}