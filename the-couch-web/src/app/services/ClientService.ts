import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, VirtualTimeScheduler } from 'rxjs';
import { map, catchError, tap} from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { faEyeDropper } from '@fortawesome/free-solid-svg-icons';
import { ToastrService } from 'ngx-toastr';
import { param } from 'jquery';
 
 
@Injectable({
    providedIn:'root'
})


export class ClientService {
  getFiltered: any;
  getFilteredSessions(options: { page: number; per_page: number; status: any; search: any; period: string; balance: any; }) {
    throw new Error('Method not implemented.');
  }
  

    baseURL: string = environment.apiURL + '/api/';
  deleteClient: any;
    
    constructor(private http: HttpClient,
        private toastr: ToastrService) {

    }
    getContract(id: any):Observable<any> {
        return this.http.get<any>(this.baseURL + 'contracts/' + id,{observe:'response'});
      }

    getClient(options: any): Observable<any> {
        return this.http.get(`${this.baseURL}clients`,
        {
            params: options,
            observe: 'response',
        })
    }
    getOneClient(id: number): Observable<any> {
        return this.http.get<any>(this.baseURL + '/clients/' + id, {
          observe: 'response',
        });
      } 
      getOneSession(id:number): Observable<any> {
        return this.http.get<any>(this.baseURL + '/sessions/' + id, {
            observe: 'response',
          });
    }
    
    //client status
    // ACTIVE,
    // SUSPENDED,
    // CLOSED
    addClient(client: any): Observable<any> {
        console.log(client)
        return this.http.post(`${this.baseURL}clients`, client,
        {observe: 'response'} ).pipe(
      catchError(this.handleError)
    );
    
    } 
    private handleError(error: HttpErrorResponse) {
        let errorMessage = 'An error occurred';
        if (error.error instanceof ErrorEvent) {
          // Client-side errors
          errorMessage = error.error.message;
        } else {
          // Server-side errors
          errorMessage = error.error.message;
        }
        // Return an observable with a user-facing error message
        return throwError(errorMessage);
      }
    suspendClient(clientData: any): Observable<any> {
        return this.http.put(`${this.baseURL}clients`, clientData)
    }


    editClient(clientToBeUpdated:any,id: any): Observable<any> {
        console.log("edit client reached")
        console.log("client  to be updated here",clientToBeUpdated)
        console.log("client id here",id)
        return this.http.put(`${this.baseURL}clients/${id}`, clientToBeUpdated)
    }
//service to change client status



   

    changeClient(clientId: any, status:any, statusForm: any): Observable<any> {
      let options = {
        status: status,
      };
        return this.http.put<any>(this.baseURL + 'clients/change-status/'+ clientId,statusForm,
        {

          params:options,
          observe:"response"

        });

    } 



     
    // Get all Contracts
    getContracts() : Observable<any> {
        return this.http.get(`${this.baseURL}contracts`)
    }
//Get all contracts by client id
    getContractsByClientId(clientId: any): Observable<any> {
        return this.http.get(`${this.baseURL}contracts/byClient/` + clientId,{observe:'response'})
        
    }
    getSessions(options: any): Observable<any> {
        return this.http.get(`${this.baseURL}sessions`,
        {
            params: options,
            observe: 'response',
        })
    }
     // Get sessions by contractId
  getSessionsBycontractId(contractId:any):Observable<any>{
    return this.http.get(`${this.baseURL}sessions/contractSessions/` + contractId,{observe:'response'})
  } 
  
    // session actions
    addSession(session: any, params: any): Observable<any> {
        console.log(session);
        return this.http.post(`${this.baseURL}sessions`, session, {params: params})
        //   .pipe(
        //     tap(response => {
        //       this.toastr.success('Session created successfully');
        //     }),
        //     catchError(error => {
        //       console.error('Session not added:', error);
        //       this.toastr.error(error.error, 'Maximum sessions reached contact coach');
        //       throw error;
        //     })
        //   );
      }
    editSession(id: any) : Observable<any> {
        return this.http.delete(this.baseURL + `/sessions/` + id,);
      }
    deleteSession(id: any ): Observable<any> {
        return this.http.delete(this.baseURL + `/sessions/` + id,);
      }

