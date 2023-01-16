import { Component, OnInit } from '@angular/core';
import { ClientService } from '../services/ClientService';
import { Router } from '@angular/router';
import { style, animate, transition, trigger } from '@angular/animations';
import { FormGroup } from '@angular/forms';

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
  itemsPerPage = 20;
  filters: any = {
    status: '',
    searchItem: '',
  };

  updateClient!: FormGroup;

  constructor(private ClientService: ClientService, private router: Router) { }
  
  Clients: any;
  ngOnInit(): void {
    this.getClients();
    
  }
  getClients(){
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
    };
    this.loading = true;
    this.ClientService.getClient(options).subscribe(
      (response) => {
        this.loading = false;
        this.Clients = response.body;
        console.log(response.body)
        console.log('clients',this.Clients)
        
      }, (error) => {
        console.log(error)
      }
    )

  }

  navigateToClientView(id: any) {
    console.log(id)
    this.router.navigate(['/clientView', id]);


  }
  deleteClient(client: any) {
    this.ClientService.deleteClient().subscribe(() => {
        // update the list of items
        this.ClientService.getClient(client).subscribe(clients => {
            this.Clients = clients;
        });
    });
}
  editClient(client:any){
    this.ClientService.editClient(client).subscribe(
      (response) => {
        this.getClients();
        this.loading = false;

      }, (error) => {
        console.log(error)
      }
    );
  }

  suspendClient(client:any){
    this.ClientService.suspendClient(client).subscribe(
      (response) => {
        this.getClients();
        this.loading = false;

      }, (error) => {
        console.log(error)
      }
    );
  }
}
