import { Component, OnInit } from '@angular/core';
import { ClientService } from '../services/ClientService';
import { SessionsService } from '../services/SessionsService';
import { style, animate, transition, trigger } from '@angular/animations';
import { HttpClient } from '@angular/common/http';
// import { ToastrService } from 'ngx-toastr';
import {
  faCaretDown,
  faChevronRight,
  faPlus,
  faRedo,
  // faRefresh,
} from '@fortawesome/free-solid-svg-icons';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-session-view',
  templateUrl: './session-view.component.html',
  styleUrls: ['./session-view.component.css'],
  
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
export class sessionViewComponent implements OnInit {
  paymentForm!: FormGroup;
  confirmOrderForm!: FormGroup;
  cancelOrderForm!: FormGroup;
  deleteOrderForm!: FormGroup;
  deliveredOrderForm!: FormGroup;
  caretDown = faCaretDown;
  addIcon = faPlus;
  rightIcon = faChevronRight;
  refreshIcon =faRedo;
  payments!: any;
  notifications!: any;
  notification!: any;
  attachments!: any;
  sessionId!: any;
  customerId!: any;
  payment!: any;
  order!: any;
  response_data!: any;
  searching = false;
  currentTab = 'payments';
  loading = false;
  deliveredDate = '';
  deliveredTime = '';
  service: any;
  sessions:any;
  session:any;



  constructor(
    private restApiService:ClientService,
    private http: HttpClient,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private formbuilder: FormBuilder,
    private sessionService:SessionsService
    // private toastrService: ToastrService

  ) {}

  ngOnInit(): void {
    this.sessionId = this.activatedRoute.snapshot.params['id'];
    this.getSession();
    this.getAllSessions();

    this.paymentForm = this.formbuilder.group({
      extPaymentRef: '',
      amount: '',
      narration: '',
      orderId: this.sessionId,
      customerId: this.customerId,
      sendNotification: true,
    });
    this.confirmOrderForm = this.formbuilder.group({
      narration: '',
    });
    this.cancelOrderForm = this.formbuilder.group({
      narration: '',
      isSendNotification: true,
    });
    this.deliveredOrderForm = this.formbuilder.group({
      deliveredOn: '',
      narration: '',
      isSendNotification: true,
    });
    this.deleteOrderForm = this.formbuilder.group({
      narration: '',
    });
  }

  toggleTab(tab: string): void {
    this.currentTab = tab;
  }
  getAllSessions(){
    this.restApiService.getSessions().subscribe(
      (response: any) => {
        console.log(response)
        this.sessions = response
      }, (error: any) => {
        console.log(error)
      }
    )
  }


  getSession() {
    this.loading = true;

    this.sessionService.getOneSession(this.sessionId).subscribe((res: any) => {
      // console.log('order', res.body);
      this.loading = false;
      this.order = res.body;
      this.customerId = this.order.customer.id;
      // console.log(this.customerId);
      // this.getPayments();
      // this.getNotifications();
    });
  }
  // get payments for specific order
  // getPayments(navigate?: boolean): void {
  //   this.searching = true;
  //   this.payments = [];
  //   const options = {
  //     page: 1,
  //     per_page: 10,
  //     order_id: this.orderId,
  //   };
  //   this.restApiService.filterPaymentsByOrderId(options).subscribe((res: any) => {
  //     this.payments = res.body.data;
  //     console.log('payments ni', this.payments);
  //     this.searching = false;
  //   });
  // }
  // getNotifications(navigate?: boolean): void {
  //   this.searching = true;
  //   this.notifications = [];
  //   const options = {
  //     page: 1,
  //     per_page: 10,
  //     order_id: this.orderId,
  //   };

  //   this.restApiService.getNotificationsbyOrderId(options).subscribe((res: any) => {
  //     this.notifications = res.body.data;
  //     console.log('notification ni', this.notifications);
  //     this.searching = false;
  //   });
  // }

