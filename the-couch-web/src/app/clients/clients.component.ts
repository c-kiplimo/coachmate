import { Component, OnInit } from '@angular/core';
import { ClientService } from '../services/ClientService';
import { Router } from '@angular/router';
import { style, animate, transition, trigger } from '@angular/animations';
import { FormBuilder, FormGroup } from '@angular/forms';

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
  clientId: any;
  editedClient: any;
getSearchedClient(arg0: number) {
throw new Error('Method not implemented.');
}
  
salesData: any;
getAllCustomers(arg0: number) {
throw new Error('Method not implemented.');
}
  loading = false;
  itemsPerPage = 20;
  filters: any = {
    status: '',
    searchItem: '',
  };
  

  updateClient!: FormGroup;

  constructor(private ClientService: ClientService, 
    private router: Router,
    private formbuilder: FormBuilder,) { }
  
  Clients!: any;

  clientToBeUpdated!: any;
  ngOnInit(): void {
    this.getClients();

    this.updateClient = this.formbuilder.group({
     
    firstName: ' ',
    lastName: ' ',
    clientType: ' ',
    msisdn: ' ',
    email: ' ',
    physicalAddress: ' ',
    profession: ' ',
    paymentMode: ' ',
    reason: '',

    });
    
  }
  getClass(client: any) {
    if (client.status === 'SUSPENDED') {
        return 'badge-warning';
    } else if (client.status === 'ACTIVE') {
        return 'badge-success';
    } else {
        return 'badge-danger';
    }
}

  
  getClients(){
    this.Clients = [];
    this.loading = true;
    window.scroll(0, 0);
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
        this.Clients = response.body.data;
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
    this.clientToBeUpdated = client;

    this.updateClient = this.formbuilder.group({
      firstName: this.clientToBeUpdated.firstName,
      lastName: this.clientToBeUpdated.lastName,
      clientType: this.clientToBeUpdated.clientType,
      msisdn: this.clientToBeUpdated.msisdn,
      email: this.clientToBeUpdated.email,
      physicalAddress: this.clientToBeUpdated.physicAddress,
      profession: this.clientToBeUpdated.profession,
      paymentMode: this.clientToBeUpdated.paymentMode,
      reason: this.clientToBeUpdated.reason,
    });
  
  }

  updateClientDetails(id:any){
    console.log(this.clientToBeUpdated)
    console.log(id)  
    this.ClientService.editClient(this.clientToBeUpdated,id).subscribe(
      (data) => {
        this.loading = false;
        this.editedClient = data.body;
        console.log(this.editedClient)
        console.log('clients',this.editedClient)

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
  // filter clients by status
  filterClientsByStatus(status: any) {
    this.filters.status = status;
    this.getClients();
  }
  
}
