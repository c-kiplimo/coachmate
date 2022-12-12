import { Component, OnInit } from '@angular/core';
import { ApiService } from '../services/ApiService';
import { ClientService } from '../services/ClientService';
import { style, animate, transition, trigger } from '@angular/animations';

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
export class ContractViewComponent implements OnInit {
  loading = false;
  contracts: any;
  constructor(private clientService: ClientService) {}

  ngOnInit(): void {
    this.getAllContracts();
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