  // newPayment() {
  //   // console.log(this.paymentForm.value);
  //   this.restApiService
  //     .addNewPayment(this.paymentForm.value)
  //     .subscribe({
  //       next:(response: any) => {
  //         this.paymentForm.reset();
  //         // this.toastrService.success('Payment added!', 'Success!');
  //         setTimeout(() => {
  //           location.reload();
  //         }, 5);},
  //       error: (err: any) => {
  //         console.log(err);
  //         // this.toastrService.error(
  //           // 'Payment not added, try again!',
  //           // 'Failed!'
  //         // );
  //       },
  //     });
  // }

  viewPayment(payment: any): void {
    console.log(this.order);
    this.payment = payment;
    if (this.payment.order.id === this.order?.id) {
      console.log(this.order.id);
      this.payment.balance = this.order.orderAmount - this.payment.amount;
    }
  }
  viewNotification(notification: any): void {
    this.notification = notification;
    console.log(this.notification);
  }

  //Order Actions functions

  confirmOrder() {
    const options = {
      status: 'CONFIRMED',
    };
    console.log(this.confirmOrderForm.value);
    this.service
      .orderAction(this.sessionId, this.confirmOrderForm.value, options).subscribe({
        next: (res: any) => {
          console.log(res);
          // this.toastrService.info('Order confirmed!', 'Info!');
          this.confirmOrderForm.reset();
          setTimeout(() => {
            location.reload();
          }, 5);
        },
        error: (err: { message: any; }) => {
          console.log('error->',err.message);
          // this.toastrService.error(
          //  'Order status was not updated, try again',
            // 'Failed!'
          // );
        },
      });
  }

  // cancelOrder() {
  //   const options = {
  //     status: 'CANCELLED',
  //   };
  //   this.restApiService
  //     .orderAction(this.orderId, this.cancelOrderForm.value, options)
  //     .subscribe({
  //       next: (res: any) => {
  //         console.log(res);
  //         // this.toastrService.info('Order status is cancelled!', 'Info!');
  //         this.cancelOrderForm.reset();
  //         setTimeout(() => {
  //           location.reload();
  //         }, 5);
  //       },
  //       error: (err: { message: any; }) => {
  //         console.log('error->',err.message);
  //       //   this.toastrService.error(
  //       //    'Order status was not updated, try again',
  //       //     'Failed!'
  //       //   );
  //       },
  //     });
  // }

  // deliveredOrder() {
  //   this.deliveredOrderForm.patchValue({
  //     deliveredOn: this.deliveredDate + 'T' + this.deliveredTime,
  //   });
  //   const options = {
  //     status: 'DELIVERED',
  //   };
  //   this.restApiService
  //     .orderAction(this.orderId, this.deliveredOrderForm.value, options)
  //     .subscribe({
  //       next: (res: any) => {
  //         console.log(res);
  //         // this.toastrService.info('Order status is delivered!', 'Info!');
  //         this.deliveredOrderForm.reset();
  //         setTimeout(() => {
  //           location.reload();
  //         }, 5);
  //       },
  //       error: (err: { message: any; }) => {
  //         console.log('error->',err.message);
  //         // this.toastrService.error(
  //         //  'Order status was not updated, try again',
  //         //   'Failed!'
  //         // );
  //       },
  //     });
  // }

  // deleteOrder() {
  //   const options = {
  //     status: 'DELETED',
  //   };
  //   this.restApiService
  //     .orderAction(this.orderId, this.deleteOrderForm.value, options)
  //     .subscribe({
  //       next: (res: any) => {
  //         console.log(res);
  //         // this.toastrService.info('Order status is deleted!', 'Info!');
  //         this.deleteOrderForm.reset();
  //         setTimeout(() => {
  //           location.reload();
  //         }, 5);
  //       },
  //       error: (err: { message: any; }) => {
  //         console.log('error->',err.message);
  //         // this.toastrService.error(
  //         //  'Order status was not updated, try again',
  //         //   'Failed!'
  //         // );
  //       },
  //     });
  // }
}
