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
  constructor(private restApiService:ClientService) { }

  ngOnInit(): void {
    this.restApiService.getContracts().subscribe(
      (response: any) => {
        console.log(response)
        this.contracts = response
      }, (error: any) => {
        console.log(error)
      }
    )
  }

}
