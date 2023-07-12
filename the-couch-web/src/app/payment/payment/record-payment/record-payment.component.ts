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
    console.log(this.coachData);
    this.userRole = this.coachData.userRole;
    console.log(this.userRole);

 if(this.userRole == 'COACH'){
    this.coachId = this.coachData.id;
    this.getUser();
    this.getClients(this.page)
 } else if(this.userRole == 'ORGANIZATION'){
    this.getUserOrg();
    this.getOrgClients()
 } else if(this.userRole == 'CLIENT'){
    this.getUserClient();
 }
  

  }
  getUser() {
    this.coachId = this.coachData.id;
    console.log(this.coachId);
  }
  getUserOrg() {
    this.orgId = this.coachData.id;
    console.log(this.orgId);
  }
  getUserClient() {
    this.clientId = this.coachData.id;
    console.log(this.clientId);
  }

  recordPayment() {
    console.log(this.paymentForm);

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
        console.log(response);
        this.successMessage = response.message;
      
        this.errorMessage = '';
        this.router.navigate(['/receipts']);
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
        this.clients = JSON.stringify(response.body);
        console.log("clients",this.clients);
        this.loading = false;
      }, (error: any) => {
        console.log(error);
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
        console.log(response)
        console.log('clients',this.clients)
      }, (error) => {
        console.log(error)
      }
    )
  }

  back() {
    window.history.back();
  }

}

