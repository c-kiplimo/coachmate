import { Component, ElementRef, HostListener, OnInit, ViewChild } from '@angular/core';
import { ClientService } from '../services/ClientService';
import { style, animate, transition, trigger } from '@angular/animations';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import {
  faBell,
  faCaretDown,
  faChevronLeft,
  faChevronRight,
  faPenSquare,
  faPlus,
  faRedo,
  // faRefresh,
} from '@fortawesome/free-solid-svg-icons';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup } from '@angular/forms';
import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { ApiService } from '../services/ApiService';

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
  addsessionForm!:FormGroup;
  payment: any;
  addSessionForm:any={

  }; 
  editedClient: any;
  coachSessionData: any;
  coachData: any;
  userRole: any;
  alertIcon!: IconProp;
userIcon!: IconProp;
calendarIcon: IconProp = "function";
clockIcon: IconProp = "function";
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
payments!: any [];
notifications!: any[];
notification!: any;

sessionType = [
    
];

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
id: any;
sessions!: any[];
session: any;
clients:any;
  console: any;
  filters: any = {
    status: '',
    searchItem: '',
  };
  status: any;

updateClient!: FormGroup;
sessionDueDate: any;
sessionStartTime: any;
sessionDuration: any;

loading:any;
refreshIcon!: IconProp;
createSessionClientId: any;
selectedContract: any;
sessionId: any;
clientToBeUpdated!: any;
  sessionModal: any;
  contractId: any;
  contract: any;

  constructor(
    private ClientService:ClientService,
    private router:Router,
    private http: HttpClient,
    private formbuilder: FormBuilder,
    private toastrService: ToastrService,
    private route: ActivatedRoute,
    private apiService:ApiService,
    private activatedRoute: ActivatedRoute)
   { }

  ngOnInit() {
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);
    this.userRole = this.coachData.userRole;
    console.log(this.userRole);

    if(this.userRole == 'COACH'){
    this.sessionId = this.route.snapshot.params['sessionId'];
    console.log(this.sessionId);
    this.clientId = this.activatedRoute.snapshot.params['id'];
    this.getClientData(this.clientId);
    
    this.getContracts();
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
      this.getNotifications()
      this. getPaymentsByUser()
  }
  this.addsessionForm = this.formbuilder.group({
    sessionDate: '',
    sessionStartTime: '',
    sessionDuration: '',
    sessionType: '',
    sessionVenue: '',
    name:'',
    sessionDetails:'',
    sessionEndTime:'',
    attachments:'',
    notes:'',
    feedback:'',
    paymentCurrency:'',
    amountPaid:'',
    sessionAmount:'',
    sessionBalance:'',

  });
  
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
  getNotifications(): void {
    this.searching = true;
    this.notifications = [];
    const options = {
       sessionId: this.sessionId,
       coachId :this.coachData.id,
       clientId: this.clientId,
      page: 1,
      per_page: 10,
    };

    this.ClientService.getAllNotifications(options).subscribe((res: any) => {
      this.notifications = res.body;
      console.log('notification ni', this.notifications);
      this.searching = false;
    });
  }
  getPaymentsByUser(){
    this.loading = true;
    this.payments = [];
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
    };
    this.ClientService.getPaymentsByUser(options).subscribe((res: any) => {
      this.payments = res.body.data;
      console.log('payments ni', this.payments);
      this.loading = false;
    }
    );

  }
  onContractChange(event: any) {
    console.log(event.target.value);
    this.createSessionClientId = event.target.value;

    //get client details from contract id
    this.selectedContract = this.contracts.find((contract: any) => contract.id == event.target.value);
    console.log(this.selectedContract);

  }
  addSession () {
    console.log('add button clicked here')
    console.log(this.addSessionForm);
    const session = this.addSessionForm;
    session.contractId = this.contractId;
    if (this.selectedContract && this.selectedContract.client) {
      this.addSessionForm.clientId = this.selectedContract.client.id;
    }
    if (typeof this.sessionType === "string") {
      let stringValue = this.sessionType;
      if(this.sessionType === 'INDIVIDUAL') {
        session.sessionAmount = this.contract.individualFeesPerSession;
      }
      else
      {session.sessionAmount = this.contract.groupFeesPerSession;}
    }
   console.log(this.addSessionForm);
   console.log('add button clicked here')
   const params = {
      clientId: this.selectedContract.client.id,
      
      contractId: this.createSessionClientId
   };

   console.log(params);
 
   this.ClientService.addSession(this.addSessionForm, params).subscribe((res:any) => {
    console.log(res);
    this.toastrService.success('Session added successfully');
    setTimeout(() => {
      location.reload();
    }, 1000);
    this.sessionModal.nativeElement.classList.remove('show');
    this.sessionModal.nativeElement.style.display = 'none';
  }, error => {
    console.log(error);
    this.toastrService.error(error.error, 'Maximum sessions reached contact coach');
    this.sessionModal.nativeElement.classList.remove('show');
    this.sessionModal.nativeElement.style.display = 'none';
   
  });
  
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
  @ViewChild('modal', { static: false })
modal!: ElementRef;
closeModal() {
  this.modal.nativeElement.style.display = 'none';
  document.body.classList.remove('modal-open');
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
@ViewChild('editClientModal', { static: false })
editClientModal!: ElementRef;
@ViewChild('activateclientModal', { static: false })
activateclientModal!: ElementRef;
@ViewChild('suspendclientModal', { static: false })
suspendclientModal!: ElementRef;
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
        (res) => {
          console.log(res);
          this.toastrService.success('Status Changed successfully');
          setTimeout(() => {
            location.reload();
          }, 1000);
          this.activateclientModal.nativeElement.classList.remove('show');
          this.activateclientModal.nativeElement.style.display = 'none';
        
        }, (error) => {
          console.log(error)
          this.toastrService.success('Status change failed');
        }
      );
    }

    if(this.status === "SUSPENDED") {
      this.ClientService.changeClientStatus(this.clientId, "SUSPENDED").subscribe(
        (res) => {
          console.log(res);
          this.toastrService.success('Status changed successfully');
          setTimeout(() => {
            location.reload();
          }, 1000);
          this.suspendclientModal.nativeElement.classList.remove('show');
          this.suspendclientModal.nativeElement.style.display = 'none';
        }, (error) => {
          console.log(error)
          this.toastrService.success('Status change failed');
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
  viewNotification(notification: any): void {
    this.notification = notification;
    console.log(this.notification);
  }
  goToItem(type: any, entityObj: any): void {
    this.router.navigate([type, entityObj.id]);
  }
  viewPayment(payment: any): void {
    console.log(this.session);
    this.payment = payment;
    this.paymentId = this.payment.id;
    if (this.payment.session.id === this.session?.id) {
      console.log(this.session.id);
    }
  }

  getContracts() {
    this.ClientService.getContracts().subscribe((res:any) => {
      console.log(res);
      this.contracts = res; });
  }

}

  // Get Notification for specific customer




