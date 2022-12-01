import { Component, OnInit } from '@angular/core';
import { ClientsComponent } from '../clients/clients.component';
import { ClientService } from '../services/ClientService';
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  ClientService: any;
  clients: any;

  constructor(private clientservice : ClientService) { }

  ngOnInit(): void {
    this.clients=this.ClientService.getClients();
  }
  

}
