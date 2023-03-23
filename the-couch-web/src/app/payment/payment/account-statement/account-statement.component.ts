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
  totalLength: any;
  page: number = 1;
  coachSessionData: any;
  coachData: any;
  userRole: any;
  ClientId: any;
  bills: any;
  billingAccount: any;
  selectedDateRange: any;
  statementPeriod!: string;
  accountStatement: any;
  accountStatements: any;
  
 
  
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
        this.getAccountStatementByCoachIdAndStatementPeriod();
       
       
        
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
        this.getPaymentsByClientId(this.ClientId);
        this.getAccountStatementByClientId(this.ClientId);
  
        
      }
      else if(this.userRole == 'ORGANIZATION'){
        this.getAccountStatementByOrgId();
        this.getAccountStatementByOrgIdAndStatementPeriod();
        
      }
      
    }
    getPaymentsByClientId(id: any){
      window.scroll(0, 0);
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
          this.loading = false;
          this.payments = response.body;
          console.log('payments', this.payments);
        }, (error) => {
          console.log(error);
        }
      )
    }
// get statement by coach id and selected period
    getAccountStatementByCoachIdAndStatementPeriod(){
      window.scroll(0, 0);
      const options = {
        page: 1,
        per_page: this.itemsPerPage,
        status: this.filters.status,
        search: this.filters.searchItem,
        coachId: this.coachData.coach.id,
        statementPeriod: this.statementPeriod,
      };
      this.loading = true;
      this.ClientService. getAccountStatementByCoachIdAndStatementPeriod(options).subscribe(
        (response) => {
          this.loading = false;
          this.accountStatements = response.body.data;
          console.log('Account statement by coach and selected period', this.accountStatements);
        }, (error) => {
          console.log(error);
        }
      )
    }
    // get statement by client id and selected period
    getAccountStatementByClientIdAndStatementPeriod(id: any){
      window.scroll(0, 0);
      const options = {
        page: 1,
        per_page: this.itemsPerPage,
        status: this.filters.status,
        search: this.filters.searchItem,
        client_id: id,
        statementPeriod: this.statementPeriod,
      };
      this.loading = true;
      this.ClientService. getAccountStatementByClientIdAndStatementPeriod(options).subscribe(
        (response) => {
          this.loading = false;
          this.accountStatements = response.body.data;
          console.log('Account statement by client and selected period', this.accountStatements);
        }, (error) => {
          console.log(error);
        }
      )
    }
    // get statement by org id and selected period
    getAccountStatementByOrgIdAndStatementPeriod(){
      window.scroll(0, 0);
      const options = {
        page: 1,
        per_page: this.itemsPerPage,
        status: this.filters.status,
        search: this.filters.searchItem,
        orgId: this.coachData.organization.id,
        statementPeriod: this.statementPeriod,
      };
      this.loading = true;
      this.ClientService. getAccountStatementByOrgIdAndStatementPeriod(options).subscribe(
        (response) => {
          this.loading = false;
          this.accountStatements = response.body.data;
          console.log('Account statement by org and selected period', this.accountStatements);
        }, (error) => {
          console.log(error);
        }
      )
    }
   
    

  // get statement by client id
    getAccountStatementByClientId(id: any){
      window.scroll(0, 0);
     
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
      switch (this.statementPeriod) {
        case 'PerMonth':
          // apply filter for 1 month
          if (this.coachData.coach) {
            this.getAccountStatementByCoachIdAndStatementPeriod();
          } else if (this.coachData.organization) {
            this.getAccountStatementByOrgIdAndStatementPeriod();
          } else if (this.coachData.client) {
            this.getAccountStatementByClientIdAndStatementPeriod(this.coachData.client.id);
          }
          break;
        case 'Per6Months':
          // apply filter for 6 months
          if (this.coachData.coach) {
          this.getAccountStatementByCoachIdAndStatementPeriod();
          } else if (this.coachData.organization) {
            this.getAccountStatementByOrgIdAndStatementPeriod();
          } else if (this.coachData.client) {
            this.getAccountStatementByClientIdAndStatementPeriod(this.coachData.client.id);
          }
          break;
        case 'PerYear':
          // apply filter for 1 year
          if (this.coachData.coach) {
          this.getAccountStatementByCoachIdAndStatementPeriod();
          } else if (this.coachData.organization) {
            this.getAccountStatementByOrgIdAndStatementPeriod();
          } else if (this.coachData.client) {
            this.getAccountStatementByClientIdAndStatementPeriod(this.coachData.client.id);
          }
          break;
        default:
          // no filter applied
    
          break;
      }
    }
  }
  

  
