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
client: any;
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
orderForm!: FormGroup;
editClientForm!: FormGroup;
suspendClientForm!: FormGroup;
closeClientForm!: FormGroup;
activateClientForm!: FormGroup;
editPaymentForm!: FormGroup;
caretDown = faCaretDown;
addIcon = faPlus;
editIcon = faPenSquare;
rightIcon = faChevronRight;
// refreshIcon = faRefresh;
backIcon = faChevronLeft;
lientId: any;
orderId: any;
paymentId: any;
searchTerm = '';
orderAmount = '';
eventType = '';
orderDueDate = '';
orderDueTime = '';
lient: any;
orders!: any;
showHideMessage = true;
payments!: any;
notifications!: any;
notification!: any;
payment!: any;
currentTab = 'orders';
searching = false;
deliveryOption = ['DELIVERED', 'PICKUP'];
actions = ['Activate', 'Close', 'Suspend'];

notificationOptions = [false, true];
notificationType = ['sms', 'email'];
loadingClient = false;
totalLength: any;
page: number = 1;
itemsPerPage:number = 20;
open = false;
sessions:any;
clients:any;
clientId:any;
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

  // loadingClient = false;
 

  
  // page: number = 1;
  // itemsPerPage = 20;
  // ApiService: any;

  clientToBeUpdated!: any;
  ngOnInit(): void {
    this.clientId = this.activatedRoute.snapshot.params['id'];
   
    this.getClientData(this.clientId);

    this.updateClient = this.formbuilder.group({
    
      firstName: ' ',
      lastName: ' ',
      type: ' ',
      msisdn: ' ',
      email_address: ' ',
      physical_address: ' ',
      profession: ' ',
      paymentMode: '',
      payment_mode: ' ',
      reason: '',
  
      });
      this.getAllSessions();
  }
  getAllSessions() {
    console.log("here")
    this.loading = true;
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
    };
    this.apiService.getSessions(options).subscribe(
      (response: any) => {
        console.log(response.body.data);
        this.sessions = response.body.data;
        this.loading = false;
      },
      (error: any) => {
        console.log(error);
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

      this.updateClient = this.formbuilder.group({
       
        firstName: this.clients.firstName,
        lastName: this.clients.lastName,
        type: this.clients.type,
        msisdn: this.clients.msisdn,
        email_address: this.clients.email_address,
        physical_address: this.clients.physical_address,
        profession: this.clients.profession,
        payment_mode: this.clients.payment_mode,
        reason: this.clients.reason,
      });

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
  updateClientDetails(){
  
    console.log(this.updateClient.value)
    
    
    console.log(this.updateClient.value)
   
    this.ClientService.editClient(this.clientId, this.updateClient.value).subscribe(
      (response) => {
        this.router.navigate(['/clients']);
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

}


// getOrders(page: number, navigator?: boolean): void {
//   this.searching = true;
//   this.orders = [];
//   const Option = {
//     page: page,
//     per_page: 10,
//     Client_id: this.lientId,
//   };
//   // console.log(this.ClientId);
//   this.service.filterOrdersBy ClientId(Option).subscribe((res: any) => {
//     // console.log('orders->',res.body.data);
//     this.orders = res.body.data;
//     console.log("here are this  Clients orders=>",this.orders)
//     this.totalLength = Number(res.body.totalElements);
//     this.searching = false;
//     this.getNotifications();
//   });
// }

// // get payments for specific  Client
// getPayments(navigator?: boolean): void {
//   this.searching = true;
//   this.payments = [];
//   const Option = {
//     page: 1,
//     per_page: 10,
//     Client_id: this.lientId,
//   };

//   this.service.filterPaymentsBy ClientId(Option).subscribe((res: any) => {
//     // console.log('payments here=>', res.body.data);
//     this.payments = res.body.data;
//     console.log("payments for this  Client=>",this.payments)
  
//     this.searching = false;
//   });
// }

// // Get Notification for specific  Client
// getNotifications(navigator?: boolean): void {
//   this.searching = true;
//   this.notifications = [];
//   const Option = {
//     page: 1,
//     per_page: 10,
//     Client_id: this.lientId,
//   };

//   this.service.getNotificationsby ClientId(options).subscribe((res: any) => {
//     // console.log(res.body);
//     this.notifications = res.body.data;
//     this.searching = false;
//   });
// }

// newOrder() {
//   const names = this.searchTerm.split(' ');

//   // console.log(this.orderForm.value);
//   this.orderForm.patchValue({
//     name: this. Client.firstName + ' ' + this.orderForm.value.orderType,
//     sendNotification: this.showHideMessage,
//     dueDate: this.orderDueDate + 'T' + this.orderDueTime,
//   });
//   this.service.addNewOrder(this.orderForm.value).subscribe({
//     next: (response: any) => {
//       this.orderForm.reset();
//       this.toastrService.success('Order created!', 'Success!');
//       setTimeout(() => {
//         location.reload();
//       }, 5);
//     },
//     error: (err) => {
//       console.log(err);
//       this.toastrService.error('Order not created, try again!', 'Failed!');
//     },
//   });
// }
// viewPayment(payment: any): void {
//   this.payment = payment;
//   this.paymentId = this.payment.id;
//   this.orderId = this.payment.order.id;
//   this.orders.forEach((order: any) => {
//     if (this.payment.order.id === order.id) {
//       // console.log(order.id);
//       this.payment.balance = order.orderAmount - this.payment.amount;
//     }
//   });
// }
// viewNotification(notification: any): void {
//   this.notification = notification;
// }

//edit Client details
// editedClientDetails() {
//   console.log(
//     'these are edited Client details=>',
//     this. ClientId,
//     this.edit ClientForm.value
//   );
//   this.service
//     .edit Client(this. ClientId, this.editClientForm.value)
//     .subscribe({
//       next:(response: any) => {
//       console.log(response);
//       this.toastrService.success('Clients details updated!', 'Success!');
//       setTimeout(() => {
//         location.reload();
//       }, 2);
//     },
//     error: (err) => {
//       console.log(err);
//       this.toastrService.error(
//         ' Client not updated, try again!',
//         'Failed!'
//       );
//     },
//   });
 //update payment details
//  updatePaymentDetails() {
//   this.service.editPayment(this.paymentId, this.editPaymentForm.value).subscribe({
//     next: (response: any) => {
//       console.log(response);
//       this.toastrService.success('Payment details updated!', 'Success!');
//       setTimeout(() => {
//         location.reload();
//       }, 2);
//     },
//     error: (err) => {
//       console.log(err);
//       this.toastrService.error('Payment not updated, try again!', 'Failed!');
//     },
//   });
// }
// Client Actions functions
// suspend Client() {
//   const options = {
//     status: 'SUSPENDED',
//   };
//   // console.log('details to suspend', this.suspend ClientForm.value);
//   this.service
//     . ClientAction(this. ClientId, this.suspend ClientForm.value, options)
//     .subscribe({
//       next: (res: any) => {
//         console.log(res);
//         this.toastrService.info(' Client suspended!', 'Info!');
//         this.suspendClientForm.reset();
//         setTimeout(() => {
//           location.reload();
//         }, 5);
//       },
//       error: (err) => {
//         console.log(err);
//         this.toastrService.error(
//           ' Client not suspended, try again!',
//           'Failed!'
//         );
//       },
//     });
// }
// closeclient() {
//   this.service.closeClient(this.lientId).subscribe({
//     next: (res: any) => {
//       console.log(res);
//       this.toastrService.info(' Client closed!', 'Info!');
//       this.closeClientForm.reset();
//     },
//     error: (err) => {
//       console.log(err);
//       this.toastrService.error('Client not closed, try again!', 'Failed!');
//     },
//   });
// }
// activateClient() {
//   const options = {
//     status: 'ACTIVATE',
//   };
//   this.service
//     . ClientAction(this. ClientId, this.activateClientForm.value, options)
//     .subscribe({
//       next: (res: any) => {
//         // console.log(res);
//         this.toastrService.success('Client activated!', 'Success!');
//         this.activate ClientForm.reset();
//         setTimeout(() => {
//           location.reload();
//         }, 5);
//       },
//       error: (err) => {
//         console.log(err);
//         this.toastrService.error(
//           ' Client not activated, try again!',
//           'Failed!'
//         );
//       },
//     });
// }
;
// back(){
//   window.history.back()
// }


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

