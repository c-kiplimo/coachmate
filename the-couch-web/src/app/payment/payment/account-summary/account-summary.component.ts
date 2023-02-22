import { Component, OnInit } from '@angular/core';
import { ClientService } from 'src/app/services/ClientService';

@Component({
  selector: 'app-account-summary',
  templateUrl: './account-summary.component.html',
  styleUrls: ['./account-summary.component.css']
})
export class AccountSummaryComponent implements OnInit {
  coachSessionData!: any;
  coachData: any;
  userRole: any;
  User: any;
  orgName!: any;
totalDue: any;
amountDeposited: any;
invoicesPiad: any;
invoicesPaid: any;

  constructor( private clientService:ClientService) { }

  ngOnInit(): void {
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);
    this.userRole = this.coachData.userRole;
    console.log(this.userRole);

    if(this.userRole == 'COACH'){
    
    this.getUser();
 

    } else if(this.userRole == 'ORGANIZATION'){
      console.log('ORGANIZATION');
      this.getUserOrg();
     
    }else if(this.userRole == 'CLIENT'){
      console.log('not coach');
      this.getUser();
    }


  }
  getClients(id: any) {
    this.clientService.getClient(id).subscribe((data: any) => {
      console.log(data);
      this.totalDue = data.totalDue;
      this.amountDeposited = data.amountDeposited;
      this.invoicesPiad = data.invoicesPiad;
      this.invoicesPaid = data.invoicesPaid;
    });
  }
  
  getUser() {
    this.User = JSON.parse(sessionStorage.getItem('user') as any);
    console.log(this.User);
  }
  getUserOrg() {
    this.User = JSON.parse(sessionStorage.getItem('user') as any);
    console.log(this.User);
    this.getOrganization(this.User.id);

  }
  getOrganization(id: any) {
    this.clientService.getOrganization(id).subscribe((data: any) => {
      console.log(data);
      this.orgName = data.name;
      console.log(this.orgName);
      this.getOrgClients(this.orgName);
    });
 

 
  }
  getOrgClients(orgName: any) {
    this.clientService.getOrgClients(orgName).subscribe((data: any) => {
      console.log(data);
      this.totalDue = data.totalDue;
      this.amountDeposited = data.amountDeposited;
      this.invoicesPiad = data.invoicesPiad;
      this.invoicesPaid = data.invoicesPaid;
    });
  }

}
