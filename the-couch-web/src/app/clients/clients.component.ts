import { Component, OnInit } from '@angular/core';
import { ClientService } from '../services/ClientService';
import { Router } from '@angular/router';

@Component({
  selector: 'app-clients',
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.css']
})
export class ClientsComponent implements OnInit {

  constructor(private ClientService: ClientService, private router: Router) { }
  
  Clients: any;
  ngOnInit(): void {
    this.getClients();
    
  }
  getClients(){
    this.ClientService.getClient().subscribe(
      (response) => {
        console.log(response)
        this.Clients = response
      }, (error) => {
        console.log(error)
      }
    )

  }

  navigateToClientView(id: any) {
    console.log(id)
    this.router.navigate(['clientView', id]);


  }
  deleteClient(id:any){
    this.deleteClient;
  }
}
