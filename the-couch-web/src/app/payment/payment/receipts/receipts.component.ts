import { Component, OnInit } from '@angular/core';
import { ClientService } from 'src/app/services/ClientService';
import { Router } from '@angular/router';
import { style, animate, transition, trigger } from '@angular/animations';
import html2canvas from 'html2canvas';
import jsPDF from 'jspdf';
import { ParseSourceFile } from '@angular/compiler';

@Component({
  selector: 'app-receipts',
  templateUrl: './receipts.component.html',
  styleUrls: ['./receipts.component.css'],
  animations: [
    trigger('fadeIn', [
      transition(':enter', [
        // :enter is alias to 'void => *'
        style({ opacity: 0 }),
        animate(600, style({ opacity: 1 })),
      ]),
    ]),
  ],
})

export class ReceiptsComponent implements OnInit {
payments: any = [];
loadingpayment: any;
coachId: any;
loading = false;
itemsPerPage = 20;
filters: any = {
  status: '',
  searchItem: '',
};
coachSessionData: any;
coachData: any;
userRole: any;
ClientId: any;
payment: any;
  client: any;
  User: any;

  constructor(
    private ClientService: ClientService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);
    this.userRole = this.coachData.userRole;
    console.log(this.userRole);

    if(this.userRole == 'COACH'){
      this.coachId = this.coachData.coach.id;
      this.getPaymentsByCoachId();
    } else if(this.userRole == 'CLIENT'){
   
      this.userRole = this.coachData.userRole;
      this.User = sessionStorage.getItem('user');
      this.client = JSON.parse(this.User);
      console.log(this.client);
      this.userRole = this.coachData.userRole;
      this.ClientId = this.client.id;
      this.getPaymentsByClientId(this.ClientId);

    }
    else if(this.userRole == 'ORGANIZATION'){
      console.log('ORGANIZATION');
      this.getPaymentsByOrgId();
    }
  }
  getPaymentsByOrgId() {
    window.scroll(0, 0);
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
      orgId: this.coachData.organization.id,
    };
    this.ClientService.getPaymentsByOrgId(options).subscribe(
      (response) => {
        this.loading = false;
        this.payments = response.body;
        console.log('payments', this.payments);
      }, (error) => {
        console.log(error);
      }
    )
  }
  filterByClientNameAndDate() {
    window.scroll(0, 0);
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      search: this.filters.searchItem,
  
    };
    console.log('options', options);
    this.ClientService.filterByClientNameAndDate(options).subscribe(
      (response) => {
        this.loading = false;
        this.payments = response.body;
        console.log('payments', this.payments);
      }, (error) => {
        console.log(error);
      }
    )
  }
  getPaymentsByClientId(id: any){
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
      client_id: id,
    };

    this.loading = true;
    this.ClientService.getPaymentsByClientId(options).subscribe(
      (response) => {
        this.loading = false;
        this.payments = response.body;
        console.log('payments', this.payments);
      }, (error) => {
        console.log(error);
      }
    )
  }

  getPaymentsByCoachId(){
    window.scroll(0, 0);
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
      coachId: this.coachData.coach.id,
      
    };

    this.loading = true;
    this.ClientService.getPaymentsByCoachId(options).subscribe(
      
    
      (response) => {
        if(response.body.extPaymentRef!==null){
          this.loading = false;
          this.payments = response.body;
          console.log('payments', this.payments);
        }
      }, (error) => {
        console.log(error);
      }
    )
  }
  


  viewInvoice(payment: any): void {
    this.payment = payment;
    console.log(this.payment);
  }
  savePdf() {
    let DATA: any = document.getElementById('invoice');
    html2canvas(DATA, { logging: true, allowTaint: true, useCORS: true }).then(
      (canvas) => {
        let fileWidth = 185;
        // let fileHeight =100;
        let fileHeight = (canvas.height * fileWidth) / canvas.width;
        const FILEURI = canvas.toDataURL('image/*');
        let PDF = new jsPDF('p', 'mm', 'a4');
        let position = 0;
        PDF.addImage(FILEURI, 'image/*', 0, position, fileWidth, fileHeight);
        PDF.save('invoice.pdf');
      }
    );
  }
  savePdfDesktop() {
    let DATA: any = document.getElementById('invoiceDesktop');
    html2canvas(DATA, { logging: true, allowTaint: true, useCORS: true }).then(
      (canvas) => {
        let fileWidth = 200;
        // let fileHeight =50;
        let fileHeight = (canvas.height * fileWidth) / canvas.width;
        const FILEURI = canvas.toDataURL('image/*');
        let PDF = new jsPDF('p', 'mm', 'a4');
        let position = 0;
        PDF.addImage(FILEURI, 'image/*', 0, position, fileWidth, fileHeight);
        PDF.save('invoice.pdf');
      }
    );
  }




}
