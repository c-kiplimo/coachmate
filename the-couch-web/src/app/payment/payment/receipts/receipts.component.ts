import { Component, OnInit } from '@angular/core';
import { ClientService } from 'src/app/services/ClientService';
import { ActivatedRoute, Router } from '@angular/router';
import { style, animate, transition, trigger } from '@angular/animations';
import html2canvas from 'html2canvas';
import jsPDF from 'jspdf';
import { ParseSourceFile } from '@angular/compiler';
import { th } from 'date-fns/locale';



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

loadingpayment: any;
coachId: any;
orgId : any;
loading = true;
page: number =0;
itemsPerPage = 20;
filters: any = {
  period: '',
  search: '',
};
date!: any;
coachSessionData: any;
user: any;
payments: any ;
showFilters = false;
organizationData: any;
userRole: any;
clientId: any;
payment: any;
  client: any;
  User: any;
statementPeriod = ['PER_MONTH','PER_6_MONTHS','PER_YEAR',]
pageSize: number =15;
totalElements: any;
currentPage: string|number|undefined;
today: Date = new Date();

  constructor(
    private clientService: ClientService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.user = JSON.parse(this.coachSessionData);
    this.userRole = this.user.userRole;
      
    if (this.userRole == 'ORGANIZATION') {
      this.orgId = this.user.organization.id;
      this.getAllPayments(this.page);

    } else if (this.userRole == 'COACH') {
      this.coachId = this.user.id;
      this.clientId = this.user.clientId;
      this.getAllPayments(this.page);

    } else if (this.userRole == 'CLIENT') {
      this.clientId = this.user.id;
      this.getAllPayments(this.page);
    }
  }
  toggleFilters() {
    this.showFilters = !this.showFilters;
  }

  filterByStatus(event: any): any {
    this.page = 0;
    this.filters.status = event.target.value;
    this.getAllPayments(this.page);
  }

  search() {
    this.page = 0;
    this.getAllPayments(this.page);
  }
  resetPeriod(): void {
    this.filters.period = '';
    this.getAllPayments(this.page);
  }


    
  getAllPayments(page: any) {
    this.loading = true;
    this.page = page;
    //if page is 0, don't subtract 1
    if (page === 0 || page < 0) {
      page = 0;
    } else {
      page = page - 1;
    }
    const options: any = {
      page: page,
      size: this.pageSize,
      statementPeriod: this.filters.period,
      search: this.filters.search,
      sort: 'id,desc',
    };
    if(this.userRole == 'COACH'){
      options.coachId = this.coachId;
    }else if(this.userRole == 'CLIENT'){
      options.clientId = this.clientId;
    }else if(this.userRole == 'ORGANIZATION'){
      options.organizationId = this.orgId;
    }
    this.clientService.getPayments(options).subscribe(
      (response: any) => {
        this.payments = response.body;
        for (let i = this.payments.length - 1; i >= 0; i--) {
          if (this.payments[i].modeOfPayment === null) {
            this.payments.splice(i, 1);
          }
      }
        this.totalElements = + response.headers.get('X-Total-Count');
        this.loading = false;
      },
      (error: any) => {
        this.loading = false;
      }
    );
  }
  filterByClientName() {
    
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      search: this.filters.searchItem,
  
    };
    console.log('options', options);
    this.clientService.filterByClientName(options).subscribe(
      (response) => {
        this.loading = false;
        this.payments = response.body;
      }, (error) => {
        this.loading = false;
      }
    )
  }
  // getPaymentsByClientId(id: any){
  //   const options = {
  //     page: 1,
  //     per_page: this.itemsPerPage,
  //     // status: this.filters.status,
  //     // search: this.filters.searchItem,
  //     clientId: id,
  //   };

  //   this.loading = true;
  //   this.ClientService.getPaymentsByClientId(options).subscribe(
  //     (response) => {
  //       this.loading = false;
  //       this.payments = response.body.data;
  //       console.log('payments', this.payments);
  //     }, (error) => {
  //       console.log(error);
  //     }
  //   )
  // }
  // // searchPayments() {
  // //   this.getPaymentsByClientId(this.ClientId);
  // // }
  // getPaymentsByCoachId(){
  
  //   const options = {
  //     page: 1,
  //     per_page: this.itemsPerPage,
  //     coachId: this.coachId,
      
  //   };

  //   this.loading = true;
  //   this.ClientService.getPaymentsByCoachId(options).subscribe(
  //     (response) => {
  //         this.loading = false;
  //         this.payments = response.body.data;
  //         console.log('payments', this.payments);
  //       }, (error) => {
  //       console.log(error);
  //     }
  //   )
  // }

  // TODO: retrieve client by id from payment and display in invoice
  getClient(id: any) {
    this.loading = true;
    this.clientService.getOneClient(id).subscribe(
      (response: any) => {
        this.client = response.body;
        this.loading = false;
      },
      (error: any) => {
        this.loading = false;
      }
    );
  }
  


  viewInvoice(payment: any): void {
    this.payment = payment;
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
