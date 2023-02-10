import { Component, OnInit } from '@angular/core';

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

  constructor() { }

  ngOnInit(): void {
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);
    this.userRole = this.coachData.userRole;
    console.log(this.userRole);
    if(this.userRole == 'COACH'){
    this.getClients();
    this.getUser();
    this.getNoOfSessions();
    this.getNoOfContracts();
    this.getCoachEducation(this.coachData.id);

    } else if(this.userRole == 'ORGANIZATION'){
      console.log('ORGANIZATION');
      this.getUserOrg();
     

     
    }else {
      console.log('not coach');
      this.getUser();
     
      
    }
    setTimeout(() => {
      location.reload();
    }, 5);

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
    throw new Error('Method not implemented.');
  }

  getClients() {
    throw new Error('Method not implemented.');
  }
 
  getNoOfSessions() {
    throw new Error('Method not implemented.');
  }
  getNoOfContracts() {
    throw new Error('Method not implemented.');
  }
  getCoachEducation(id: any) {
    throw new Error('Method not implemented.');
  }
 
  }


