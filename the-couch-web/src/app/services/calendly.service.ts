import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class CalendlyService {

  private baseUrl = 'https://api.calendly.com';

  constructor(private http: HttpClient) { }

  getEventTypes(): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': 'Bearer eyJraWQiOiIxY2UxZTEzNjE3ZGNmNzY2YjNjZWJjY2Y4ZGM1YmFmYThhNjVlNjg0MDIzZjdjMzJiZTgzNDliMjM4MDEzNWI0IiwidHlwIjoiUEFUIiwiYWxnIjoiRVMyNTYifQ.eyJpc3MiOiJodHRwczovL2F1dGguY2FsZW5kbHkuY29tIiwiaWF0IjoxNjkyNjA4MzE1LCJqdGkiOiI2ZDM3MjFmOS02MjU1LTQwZDAtODA5OC1mZGU4ODczNDY5MzgiLCJ1c2VyX3V1aWQiOiI3MWY0NTg1Zi0wZTg2LTRiNjEtOTY1Ni04YTVjMzk5NDRkODEifQ.HyvJLcgo8jJftzP4xZUZT4wVxqxNeyPXWM46FAgITX04xFhKjn6PM4yWYmF4HBE-6SZY1M0pkel5h7G6oJyt4A',
    });

    return this.http.get(`${this.baseUrl}/event_types`, { headers });
  }

}