    getOrgSessions(id: any): Observable<any> {
        return this.http.get(`${this.baseURL}sessions/getorgSessions/` + id);
    }
    
    

    getClientSessions(clientId: any): Observable<any> {
        console.log("Get Sessions reached!");
        return this.http.get(`${this.baseURL}sessions/clientSessions/` + clientId,{observe:'response'})
    }

    getClientContracts(clientId: any): Observable<any> {
        console.log("Get Contracts reached!");
        return this.http.get(`${this.baseURL}contracts/byClient/` + clientId)
    }

    getClientByEmail(email: any): Observable<any> {
        return this.http.post(`${this.baseURL}clients/getClientByEmail`, email);
    }


    //ORGANIZATION SERVICES
    getOrganization(data: any): Observable<any>{
        return this.http.get(`${this.baseURL}organizations/getOrganizationBySuperCoachId`, {params: data})
    }

    getOrgCoaches(data: any): Observable<any>{
        return this.http.get(`${this.baseURL}organizations/getCoachesByOrgId`, {params: data})
    }
    getOrgClients(id: any): Observable<any>{
        return this.http.get(`${this.baseURL}clients/getOrgClients/` + id);
    }

    getOrgContracts(id: any): Observable<any>{
        return this.http.get(`${this.baseURL}contracts/getOrgContracts/` + id);
    }
 

// FEEDBACK SERVICES
addFeedback(feedback: any, options: any): Observable<any> {
    return this.http.post<any>(
        this.baseURL + 'feedback',
        feedback,
        { params: options, observe: 'response' }
      );
  }
getFeedback(params:any):Observable<any>{
    return this.http.get(`${this.baseURL}feedback/get-by-session-id`,{
        params: params,
        observe:'response'})
}
getOrgFeedbacks(id: any): Observable<any>{
    return this.http.get(`${this.baseURL}feedback/getOrgFeedbacks/` + id);
}

getCoachFeedbacks(id: any): Observable<any>{
    return this.http.get(`${this.baseURL}feedback/getCoachFeedbacks/` + id);
}
// ATTACHMENT SERVICES
addAttachment(formData: any, options: any): Observable<any> {
  return this.http.post<any>(
      this.baseURL + 'attachments',
      formData,
      { params: options, observe: 'response' }
    );
}
getAttachment(params:any):Observable<any>{
  return this.http.get(`${this.baseURL}attachments/get-by-session-id`,{
      params: params,
      observe:'response'})
}



//PAYMENT SERVICES
recordPayment(payment: any): Observable<any> {
    return this.http.post<any>(
        this.baseURL + 'wallet',
        payment,
        { observe: 'response' }
      );
  }

  getPaymentsByCoachId(options: any): Observable<any> {
    return this.http.get(`${this.baseURL}wallet/filterByCoachId`,{
        params: options,
        observe:'response'})
  }
  getPaymentsByClientId(options: any): Observable<any> {
    return this.http.get(`${this.baseURL}wallet/filter-by-client-id`,{
        params: options,
        observe:'response'})
  }
  getPaymentsByClientIdAndCoachId(options: any): Observable<any> {
    return this.http.get(`${this.baseURL}wallet/filterByClientIdAndCoachId`,{
        params: options,
        observe:'response'})
  }
  getPaymentsByOrgId(options: any): Observable<any> {
    return this.http.get(`${this.baseURL}wallet/filterByOrgId`,{
        params: options,
        observe:'response'})
  }
  getPaymentsByOrgIdAndClientId(options: any): Observable<any> {
    return this.http.get(`${this.baseURL}wallet/filterByOrgIdAndClientId`,{
        params: options,
        observe:'response'})
  }
  // get payment based on user logged in
  getPaymentsByUser(options: any): Observable<any> {
    return this.http.get(`${this.baseURL}wallet`,{
        params: options,
        observe:'response'})
  }
   //getPaymentByCoachIdAndSelectedPeriod
    getPaymentsByCoachIdAndSelectedPeriod(options: any): Observable<any> {
        return this.http.get(`${this.baseURL}wallet/filterByCoachIdAndStatementPeriod`,{
            params: options,
            observe:'response'})
      }
//getPaymentByClientIdAndSelectedPeriod
    getPaymentsByClientIdAndSelectedPeriod(options: any): Observable<any> {
        return this.http.get(`${this.baseURL}wallet/filterByClientIdAndStatementPeriod`,{
            params: options,
            observe:'response'})
            
      }
//getPaymentByOrgIdAndSelectedPeriod
    getPaymentsByOrgIdAndSelectedPeriod(options: any): Observable<any> {
        return this.http.get(`${this.baseURL}wallet/filterByOrganizationIdAndStatementPeriod`,{
            params: options,
            observe:'response'})
      }


