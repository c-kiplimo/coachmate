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
  this.getUser();
    this.getClients()
 } else if(this.userRole == 'ORGANIZATION'){
  this.getUserOrg();
    this.getOrgClients()
 } else if(this.userRole == 'CLIENT'){
  this.getUserClient();
 }
  

  }
  getUser() {
    this.User = JSON.parse(sessionStorage.getItem('user') as any);
    console.log(this.User);
    this.coachId = this.User.coach.id;
  }
  getUserOrg() {
    this.User = JSON.parse(sessionStorage.getItem('user') as any);
    console.log(this.User);
    this.orgId = this.User.organization.id;
  }
  getUserClient() {
    this.User = JSON.parse(sessionStorage.getItem('user') as any);
    console.log(this.User);
    this.clientId = this.User.id;

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
  getClients() {
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
    };
    this.clientService.getClients(options).subscribe(
      (response: any) => {
        console.log('here clients=>', response.body.data);
        this.clients = response.body.data;

      },
      (error: any) => {
        console.log(error);
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

}

