import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ClientService } from '../../services/ClientService';
import { Router } from '@angular/router';
import { style, animate, transition, trigger } from '@angular/animations';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';

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
  salesData: any;
  loading = false;
  itemsPerPage = 20;
  filters: any = {
    status: '',
    searchItem: '',
  };
  

  updateClient!: FormGroup;

  constructor(private clientService: ClientService, 
    private router: Router,
    private toastrService: ToastrService,
    private formbuilder: FormBuilder,) { }
  
  clients!: any;
  all_clients!: any;

  clientToBeUpdated!: any;
  coachSessionData: any;
  coachData: any;
  userRole: any;

  user: any;

  orgId!: number;
  coachId!: number;

  page: number = 0;
  pageSize: number = 15;
  totalElements: any;

  ngOnInit(): void {
    this.coachSessionData = sessionStorage.getItem('user');
    this.user = JSON.parse(this.coachSessionData);

    this.userRole = this.user.userRole;
    console.log(this.userRole);


    if (this.userRole == 'ORGANIZATION') {
      this.orgId = this.user.id;
      this.getClients(this.page);

    } else if (this.userRole == 'COACH') {
      this.coachId = this.user.id;
      this.getClients(this.page);

    } else if (this.userRole == 'CLIENT') {
      this.clientId = this.user.id;
      this.getClients(this.page);
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

    this.getClients(this.page);
    
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


  getClients(page: any) {
 
    this.loading = true;
    this.page = page;
    //if page is 0, don't subtract 1
    if (page === 0 || page < 0) {
      page = 0;
    } else {
      page = page - 1;
    }
    const options: any = {
      page: page,
      size: this.pageSize,
      status: this.filters.status,
      search: this.filters.searchItem,
      sort: 'id,desc',
    };

    if(this.userRole == 'COACH'){
      options.coachId = this.coachId;
    }else if(this.userRole == 'CLIENT'){
      options.clientId = this.clientId;
    }else if(this.userRole == 'ORGANIZATION'){
      options.coachId = this.coachId;
    }
    
    this.clientService.getClients(options).subscribe(
      (response) => {
        this.loading = false;
        this.clients = response.body;
        this.totalElements = +response.headers.get('X-Total-Count');
        console.log('clients',this.clients)
      }, (error) => {
        this.loading = false;
        console.log(error)
      }
    )
  }

  search() {
    this.page = 0;
    this.getClients(this.page);
  }
  filterByStatus() {
    this.page = 0;
    this.getClients(this.page);
  }

  navigateToClientView(id: any) {
    console.log(id)
    this.router.navigate(['/clientView', id]);
  }
  deleteClient(client: any) {
    // this.clientService.deleteClient().subscribe(() => {
    //     // update the list of items
    //     this.clientService.getClient(client).subscribe(clients => {
    //         this.Clients = clients;
    //     });
    // });
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
    this.clientService.editClient(this.clientToBeUpdated,id).subscribe(
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
    this.clientService.suspendClient(client).subscribe(
      (response) => {
        this.getClients(this.page);
        this.loading = false;

      }, (error) => {
        console.log(error)
      }
    );
  }
  // filter clients by status
  filterClientsByStatus(status: any) {
    this.filters.status = status;
    this.getClients(this.page);
  }

  onTableDataChange(event: any) {
    this.page = event;
    this.getClients(this.page);
  }
  
}
