import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable, throwError, VirtualTimeScheduler } from 'rxjs';
import { map, catchError} from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { faEyeDropper } from '@fortawesome/free-solid-svg-icons';
 
 
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
    
    constructor(private http: HttpClient) {

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
        return this.http.post(`${this.baseURL}clients`, client)
    } 
    suspendClient(clientData: any): Observable<any> {
        return this.http.put(`${this.baseURL}clients`, clientData)
    }


    editClient(id: any, client: any): Observable<any> {
        client.id = id;
        return this.http.put(`${this.baseURL}clients/${id}`, client)
    }
//service to change client status



   

    changeClientStatus(clientId: any, status: any): Observable<any> {
        var client:any = {
            id: clientId,
            status: status
        }
    
        return this.http.put(`${this.baseURL}clients/changeStatus/`+ clientId, client)
        "/change-status/{id}"
    }

     
    // Get Contracts
    getContracts() : Observable<any> {
        return this.http.get(`${this.baseURL}contracts`)
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
        console.log(session)
     
        return this.http.post(`${this.baseURL}sessions`, session, {params: params})
            
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
  //get billings by coach id
    getBillingsByCoachId(options: any): Observable<any> {
        return this.http.get(`${this.baseURL}billing/filterByCoachId`,{
            params: options,
            observe:'response'})
      }


}
// {
//     headers: {
//         Authorization: `Bearer ${JSON.parse(sessionStorage.getItem('userDetails') as any).token}`
//       }
// })

