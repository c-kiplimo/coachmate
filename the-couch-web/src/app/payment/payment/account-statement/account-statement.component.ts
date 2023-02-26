import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ClientService } from 'src/app/services/ClientService';
import html2canvas from 'html2canvas';
import jsPDF from 'jspdf';
import { id } from 'date-fns/locale';

@Component({
  selector: 'app-account-statement',
  templateUrl: './account-statement.component.html',
  styleUrls: ['./account-statement.component.css']
})
export class AccountStatementComponent implements OnInit {
  payment: any;
  payments: any;
  loadingpayment: any;
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
  bills: any;
  billingAccount: any;
  selectedDateRange: any;
  selectedPeriod: any;
  accountStatement: any;
  
 
  
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
      if (this.userRole == 'CLIENT') {
     
      }
      if(this.userRole == 'COACH'){
        this.getPaymentsByCoachId();
        this.getAccountStatementByCoachId();
       
        
      } else if(this.userRole == 'CLIENT'){
      
       const email = {
        email: this.coachData.email
      }
      this.ClientService.getClientByEmail(email).subscribe(
        (response: any) => {
          console.log(response);
         this.ClientId = response[0].id;
          
  
        this.getPaymentsByClientId(response[0].id);
        this.getAccountStatementByClientId(response[0].id);
       
        },
        (error: any) => {
          console.log(error);
        }
      );
     
  
      }
      else if(this.userRole == 'CLIENT'){
        this.getAccountStatementByCoachId();
        
      }
      else if(this.userRole == 'ORGANIZATION'){
        this.getAccountStatementByOrgId();
        
      }
      
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
          this.loading = false;
          this.payments = response.body;
          console.log('payments', this.payments);
        }, (error) => {
          console.log(error);
        }
      )
    }
// get statement by coach id
    getAccountStatementByCoachId(){
      const options = {
        page: 1,
        per_page: this.itemsPerPage,
        status: this.filters.status,
        search: this.filters.searchItem,
        coachId: this.coachData.coach.id,
      };
      this.loading = true;
      this.ClientService.getAccountStatementByCoachId(options).subscribe(
        (response) => {
          this.loading = false;
          this.accountStatement = response.body;
          console.log('get statement by coach id', this.accountStatement);
        }, (error) => {
          console.log(error);
        }
      )
    }
  // get statement by client id
    getAccountStatementByClientId(id: any){
      const options = {
        page: 1, 
        per_page: this.itemsPerPage,
        status: this.filters.status,
        search: this.filters.searchItem,
        client_id: id,
      };
      this.loading = true;
      this.ClientService. getAccountStatementByClientId(options).subscribe(
        (response) => {
          this.loading = false;
          this.accountStatement = response.body;
          console.log('get statement by client id', this.accountStatement);
        }, (error) => {
          console.log(error);
        }
      )
    }
  // get statement by coach id and client id
    getAccountStatementByCoachIdAndClientId(id: any){
      const options = {
        page: 1,
        per_page: this.itemsPerPage,
        status: this.filters.status,
        search: this.filters.searchItem,
        client_id: id,
        coachId: this.coachData.coach.id,
      };
      this.loading = true;
      this.ClientService. getAccountStatementByCoachIdAndClientId(options).subscribe(
        (response) => {
          this.loading = false;
          this.accountStatement = response.body.data;
          console.log('Account statement by coach and client id', this.accountStatement);
        }, (error) => {
          console.log(error);
        }
      )
    }
  // get statement by org id
    getAccountStatementByOrgId(){
      const options = {
        page: 1,
        per_page: this.itemsPerPage,
        status: this.filters.status,
        search: this.filters.searchItem,
        orgId: this.coachData.organization.id,
      };
      this.loading = true;
      this.ClientService. getAccountStatementByOrgId(options).subscribe(
        (response) => {
          this.loading = false;
          this.accountStatement = response.body.data;
          console.log('Account statement by organization', this.accountStatement);
        }, (error) => {
          console.log(error);
        }
      )
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
      let DATA: any = document.getElementById('statementDesktop');
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
    applyFilter() {
      switch (this.selectedPeriod) {
        case '1':
          // apply filter for 1 month
          break;
        case '2':
          // apply filter for 6 months
          break;
        case '3':
          // apply filter for 1 year
          break;
        default:
          // no filter applied
          break;
      }
    }
    // applyFilter() {
    //   const now = new Date();
    //   const startDate = new Date(now);
    //   if (this.selectedPeriod === '1') { // 1 month
    //     startDate.setMonth(now.getMonth() - 1);
    //   } else if (this.selectedPeriod === '2') { // 6 months
    //     startDate.setMonth(now.getMonth() - 6);
    //   } else if (this.selectedPeriod === '3') { // 1 year
    //     startDate.setFullYear(now.getFullYear() - 1);
    //   }
    //   const endDate = now;
      
    //   const filteredPayments = this.payments.filter(payment => {
    //     const paymentDate = new Date(payment.createdAt);
    //     return paymentDate >= startDate && paymentDate <= endDate;
    //   });
    
    //   // do something with filteredPayments
    // }
    
  
  
  }
  
