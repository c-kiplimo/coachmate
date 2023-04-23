import { Component, OnInit } from '@angular/core';
import { ClientService } from '../services/ClientService';
import { style, animate, transition, trigger } from '@angular/animations';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
@Component({
  selector: 'app-contract-view',
  templateUrl: './contract-view.component.html',
  styleUrls: ['./contract-view.component.css'],
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
export class contractViewComponent implements OnInit {

  loading = false;
  contracts!: any;
  contractId: any;
  coachSessionData: any;
  coachData: any;
  userRole: any;
  contract:any;
  OrgData: any;
  orgSession: any;
  clientId: any;
  User: any;
  Organization: any;
  orgName: any;
  OrgCoaches: any;
  

  constructor(private clientService: ClientService, private router: Router,private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);
    this.userRole = this.coachData.userRole;
    console.log(this.userRole);
  
    if(this.userRole == 'COACH'){
    this.getUser();
    this.getAllContracts();;
    window.scroll(0, 0);
    } else if(this.userRole == 'ORGANIZATION'){
      console.log('ORGANIZATION');
      this.getUserOrg();  
      this.getOrgContracts(this.coachData.organization.id);
     
    }else if(this.userRole == 'CLIENT') {
      console.log('not coach');
      this.getUser();
      this.clientId = this.coachData.client.id;
      this.getClientContracts();
      
    }
  }
  navigateToTerms(id: any) {
    console.log("contractId on navigate",id);
    this.contractId = id;
    if(this.userRole == 'COACH'){

    this.router.navigate(['/contractDetail', id]);
    } else if (this.userRole == 'CLIENT'&& id.contractStatus==null) {
      this.router.navigate(['/terms', id]);
    }else(this.userRole == 'CLIENT'&& id.contractStatus=="SIGNED")
    this.router.navigate(['/contractDetail', id]);


  }
  getUser() {
    this.User = JSON.parse(sessionStorage.getItem('user') as any);
    console.log(this.User);
  }
  getUserOrg() {
    this.User = JSON.parse(sessionStorage.getItem('user') as any);
    console.log(this.User);
    this.Organization = JSON.parse(sessionStorage.getItem('Organization') as any);

  }

  getOrgCoaches(id: any) {
    const data = {
      OrgId: id,
    }
    this.clientService.getOrgCoaches(data).subscribe(
      (response: any) => {
        console.log('here Organization=>', response);
        this.OrgCoaches = response;
        console.log(this.OrgCoaches);
        this.getOrgContracts(this.Organization.id);
       
      },
      (error: any) => {
        console.log(error);
      }
    );
  }

  getOrgContracts(id: any) {
    window.scroll(0, 0);
    this.loading = true;
    this.clientService.getOrgContracts(id).subscribe(
      (response: any) => {
        console.log(response);
        this.contracts = response.body;
        this.loading = false;
      },
      (error: any) => {
        console.log(error);
      }
    );
  }

  getAllContracts() {
    window.scroll(0, 0);
    this.loading = true;
    this.clientService.getContracts().subscribe(
      (response: any) => {
        this.contracts = response.body;
        console.log(response);
        this.loading = false;
      },
      (error: any) => {
        console.log(error);
      }
    );
  }

  getClientContracts() {
    this.clientService.getClientContracts(this.clientId).subscribe(
      (response: any) => {
        this.contracts = response.body;
        console.log('here contracts=>', response);
        this.loading = false;
      
      },
      (error: any) => {
        console.log(error);
      }
    );
  }
 


}
