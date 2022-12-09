import { Component, OnInit } from '@angular/core';
import { ApiService } from '../services/ApiService';
import { ClientService } from '../services/ClientService';

@Component({
  selector: 'app-contract-view',
  templateUrl: './contract-view.component.html',
  styleUrls: ['./contract-view.component.css']
})
export class ContractViewComponent implements OnInit {
  contracts:any;
  constructor(private clientService:ClientService) { }

  ngOnInit(): void {
    this.getAllContracts();
  }
getAllContracts(){
  this.clientService.getContracts().subscribe(
    (response: any) => {
      console.log(response)
      this.contracts = response
    }, (error: any) => {
      console.log(error)
    }
  )
}
}