  //get statement by coach id
    getAccountStatementByCoachId(options: any): Observable<any> {
        return this.http.get(`${this.baseURL}statement/filterByCoachId`,{
            params: options,
            observe:'response'})
      }
//get statement by client id
    getAccountStatementByClientId(options: any): Observable<any> {
        return this.http.get(`${this.baseURL}statement/filterByClientId`,{
            params: options,
            observe:'response'})
      }
//get statement by organization id
    getAccountStatementByOrgId(options: any): Observable<any> {
        return this.http.get(`${this.baseURL}statement/filterByOrgId`,{
            params: options,
            observe:'response'})
      }
//get statement by organization id and client id
    getAccountStatementByOrgIdAndClientId(options: any): Observable<any> {
        return this.http.get(`${this.baseURL}statement/filterByOrgIdAndClientId`,{
            params: options,
            observe:'response'})
      }
//get statement by coach id and client id
    getAccountStatementByCoachIdAndClientId(options: any): Observable<any> {
        return this.http.get(`${this.baseURL}statement/filterByCoachIdAndClientId`,{
            params: options,
            observe:'response'})
      }
      //getAccountStatementByCoachIdAndSelectedPeriod
      getAccountStatementByCoachIdAndStatementPeriod(options: any): Observable<any> {
        return this.http.get(`${this.baseURL}statement/filterByCoachIdAndStatementPeriod`,{
            params: options,
            observe:'response'})
      }
      //getAccountStatementByClientIdAndSelectedPeriod
      getAccountStatementByClientIdAndStatementPeriod(options: any): Observable<any> {
        return this.http.get(`${this.baseURL}statement/filterByClientIdAndStatementPeriod`,{
            params: options,
            observe:'response'})
      }
      //getAccountStatementByOrgIdAndSelectedPeriod
      getAccountStatementByOrgIdAndStatementPeriod(options: any): Observable<any> {
        return this.http.get(`${this.baseURL}statement/filterByOrgIdAndStatementPeriod`,{
            params: options,
            observe:'response'})
      }


      //contact us  message
  contactUsMessage(message: any): Observable<any> {
    return this.http.post<any>(
      this.baseURL + '/registration/contact',
      message,
      {
        observe: 'response',
      }
    );
  }
    //save settings service
    saveSettings(settingsObject: any): Observable<any> {
        return this.http.put<any>(
          this.baseURL + '/settings',
          settingsObject,
          { observe: 'response' }
        );
      }

  //notification service

  getNotificationsbySessionId(options: any): Observable<any> {
    return this.http.get<any>(
      this.baseURL + '/notification/filter-by-session-id',
      {
        params: options,
        observe: 'response',
      }
    );
  }
  getNotificationsbyCoachId(options: any): Observable<any> {
    return this.http.get<any>(
      this.baseURL + '/notification/filter-by-coach-id',
      {
        params: options,
        observe: 'response',
      }
    );
  }
  getNotificationsbyClientId(options: any): Observable<any> {
    return this.http.get<any>(
      this.baseURL + '/notification/filter-by-client-id',
      {
        params: options,
        observe: 'response',
      }
    );
  }
  getAllNotifications(options: any): Observable<any> {
    return this.http.get<any>(
      this.baseURL + '/notification',
      {
        params: options,
        observe: 'response',
      }
    );
  }
  // change contract status
  changeContractStatus(contractId: any, status: any): Observable<any> {
    var contract:any = {
      id: contractId,
      status: status
  }
    return this.http.put<any>(
      this.baseURL + '/contract/changeContractStatus/' + contractId,contract,
      { observe: 'response' }
    );
  }
}
// {
//     headers: {
//         Authorization: `Bearer ${JSON.parse(sessionStorage.getItem('userDetails') as any).token}`
//       }
// })

