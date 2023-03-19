import { Component, OnInit } from '@angular/core';
import { ApiService } from '../services/ApiService';
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
  contracts: any;
  contractId: any;
  coachSessionData: any;
  coachData: any;
  userRole: any;
  contract:any;
  OrgData: any;
  orgSession: any;

  User: any;
  

  constructor(private clientService: ClientService, private router: Router,private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);
    this.userRole = this.coachData.userRole;
    console.log(this.userRole);
    window.scroll(0, 0);

  
   


    if(this.userRole == 'COACH'){
    this.getAllContracts();
    this.route.params.subscribe(params => {
      const id = params['id'];
      // Retrieve the contract from the database using the id
      this.contracts = this.clientService.getContract(id);
    });
  } else if (this.userRole == 'ORGANIZATION') {
    this.OrgData = sessionStorage.getItem('Organization');
    this.orgSession = JSON.parse(this.OrgData);
    console.log(this.orgSession);
    
    this.getOrgContracts(this.orgSession.id);
  } else if (this.userRole == 'CLIENT') {

    this.User = JSON.parse(sessionStorage.getItem('user') as any);
      console.log(this.User);
      const email = {
        email: this.User.email
      }
      this.clientService.getClientByEmail(email).subscribe(
        (response: any) => {
          console.log(response);
          this.getClientContracts(response[0].id);
        },
        (error: any) => {
          console.log(error);
        }
      );

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
  getSessionsBycontractId(){
    console.log(this.contractId);
    window.scroll(0, 0);
    this.loading = true;
    this.clientService.getSessionsBycontractId(this.contractId).subscribe(
      (response: any) => {
        console.log(response);
        this.contracts = response;
        this.loading = false;
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
        this.contracts = response;
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
        console.log(response);
        this.contracts = response;
        this.loading = false;
      },
      (error: any) => {
        console.log(error);
      }
    );
  }

  getClientContracts(id: any) {
    // const data = {
    //   clientId: id,
    // }
    window.scroll(0, 0);
    this.clientService.getClientContracts(id).subscribe(
      (response: any) => {
        console.log('here contracts=>', response);
        this.contracts = response;
        console.log(this.contracts);
      
      },
      (error: any) => {
        console.log(error);
      }
    );
  }
 


}
