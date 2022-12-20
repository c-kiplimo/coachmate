import { ClientService } from '../services/ClientService';
import { ApiService } from '../services/ApiService';
import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import {
  faCaretDown,
  faChevronRight,
  faPlus,
  // faRefresh,
} from '@fortawesome/free-solid-svg-icons';
import { style, animate, transition, trigger } from '@angular/animations';
import { FormBuilder, FormGroup } from '@angular/forms';

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
  orderForm!: FormGroup;
  suspendCustomerForm!: FormGroup;
  closeCustomerForm!: FormGroup;
  activateCustomerForm!: FormGroup;
  caretDown = faCaretDown;
  addIcon = faPlus;
  rightIcon = faChevronRight;
  // refreshIcon = faRefresh;
  customerId: any;
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
  orderType = [
    'BIRTHDAY',
    'ANNIVERSARY',
    'BIRTHDAY_GIRL',
    'BIRTHDAY_BOY',
    'BIRTHDAY_ADULT_LADY',
    'BIRTHDAY_ADULT_GUY',
    'BABY_SHOWER_GIRL',
    'BABY_SHOWER_BOY',
    'BRIDAL_SHOWER',
    'BIRTHDAY_6_MONTHS',
    'WEDDING',
    'GRADUATION',
    'OTHER',
  ];
  cakeSize = [
    'ONE_KG',
    'ONE_HALF_KG',
    'TWO_KG',
    'TWO_HALF_KG',
    'THREE_KG',
    'THREE_HALF_KG',
    'FOUR_KG',
    'FOUR_HALF_KG',
    'FIVE_KG',
  ];
  deliveryOption = ['DELIVERED', 'PICKUP'];
  notificationOptions = [false, true];
  notificationType = ['sms', 'email'];
  loadingCustomer = false;
  totalLength: any;

  page: number = 1;
  itemsPerPage = 20;
  ApiService: any;

  constructor(
    private http: HttpClient,
    private formbuilder: FormBuilder,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private toastrService: ToastrService,
    private clientService:ClientService,private restApiService:ApiService
  ) {}


  

  ngOnInit(): void {
    this.customerId = this.activatedRoute.snapshot.params['id'];
    this.getCustomer();

    // this.getNotifications();

    this.orderForm = this.formbuilder.group({
      name: '',
      sessionType: '',
      orderAmount: '',
      amountPaid: '0',
      details: '',
      notes: '',
      deliveryAddress: '',
      dueDate: '',
      customerId: this.customerId,
      sendNotification: true,
      attachment: '',
      deliveryOption: '',
      cakeSize: '',
      messageOnCake: '',
    });

    this.suspendCustomerForm = this.formbuilder.group({
      narration: '',
    });

    this.closeCustomerForm = this.formbuilder.group({
      id: this.customerId,
      narration: '',
    });

    this.activateCustomerForm = this.formbuilder.group({
      narration: '',
    });
  }

  toggleTab(tab: string): void {
    this.currentTab = tab;
  }

  getCustomer(): void {
    this.loadingCustomer = true;

    this.ApiService.getOneCustomer(this.customerId).subscribe(
      (res: any) => {
        console.log('customer data', res.body);
        this.customer = res.body;
        this.getOrders(1);
        this.getPayments();
        this.loadingCustomer = false;
      },
      (error: any) => {
        this.loadingCustomer = false;
      }
    );
  }

  // get orders for specific customer
  getOrders(page: number, navigate?: boolean): void {
    this.searching = true;
    this.orders = [];
    const options = {
      page: page,
      per_page: 10,
      customer_id: this.customerId,
    };
    // console.log(this.customerId);
    this.ApiService.filterOrdersByCustomerId(options).subscribe((res: any) => {
      // console.log('orders->',res.body.data);
      this.orders = res.body.data;
      this.totalLength = Number(res.body.totalElements);
      this.searching = false;
      this.getNotifications();
    });
  }

  // get payments for specific customer
  getPayments(navigate?: boolean): void {
    this.searching = true;
    this.payments = [];
    const options = {
      page: 1,
      per_page: 10,
      customer_id: this.customerId,
    };

    this.ApiService.filterPaymentsByCustomerId(options).subscribe((res: any) => {
      // console.log('payments here=>', res.body.data);
      this.payments = res.body.data;
      this.searching = false;
    });
  }

  // Get Notification for specific customer
  getNotifications(navigate?: boolean): void {
    this.searching = true;
    this.notifications = [];
    const options = {
      page: 1,
      per_page: 10,
      customer_id: this.customerId,
    };

    this.ApiService.getNotificationsbyCustomerId(options).subscribe((res: any) => {
      // console.log(res.body);
      this.notifications = res.body.data;
      this.searching = false;
    });
  }

  newOrder() {
    const names = this.searchTerm.split(' ');

    // console.log(this.orderForm.value);
    this.orderForm.patchValue({
      name: this.customer.firstName + " " + this.orderForm.value.orderType ,
      dueDate: this.orderDueDate + 'T' + this.orderDueTime,
    })
    this.ApiService
      .addNewOrder(this.orderForm.value)
      .subscribe({
        next:(response: any) => {
          this.orderForm.reset();
          this.toastrService.success('Order created!', 'Success!');
          setTimeout(() => {
            location.reload();
          }, 5);},
        error: (err: any) => {
          console.log(err);
          this.toastrService.error(
            'Order not created, try again!',
            'Failed!'
          );
        },
      });
  }
  viewPayment(payment: any): void {
    this.payment = payment;
    this.orders.forEach((order: any) => {
      if (this.payment.order.id === order.id) {
        // console.log(order.id);
        this.payment.balance = order.orderAmount - this.payment.amount;
      }
    });
  }
  viewNotification(notification: any): void {
    this.notification = notification;
  }
  //Customer Actions functions
  suspendCustomer() {
    const options = {
      status: 'SUSPENDED',
    };
    // console.log('details to suspend', this.suspendCustomerForm.value);
    this.ApiService
      .customerAction(this.customerId, this.suspendCustomerForm.value, options)
      .subscribe({
        next: (res: any) => {
          console.log(res);
          this.toastrService.info('Customer suspended!', 'Info!');
          this.suspendCustomerForm.reset();
          setTimeout(() => {
            location.reload();
          }, 5);
        },
        error: (err: any) => {
          console.log(err);
          this.toastrService.error(
            'Customer not suspended, try again!',
            'Failed!'
          );
        },
      });
  }
  closeCustomer() {
    this.ApiService.closeCustomer(this.customerId).subscribe({
      next: (res: any) => {
        console.log(res);
        this.toastrService.info('Customer closed!', 'Info!');
        this.closeCustomerForm.reset();
      },
      error: (err: any) => {
        console.log(err);
        this.toastrService.error('Customer not closed, try again!', 'Failed!');
      },
    });
  }
  activateCustomer() {
    const options = {
      status: 'ACTIVATE',
    };
    this.ApiService
      .customerAction(this.customerId, this.activateCustomerForm.value, options)
      .subscribe({
        next: (res: any) => {
          // console.log(res);
          this.toastrService.success('Customer activated!', 'Success!');
          this.activateCustomerForm.reset();
          setTimeout(() => {
            location.reload();
          }, 5);
        },
        error: (err: any) => {
          console.log(err);
          this.toastrService.error(
            'Customer not activated, try again!',
            'Failed!'
          );
        },
      });
  }
}
