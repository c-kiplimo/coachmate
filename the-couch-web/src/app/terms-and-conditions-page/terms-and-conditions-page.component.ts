import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ClientService } from '../services/ClientService';
@Component({
  selector: 'app-terms-and-conditions-page',
  templateUrl: './terms-and-conditions-page.component.html',
  styleUrls: ['./terms-and-conditions-page.component.css']
})

export class TermsAndConditionsPageComponent implements OnInit {
  
  
  agreedToTerms: boolean = false;
  contract: any;
  User: any;
  loading = false;
  contracts: any;
  contractId: any;
  coachSessionData: any;
  coachData: any;
  userRole: any;
  OrgData: any;
  orgSession: any;

  constructor(private clientService: ClientService, private router: Router,private route: ActivatedRoute, private toastrService: ToastrService,) {}
  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];
    console.log("contractId here",id);
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);
    this.userRole = this.coachData.userRole;
    console.log(this.userRole);

    if(this.userRole == 'COACH'){
    this.getAllContracts();
    this.route.params.subscribe(params => {
      const id = params['id'];
      console.log("contractId here",id);
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

  
  getSessionsBycontractId(){
    console.log(this.contractId);
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
  navigateToContractDetail() {
    const id = this.route.snapshot.params['id'];
    console.log("contractId on navigate",id);
    this.contractId = id;
    this.router.navigate(['/contractDetail', id]);


  }
  onCheckboxChange() {
    if (this.agreedToTerms) {
      const id = this.route.snapshot.params['id'];
      console.log("contractId on navigate",id);
      this.contractId = id;
      let data = {
       
        status: 'SIGNED',
    }
      console.log("data here",data);
    console.log("contractId on checkbox",this.contractId);
      this.clientService.changeContractStatus(this.contractId, data).subscribe(
        (response) => {
          console.log(response);
          this.toastrService.success('Status Changed successfully');
        },
        (error) => {
          console.log(error);
          this.toastrService.error(error.message);
        }
      );
    }
  }
  

}



