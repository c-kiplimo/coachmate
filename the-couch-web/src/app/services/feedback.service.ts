import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class FeedbackService {

  baseURL: string = environment.apiURL + '/api/';

  constructor(private http: HttpClient) { }

  addFeedback(feedback: any, options: any): Observable<any> {
    return this.http.post<any>(this.baseURL + 'feedback/feedback', feedback, {
      params: options,
      observe: 'response',
    });
  }
}
