import { Component, OnInit } from '@angular/core';
import { ClientService } from '../services/ClientService';

@Component({
  selector: 'app-clients',
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.css']
})
export class ClientsComponent implements OnInit {

  constructor(private ClientService: ClientService) { }
  
  Clients: any;
  ngOnInit(): void {
    this.ClientService.getClients().subscribe(
      (response) => {
        console.log(response)
        this.Clients = response
      }, (error) => {
        console.log(error)
      }
    )
  }

}
