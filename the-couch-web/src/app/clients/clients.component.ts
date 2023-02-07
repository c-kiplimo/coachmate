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
  coachSessionData: any;
  coachData: any;
  userRole: any;

  ngOnInit(): void {

    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);
    this.userRole = this.coachData.userRole;
    console.log(this.userRole);

    if(this.userRole == 'COACH'){
       this.getClients();
    }else if(this.userRole == 'ORGANIZATION'){
      this.getOrgClients();

    }

    this.updateClient = this.formbuilder.group({
     
    firstName: ' ',
    lastName: ' ',
    type: ' ',
    msisdn: ' ',
    email_address: ' ',
    physical_address: ' ',
    profession: ' ',
    payment_mode: ' ',
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

getOrgClients(){
  const options = {
    page: 1,
    per_page: this.itemsPerPage,
    status: this.filters.status,
    search: this.filters.searchItem,
  };
  const id = this.coachData.id;
  this.loading = true;
  this.ClientService.getOrgClients(id).subscribe(
    (response) => {
      this.loading = false;
      this.Clients = response;
      console.log(response)
      console.log('clients',this.Clients)

    }, (error) => {
      console.log(error)
    }
  )
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
      clientType: this.clientToBeUpdated.type,
      msisdn: this.clientToBeUpdated.msisdn,
      email_address: this.clientToBeUpdated.email_address,
      physical_address: this.clientToBeUpdated.physical_address,
      profession: this.clientToBeUpdated.profession,
      payment_mode: this.clientToBeUpdated.payment_mode,
      reason: this.clientToBeUpdated.reason,
    });
  
  }

  updateClientDetails(id:any){
  
    console.log(this.updateClient.value)
    console.log(this.clientToBeUpdated)
    
    console.log(id)
   
    this.ClientService.editClient(id, this.updateClient.value).subscribe(
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
