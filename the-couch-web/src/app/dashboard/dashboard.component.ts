import { Component, OnInit } from '@angular/core';
import { ClientService } from '../services/ClientService';
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  // ClientService: any;
  clients: any;
  sessions:any;
  contracts:any;
  numberOfClients!: number;
  numberOfSessions!:number;
  numberOfContracts!:number;
rightIcon: any;

  constructor(private clientService : ClientService) { }

  ngOnInit(): void {
    this.getClients();
    this.getNoOfSessions();
    this.getNoOfContracts();
  }
  getClients(){
    this.clientService.getClient().subscribe(
      (response: any) => {
        console.log(response)
        this.clients = response
        this.numberOfClients = this.clients.length;
        console.log(this.numberOfClients)
      }, (error: any) => {
        console.log(error)
      }
    )
  

  }
  getNoOfSessions(){
    this.clientService.getSessions().subscribe(
      (response:any) =>{
        console.log(response)
        this.sessions = response
        this.numberOfSessions = this.sessions.length;
        console.log(this.numberOfSessions)
        
      },
      (error: any) => {
        console.log(error)
      }
    )
  }
  getNoOfContracts(){
    this.clientService.getContracts().subscribe(
      (response:any) =>{
        console.log(response)
        this.contracts = response
        this.numberOfContracts = this.contracts.length;
        console.log(this.numberOfContracts)
        
      },
      (error: any) => {
        console.log(error)
      }
    )
  }


}
