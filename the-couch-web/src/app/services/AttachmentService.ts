import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AttachmentService {
  baseURL: string = environment.apiURL + '/api/';

  constructor(private http: HttpClient) { }

  // addAttachment(formData: any, options: any, headers: any): Observable<any> {
  //   const data = new FormData();
  //   if (formData.files) {
  //     console.log("file here", formData.files)
  //     data.append('files', formData.files[0]);
  //   }
  //   data.append('sessionId', formData.sessionId);
  //   return this.http.post<any>(
  //     this.baseURL + 'attachments/upload',
  //     data,
  //     {
  //       params: options,
  //       observe: 'response',
  //       headers: headers
  //     }
  //   );
  // }
}
