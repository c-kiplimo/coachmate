import { Component, OnInit } from '@angular/core';
import { ClientService } from '../services/ClientService';
import { Router } from '@angular/router';
import { style, animate, transition, trigger } from '@angular/animations';

@Component({
  selector: 'app-clients',
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.css'],
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
export class ClientsComponent implements OnInit {
  loading = false;

  constructor(private ClientService: ClientService, private router: Router) { }
  
  Clients: any;
  ngOnInit(): void {
    this.getClients();
    
  }
  getClients(){
    this.loading = true;
    this.ClientService.getClient().subscribe(
      (response) => {
        console.log(response)
        this.Clients = response
        this.loading = false;
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
