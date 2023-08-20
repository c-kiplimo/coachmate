import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ro } from 'date-fns/locale';
import { ClientService } from 'src/app/services/ClientService';
@Component({
  selector: 'app-record-payment',
  templateUrl: './record-payment.component.html',
  styleUrls: ['./record-payment.component.css']
})
export class RecordPaymentComponent implements OnInit {
  clients: any;
  clientId: any;
  orgId: any;
  coachId: any;
  modesOfPayment ='';
  coachSessionData!: any;
  coachData: any;
  userRole: any;
  User: any;
  orgName!: any;
  successMessage!: string;
  errorMessage!: string;
  itemsPerPage = 20;
  filters: any = {
    status: '',
    searchItem: '',
  };

  loading = false;
  page: any;


  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private clientService:ClientService
  
  ) {}

  paymentForm = {
    clientId: '',
    amount: '',
    modeOfPayment: 'MANUAL',
    extPaymentRef: '',
    paymentCurrency: '',
    description: '',
    sendNotification: true,

    coachId: '',
    organizationId: '',
  }
  


  ngOnInit() {
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    this.userRole = this.coachData.userRole;

 if(this.userRole == 'COACH'){
    this.coachId = this.coachData.id;
    this.getUser();
    this.getClients(this.page)
 } else if(this.userRole == 'ORGANIZATION'){
    this.coachId = this.coachData.id;
    this.getUserOrg();
    // this.getOrgClients()
    this.getClientsOrg(this.page);
 } else if(this.userRole == 'CLIENT'){
    this.getUserClient();
 }
  

  }
  getUser() {
    this.coachId = this.coachData.id;
  }
  getUserOrg() {
    this.orgId = this.coachData.id;
  }
  getUserClient() {
    this.clientId = this.coachData.id;
  }

  recordPayment() {
    const data = this.paymentForm;
    if(this.userRole == 'CLIENT'){
      data.clientId = this.clientId;  
    } else if(this.userRole == 'COACH'){
      data.coachId = this.coachId;
    } else if(this.userRole == 'ORGANIZATION'){
      data.organizationId = this.orgId;
    }


    this.clientService.recordPayment(this.paymentForm).subscribe(
      (response: any) => {
        this.successMessage = response.message;
      
        this.errorMessage = '';
        // this.router.navigate(['/receipts']);
        this.back();
      }
    );


  }

  getClients(page: any) {
    this.loading = true;
    this.page = page;

    if (page === 0 || page < 0) {
      page = 0;
    } else {
      page = page - 1;
    }

    const options = {
      page: page,
      status: this.filters.status,
      search: this.filters.searchItem,
      coachId: this.coachId,
    };

    this.clientService.getClients(options).subscribe(
      (response: any) => {
        this.clients = response.body;
        this.loading = false;
      }, (error: any) => {
        this.loading = false;
      }
    );
  }

  getOrgClients(){
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
    };
    const id = this.coachData.id;
    this.loading = true;
    this.clientService.getOrgClients(id).subscribe(
      (response) => {
        this.loading = false;
        this.clients = response.body;
      }, (error) => {
        this.loading = false;
      }
    )
  }

  getClientsOrg(page: any) {
    this.loading = true;
    this.page = page;

    if (page === 0 || page < 0) {
      page = 0;
    } else {
      page = page - 1;
    }

    const options = {
      page: page,
      status: this.filters.status,
      search: this.filters.searchItem,
      orgId: this.orgId,
    };

    this.clientService.getClients(options).subscribe(
      (response: any) => {
        this.clients = response.body;
        for (let client of this.clients) {
          if (client.userRole != 'CLIENT') {
            this.clients.splice(this.clients.indexOf(client), 1);
          }
        }
        this.loading = false;
      }, (error: any) => {
        this.loading = false;
      }
    );
  }

    
  back() {
    window.history.back();
  }

}

