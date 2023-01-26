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
contracts:any;
orderForm!: FormGroup;
editCustomerForm!: FormGroup;
suspendCustomerForm!: FormGroup;
closeCustomerForm!: FormGroup;
activateCustomerForm!: FormGroup;
editPaymentForm!: FormGroup;
caretDown = faCaretDown;
addIcon = faPlus;
editIcon = faPenSquare;
rightIcon = faChevronRight;
// refreshIcon = faRefresh;
backIcon = faChevronLeft;
customerId: any;
orderId: any;
paymentId: any;
searchTerm = '';
orderAmount = '';
eventType = '';
orderDueDate = '';
orderDueTime = '';
customer: any;
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
loadingCustomer = false;
totalLength: any;
page: number = 1;
itemsPerPage = 20;
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
    private restApiService:ApiService,
    private router:Router,
    private formbuilder: FormBuilder,
    private activatedRoute: ActivatedRoute)
   { }

  loadingClient = false;
 

  
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
//     customer_id: this.customerId,
//   };
//   // console.log(this.customerId);
//   this.service.filterOrdersByCustomerId(Option).subscribe((res: any) => {
//     // console.log('orders->',res.body.data);
//     this.orders = res.body.data;
//     console.log("here are this customers orders=>",this.orders)
//     this.totalLength = Number(res.body.totalElements);
//     this.searching = false;
//     this.getNotifications();
//   });
// }

// // get payments for specific customer
// getPayments(navigator?: boolean): void {
//   this.searching = true;
//   this.payments = [];
//   const Option = {
//     page: 1,
//     per_page: 10,
//     customer_id: this.customerId,
//   };

//   this.service.filterPaymentsByCustomerId(Option).subscribe((res: any) => {
//     // console.log('payments here=>', res.body.data);
//     this.payments = res.body.data;
//     console.log("payments for this customer=>",this.payments)
  
//     this.searching = false;
//   });
// }

// // Get Notification for specific customer
// getNotifications(navigator?: boolean): void {
//   this.searching = true;
//   this.notifications = [];
//   const Option = {
//     page: 1,
//     per_page: 10,
//     customer_id: this.customerId,
//   };

//   this.service.getNotificationsbyCustomerId(options).subscribe((res: any) => {
//     // console.log(res.body);
//     this.notifications = res.body.data;
//     this.searching = false;
//   });
// }

// newOrder() {
//   const names = this.searchTerm.split(' ');

//   // console.log(this.orderForm.value);
//   this.orderForm.patchValue({
//     name: this.customer.firstName + ' ' + this.orderForm.value.orderType,
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

//edit customer details
// editedCustomerDetails() {
//   console.log(
//     'these are edited customer details=>',
//     this.customerId,
//     this.editCustomerForm.value
//   );
//   this.service
//     .editCustomer(this.customerId, this.editCustomerForm.value)
//     .subscribe({
//       next:(response: any) => {
//       console.log(response);
//       this.toastrService.success('Customers details updated!', 'Success!');
//       setTimeout(() => {
//         location.reload();
//       }, 2);
//     },
//     error: (err) => {
//       console.log(err);
//       this.toastrService.error(
//         'Customer not updated, try again!',
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
//Customer Actions functions
// suspendCustomer() {
//   const options = {
//     status: 'SUSPENDED',
//   };
//   // console.log('details to suspend', this.suspendCustomerForm.value);
//   this.service
//     .customerAction(this.customerId, this.suspendCustomerForm.value, options)
//     .subscribe({
//       next: (res: any) => {
//         console.log(res);
//         this.toastrService.info('Customer suspended!', 'Info!');
//         this.suspendCustomerForm.reset();
//         setTimeout(() => {
//           location.reload();
//         }, 5);
//       },
//       error: (err) => {
//         console.log(err);
//         this.toastrService.error(
//           'Customer not suspended, try again!',
//           'Failed!'
//         );
//       },
//     });
// }
// closeCustomer() {
//   this.service.closeCustomer(this.customerId).subscribe({
//     next: (res: any) => {
//       console.log(res);
//       this.toastrService.info('Customer closed!', 'Info!');
//       this.closeCustomerForm.reset();
//     },
//     error: (err) => {
//       console.log(err);
//       this.toastrService.error('Customer not closed, try again!', 'Failed!');
//     },
//   });
// }
// activateCustomer() {
//   const options = {
//     status: 'ACTIVATE',
//   };
//   this.service
//     .customerAction(this.customerId, this.activateCustomerForm.value, options)
//     .subscribe({
//       next: (res: any) => {
//         // console.log(res);
//         this.toastrService.success('Customer activated!', 'Success!');
//         this.activateCustomerForm.reset();
//         setTimeout(() => {
//           location.reload();
//         }, 5);
//       },
//       error: (err) => {
//         console.log(err);
//         this.toastrService.error(
//           'Customer not activated, try again!',
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

function editedCustomerDetails() {
  throw new Error('Function not implemented.');
}

function updatePaymentDetails() {
  throw new Error('Function not implemented.');
}

function suspendCustomer() {
  throw new Error('Function not implemented.');
}

function closeCustomer() {
  throw new Error('Function not implemented.');
}

function activateCustomer() {
  throw new Error('Function not implemented.');
}

function back() {
  throw new Error('Function not implemented.');
}

function viewNotification(notification: any, any: any) {
  throw new Error('Function not implemented.');
}

