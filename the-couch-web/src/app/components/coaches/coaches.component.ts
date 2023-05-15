import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ClientService } from '../../services/ClientService';

@Component({
  selector: 'app-coaches',
  templateUrl: './coaches.component.html',
  styleUrls: ['./coaches.component.css']
})
export class CoachesComponent implements OnInit {

  clientId: any;
  editedClient: any;  
salesData: any;
  loading = false;
  itemsPerPage = 20;
  filters: any = {
    status: '',
    searchItem: '',
  };
  

  updateClient!: FormGroup;
OrgCoaches: any;
  orgId: any;
  numberofCoaches: any;

  constructor(private ClientService: ClientService, 
    private router: Router,
    private toastrService: ToastrService,
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
      this.orgId = this.coachData.organization.id;
      console.log('ORGANIZATION');
      this.getOrgClients();
      this.getOrgCoaches(this.orgId);

    }

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
      this.Clients = response.body;
      console.log('clients',this.Clients)

    }, (error) => {
      console.log(error)
    }
  )
}
getOrgCoaches(id: any) {
  const data = {
    orgId: id,
  }
  this.ClientService.getOrgCoaches(data).subscribe(
    (response: any) => {
      console.log('here Organization=> coaches', response);
      this.OrgCoaches = response;
      console.log(this.OrgCoaches);
      console.log('here Organization=> coaches', response);
      this.numberofCoaches = this.OrgCoaches.length;
     
    },
    (error: any) => {
      console.log(error);
    }
  );
}
  
  getClients(){
    this.Clients = [];
    
    this.loading = true;
    
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
    };
    this.loading = true;
    this.ClientService.getClients(options).subscribe(
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
        this.ClientService.getClients(client).subscribe(clients => {
            this.Clients = clients;
        });
    });
}
@ViewChild('editClientModal', { static: false })
editClientModal!: ElementRef;  
  editClient(client:any){
    this.clientToBeUpdated = client;

    this.updateClient = this.formbuilder.group({
      firstName: this.clientToBeUpdated.firstName,
      lastName: this.clientToBeUpdated.lastName,
      clientType: this.clientToBeUpdated.clientType,
      msisdn: this.clientToBeUpdated.msisdn,
      email: this.clientToBeUpdated.email,

      physicalAddress: this.clientToBeUpdated.physicalAddress,

      profession: this.clientToBeUpdated.profession,
      paymentMode: this.clientToBeUpdated.paymentMode,
      reason: this.clientToBeUpdated.reason,
    });
  
  }

  updateClientDetails(id:any){
    this.clientToBeUpdated = this.updateClient.value;
    console.log(this.clientToBeUpdated)
    console.log(id)  
    this.ClientService.editClient(this.clientToBeUpdated,id).subscribe(
      (data) => {
        console.log(data)
        this.toastrService.success('Client Updated', 'Success!');
        setTimeout(() => {
          location.reload();
        }, 1000);
        this.editClientModal.nativeElement.classList.remove('show');
        this.editClientModal.nativeElement.style.display = 'none';

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

