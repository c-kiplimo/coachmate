import { ClientService } from '../services/ClientService';
import { ApiService } from '../services/ApiService';
import { Component, HostListener, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { style, animate, transition, trigger } from '@angular/animations';
import { id } from 'date-fns/locale';
import { FormBuilder, FormGroup } from '@angular/forms';
import { faCaretDown, faChevronLeft, faChevronRight, faPlus,faPenSquare } from '@fortawesome/free-solid-svg-icons';
import { IconProp } from '@fortawesome/fontawesome-svg-core';

@Component({
  selector: 'app-client-view',
  templateUrl: './client-view.component.html',
  styleUrls: ['./client-view.component.css',
                ],
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

export class ClientViewComponent implements OnInit {
  Clients!: [];
  editedClient: any;
editSession(_t92: any) {
throw new Error('Method not implemented.');
}
userDetails: any;
id: any;
deleteSession(arg0: any,arg1: any) {
throw new Error('Method not implemented.');
}
loading: boolean = false;
goToItem(arg0: string,_t227: any) {
throw new Error('Method not implemented.');
}
viewPayment(_t227: any) {
throw new Error('Method not implemented.');
}
getPayments() {
throw new Error('Method not implemented.');
}
userIcon!: IconProp;
calendarIcon: IconProp = "function";
clockIcon: IconProp = "function";
getsession() {
throw new Error('Method not implemented.');
}
  refreshIcon!: IconProp;
getsessions(arg0: number) {
throw new Error('Method not implemented.');
}
onContractChange($event: Event) {
throw new Error('Method not implemented.');
}
addSession() {
throw new Error('Method not implemented.');

console.log("here")
}

contracts:any;
suspendClientForm!: FormGroup;
closeClientForm!: FormGroup;
activateClientForm!: FormGroup;
editPaymentForm!: FormGroup;
caretDown = faCaretDown;
addIcon = faPlus;
editIcon = faPenSquare;
rightIcon = faChevronRight;
backIcon = faChevronLeft;
clientId: any;
orderId: any;
paymentId: any;
searchTerm = '';
lient: any;
orders!: any;
showHideMessage = true;
payments!: any;
notifications!: any;
notification!: any;
payment!: any;
currentTab = 'sessions';
searching = false;
actions = ['Activate', 'Close', 'Suspend'];

notificationOptions = [false, true];
notificationType = ['sms', 'email'];
loadingClient = false;
totalLength: any;
page: number = 1;
itemsPerPage:number = 20;
open = false;
sessions:any = [];
clients:any;
  console: any;
  filters: any = {
    status: '',
    searchItem: '',
  };
  status: any;

updateClient!: FormGroup;
session: any;
sessionDueDate: any;
sessionStartTime: any;
sessionDuration: any;
addSessionForm: any;
  @HostListener('document:click', ['$event']) onClick(event: any) {
    // console.log(event.target.attributes.id.nodeValue);
  
    if (event.target.attributes && event.target.attributes.id) {
      if (event.target.attributes.id.nodeValue === 'open') {
        this.open = true;
      }
    } else {
      this.open = false;
    }
  }

  constructor(
    private ClientService:ClientService,
    private apiService:ClientService,
    private router:Router,
    private formbuilder: FormBuilder,
    private activatedRoute: ActivatedRoute)
   { }

  clientToBeUpdated!: any;
  ngOnInit(): void {
    this.clientId = this.activatedRoute.snapshot.params['id'];
   
    this.getClientData(this.clientId);

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
      this.getClientSessions() 
  }
  getClientSessions() {
    console.log("client id",this.clientId)
    this.loading = true;
    this.ClientService.getClientSessions(this.clientId)
      .subscribe((data: any) => {
        this.sessions = data.body;
        console.log(this.sessions);
        this.loading = false;
        console.log("sessions gotten here",this.sessions)
      },
      (error: any) => {
        console.log(error);
        this.loading = false;
      }
      );
  }

  navigateToSessionView(id: any) {
    console.log(id);
    this.router.navigate(['sessionView', id]);
  }
  toggleTab(tab: string): void {
    this.currentTab = tab;
  }

  getClientData(id: any) {
    console.log("Get Client");
    this.loadingClient = true;
    
    this.ClientService.getOneClient(id).subscribe((data) => {
      this.loadingClient = false;
      this.clients = data.body;
      console.log(this.clients);
    },  
    (error: any) => {
      console.log(error);
      this.loadingClient = false;

    });
  }
  
  getClass(Clients: any) {
    if (Clients.status === 'SUSPENDED') {
        return 'badge-warning';
    } else if (Clients.status === 'ACTIVE') {
        return 'badge-success';
    } else {
        return 'badge-danger';
    }
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

  showStatus: any;

  statusState (currentstatus: any) {
    console.log(currentstatus);
    if (currentstatus === 'ACTIVE') {
      this.showStatus = "ACTIVATE";
      this.status = "ACTIVE";
    } else if (currentstatus === 'SUSPENDED') {
      this.showStatus = "SUSPEND";
      this.status = "SUSPENDED";
    } else if (currentstatus === 'CLOSED') {
      this.showStatus = "CLOSE";
      this.status = "CLOSED";
    }

  }
  changeClientStatus(){
    console.log(this.status);
    if(this.status === "ACTIVE") {
      this.ClientService.changeClientStatus(this.clientId, "ACTIVE").subscribe(
        (response) => {
          this.router.navigate(['/clients']);
        }, (error) => {
          console.log(error)
        }
      );
    }

    if(this.status === "SUSPENDED") {
      this.ClientService.changeClientStatus(this.clientId, "SUSPENDED").subscribe(
        (response) => {
          this.router.navigate(['/clients']);
        }, (error) => {
          console.log(error)
        }
      );
    }

    if(this.status === "CLOSED") {
      this.ClientService.changeClientStatus(this.clientId, "CLOSED").subscribe(
        (response) => {
          this.router.navigate(['/clients']);
        }, (error) => {
          console.log(error)
        }
      );
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


}



function getOrders(page: any, number: any, arg2: any) {
  throw new Error('Function not implemented.');
}

function getPayments(arg0: any) {
  throw new Error('Function not implemented.');
}

function getNotifications(arg0: any) {
  throw new Error('Function not implemented.');
}

function newOrder() {
  throw new Error('Function not implemented.');
}

function viewPayment(payment: any, any: any) {
  throw new Error('Function not implemented.');
}

function editedClientDetails() {
  throw new Error('Function not implemented.');
}

function updatePaymentDetails() {
  throw new Error('Function not implemented.');
}

function suspendClient() {
  throw new Error('Function not implemented.');
}

function closeClient() {
  throw new Error('Function not implemented.');
}

function activateClient() {
  throw new Error('Function not implemented.');
}

function back() {
  throw new Error('Function not implemented.');
}

function viewNotification(notification: any, any: any) {
  throw new Error('Function not implemented.');
}

