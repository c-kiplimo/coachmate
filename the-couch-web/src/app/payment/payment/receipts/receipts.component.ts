import { Component, OnInit } from '@angular/core';
import { ClientService } from 'src/app/services/ClientService';
import { Router } from '@angular/router';
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
payments: any = [];
loadingpayment: any;
statementPeriod!: string;
coachId: any;
organizationId : any;
loading = false;
itemsPerPage = 20;
filters: any = {
  status: '',
  searchItem: '',
};
coachSessionData: any;
user: any;
organizationData: any;
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
    console.log(this.coachSessionData)
    this.user = JSON.parse(this.coachSessionData);
    this.userRole = this.user.userRole;
    console.log(this.userRole);

    if(this.userRole == 'COACH'){
      this.coachId = this.user.id;
      this.getPaymentsByCoachId();

    } else if(this.userRole == 'CLIENT'){
      console.log(this.client);
      this.ClientId = this.user.id;
      this.getPaymentsByClientId(this.ClientId);

    }
    else if(this.userRole == 'ORGANIZATION'){
      this.organizationId= this.user.id;
      this.getPaymentsByOrganizationId();
    }
  }
  getPaymentsByOrganizationId() {
    
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
      orgId: this.user.organization.id,
    };
    console.log('orgId', options);
    this.ClientService.getPaymentsByOrganizationId(options).subscribe(
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
      // status: this.filters.status,
      // search: this.filters.searchItem,
      clientId: id,
    };

    this.loading = true;
    this.ClientService.getPaymentsByClientId(options).subscribe(
      (response) => {
        this.loading = false;
        this.payments = response.body.data;
        console.log('payments', this.payments);
      }, (error) => {
        console.log(error);
      }
    )
  }
  // searchPayments() {
  //   this.getPaymentsByClientId(this.ClientId);
  // }

  getPaymentsByCoachId(){
    
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      coachId: this.coachId,
      
    };

    this.loading = true;
    this.ClientService.getPaymentsByCoachId(options).subscribe(
      (response) => {
          this.loading = false;
          this.payments = response.body.data;
          console.log('payments', this.payments);
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
      // get payment by client id and selected period
      getPaymentsByCoachIdAndSelectedPeriod(){
        
        const options = {
          page: 1,
          per_page: this.itemsPerPage,
          status: this.filters.status,
          search: this.filters.searchItem,
          coachId: this.user.coach.id,
          statementPeriod: this.statementPeriod,
        };
        this.loading = true;
        this.ClientService.getPaymentsByCoachIdAndSelectedPeriod(options).subscribe(
          (response) => {
            this.loading = false;
            this.payments = response.body.data;
            console.log('Account statement by client and selected period', this.payments);
          }, (error) => {
            console.log(error);
          }
        )
      }
      // get payment by client id and selected period
      getPaymentsByClientIdAndSelectedPeriod(id: any){
        
        const options = {
          page: 1,
          per_page: this.itemsPerPage,
          status: this.filters.status,
          search: this.filters.searchItem,
          client_id: id,
          statementPeriod: this.statementPeriod,
        };
        this.loading = true;
        this.ClientService.getPaymentsByClientIdAndSelectedPeriod(options).subscribe(
          (response) => {
            this.loading = false;
            this.payments = response.body.data;
            console.log('Account statement by client and selected period', this.payments);
          }, (error) => {
            console.log(error);
          }
        )
      }
      // get payment by org id and selected period
      getPaymentsByOrgIdAndSelectedPeriod(){
        
        const options = {
          page: 1,
          per_page: this.itemsPerPage,
          status: this.filters.status,
          search: this.filters.searchItem,
          org_id: this.user.organization.id,
          statementPeriod: this.statementPeriod,
        };
        this.loading = true;
        this.ClientService.getPaymentsByOrgIdAndSelectedPeriod(options).subscribe(
          (response) => {
            this.loading = false;
            this.payments = response.body.data;
            console.log('Account statement by org and selected period', this.payments);
          }, (error) => {
            console.log(error);
          }
        )
      }


  applyFilter() {
    switch (this.statementPeriod) {
      case 'PerMonth':
        // apply filter for 1 month
        if (this.user.coach) {
          this.getPaymentsByCoachIdAndSelectedPeriod();
        } else if (this.user.organization) {
          this.getPaymentsByOrgIdAndSelectedPeriod();
        } else if (this.user.client) {
          this.getPaymentsByClientIdAndSelectedPeriod(this.user.client.id);
        }
        break;
      case 'Per6Months':
        // apply filter for 6 months
        if (this.user.coach) {
        this.getPaymentsByCoachIdAndSelectedPeriod();
        } else if (this.user.organization) {
          this. getPaymentsByOrgIdAndSelectedPeriod();
        } else if (this.user.client) {
          this.getPaymentsByClientIdAndSelectedPeriod(this.user.client.id);
        }
        break;
      case 'PerYear':
        // apply filter for 1 year
        if (this.user.coach) {
        this.getPaymentsByCoachIdAndSelectedPeriod();
        } else if (this.user.organization) {
          this. getPaymentsByOrgIdAndSelectedPeriod();
        } else if (this.user.client) {
          this.getPaymentsByClientIdAndSelectedPeriod(this.user.client.id);
        }
        break;
      default:
        // no filter applied
  
        break;
    }
  }




}
