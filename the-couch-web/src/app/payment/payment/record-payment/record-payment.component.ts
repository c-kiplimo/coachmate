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
  modesOfPayment ='';
  successMessage!: string;
  errorMessage!: string;
  itemsPerPage = 20;
  filters: any = {
    status: '',
    searchItem: '',
  };

  loading = false;

  coachSessionData: any;
  coachData: any;
  userRole: any;


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
    orgIdId: '',
  }
  


  ngOnInit() {
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);
    this.userRole = this.coachData.userRole;
    console.log(this.userRole);

 if(this.userRole == 'COACH'){
    this.getClients()
 } else if(this.userRole == 'ORGANIZATION'){
    this.getOrgClients()
 }
  
  }

  recordPayment() {
    console.log(this.paymentForm);

    const data = this.paymentForm;
    data.coachId = this.coachData.coach.id;
    data.orgIdId = this.coachData.coach.orgIdId;


    this.clientService.recordPayment(this.paymentForm).subscribe(
      (response: any) => {
        console.log(response);
        this.successMessage = response.message;
        this.errorMessage = '';
        //Router.navigate(['/payment']);
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
    this.clientService.getClient(options).subscribe(
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
        this.clients = response;
        console.log(response)
        console.log('clients',this.clients)
  
  
      }, (error) => {
        console.log(error)
      }
    )
  }

}

