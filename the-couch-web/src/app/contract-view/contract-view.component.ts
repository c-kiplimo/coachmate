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
  constructor(private clientService: ClientService, private router: Router,private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.getAllContracts();
    this.route.params.subscribe(params => {
      const id = params['id'];
      // Retrieve the contract from the database using the id
      this.contracts = this.clientService.getContract(id);
    });
   
  }
  navigateToContractDetail(id: any) {
    console.log("contractId on navigate",id);
    this.contractId = id;
    this.router.navigate(['/contractDetail', id]);


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
}
