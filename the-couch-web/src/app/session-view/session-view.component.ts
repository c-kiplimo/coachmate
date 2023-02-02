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
  faPlus,
  faRedo,
  // faRefresh,
} from '@fortawesome/free-solid-svg-icons';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup } from '@angular/forms';
import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { ApiService } from '../services/ApiService';

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
addSessionForm: any;
modalTitle: any;
editedsession() {
throw new Error('Method not implemented.');
}
loadingsession: any;
client: any;
  showHideMessage = true;
  cancelOrderForm!: FormGroup;
  deleteOrderForm!: FormGroup;
  deliveredOrderForm!: FormGroup;
  editedOrderForm!: FormGroup;
  editPaymentForm!: FormGroup;
  caretDown = faCaretDown;
  addIcon = faPlus;
  rightIcon = faChevronRight;
  backIcon = faChevronLeft;
  alertIcon = faBell;
  payments!: any;
  notifications!: any;
  notification!: any;
  attachments!: any;
  orderId!: any;
  customerId!: any;
  paymentId!: any;
  payment!: any;
  order!: any;
  response_data!: any;
  searching = false;
  currentTab = 'payments';
  loadingOrder = false;
  deliveredDate = '';
  deliveredTime = '';
  paymentForm!: FormGroup;
  sessionVenue: any;
  sessionTime: any;
  sessionType: any;
  editedsessionForm!: FormGroup<any>;
  isPaymentRef: any;
  loading = false;
  service: any;
  sessions:any;
  session:any;
  itemsPerPage = 20;
  filters: any = {
    status: '',
    searchItem: '',
  };
  sessionId!: any;
  clientId: any;
  confirmSessionForm: any;
  cancelSessionForm: any;
  deliveredSessionForm: any;
  deleteSessionForm: any;
  toastrService: any;
  sessionDate = '';
  sessionStartTime = '';
  sessionDuration = '';
  selectedPaymentOption: any;
  sessionAmount = '';
  eventType = '';
  open = false;

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
    private clientService:ClientService,
    private http: HttpClient,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private formbuilder: FormBuilder,
    private apiService:ApiService  ) {}

  ngOnInit() {
   this.activatedRoute.params.subscribe((params) => {
      this.sessionId = params['id'];
    });
    this.getSession();
    this.getPayments();
    this.getNotifications();
    this.getAllSessions();

    this.paymentForm = this.formbuilder.group({
      extPaymentRef: '',
      amount: '',
      narration: '',
    
      sendNotification: true,
    });
    this.paymentForm = this.formbuilder.group({
      paymentType:'',
      extPaymentRef: '',
      amount: '',
      narration: '',
      orderId: this.sessionId,
      clientId: this.clientId,
      sendNotification: true,
    });
    this.editPaymentForm = this.formbuilder.group({
      paymentType:'',
      extPaymentRef: '',
      amount: '',
      narration: '',
      orderId: this.sessionId,

      sendNotification: true,
    });
    this.confirmSessionForm = this.formbuilder.group({
      narration: '',
    });
    this.cancelSessionForm = this.formbuilder.group({
      narration: '',
      isSendNotification: true,
    });
    this.deliveredSessionForm = this.formbuilder.group({
      deliveredOn: '',
      narration: '',
      isSendNotification: true,
    });
    this.deleteSessionForm = this.formbuilder.group({
      narration: '',
    });
    this.editPaymentForm = this.formbuilder.group({
      paymentType: this.payments.paymentType,
      extPaymentRef: this.payments.extPaymentRef,
      amount: this.payments.amount,
      narration: '',
      sendNotification: true,
    });
  
  }
  goToItem(type: any, entityObj: any): void {
    this.router.navigate([type, entityObj.id]);
  }
  toggleTab(tab: string): void {
    this.currentTab = tab;
  }
  getSession() {
    this.loading = true;

    this.clientService.getOneSession(this.sessionId).subscribe((res: any) => {
      
      this.loading = false;
      this.sessions = res.body;
      console.log(this.sessions);

      this.clientId = this.order.client.id;
      this.getPayments();
      this.getNotifications();
      this.editPaymentForm = this.formbuilder.group({
        paymentType: this.payments.paymentType,
        extPaymentRef: this.payments.extPaymentRef,
        amount: this.payments.amount,
        narration: '',
        orderId: this.orderId,
        sendNotification: true,
      });
   
    });
  }
  getAllSessions() {
    this.loading = true;
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
    };
    this.clientService.getSessions(options).subscribe(
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
  @ViewChild('modal', { static: false })
  modal!: ElementRef;

openModal() {
    this.modal.nativeElement.style.display = 'block';
    document.body.classList.add('modal-open');
}

closeModal() {
    this.modal.nativeElement.style.display = 'none';
    document.body.classList.remove('modal-open');
}

  navigateToSessionView(id: any) {
    console.log(id);
    this.router.navigate(['sessionView', id]);
  }
  // get payments for specific order
  getPayments(navigate?: boolean): void {
    this.searching = true;
    this.payments = [];
    const options = {
      page: 1,
      per_page: 10,
      order_id: this.orderId,
    };

    this.service.filterPaymentsBySessionId(options).subscribe((res: any) => {
      this.payments = res.body.data;
      console.log('payments ni', this.payments);
      this.searching = false;
    });
  }
  getNotifications(navigate?: boolean): void {
    this.searching = true;
    this.notifications = [];
    const options = {
      page: 1,
      per_page: 10,
      order_id: this.orderId,
    };

    this.service.getNotificationsbyOrderId(options).subscribe((res: any) => {
      this.notifications = res.body.data;
      console.log('notification ni', this.notifications);
      this.searching = false;
    });
  }

  newPayment() {
    // console.log(this.paymentForm.value);
    this.paymentForm.patchValue({
      sendNotification: this.showHideMessage,
      paymentType: this.selectedPaymentOption,
    });
    this.service.addNewPayment(this.paymentForm.value).subscribe({
      next: (response: any) => {
        this.paymentForm.reset();
        this.toastrService.success('Payment added!', 'Success!');
        setTimeout(() => {
          location.reload();
        }, 5);
      },
      error: (err: any) => {
        console.log(err);
        this.toastrService.error('Payment not added, try again!', 'Failed!');
      },
    });
  }

  viewPayment(payment: any): void {
    console.log(this.order);
    this.payment = payment;
    this.paymentId = this.payment.id;
    if (this.payment.order.id === this.order?.id) {
      console.log(this.order.id);
      this.payment.balance = this.order.orderAmount - this.payment.amount;
    }
  }
  editPayment(payment: any): void {
    this.payment = payment;
    this.paymentId = this.payment.id;
  }
  viewNotification(notification: any): void {
    this.notification = notification;
    console.log(this.notification);
  }

  updatePaymentDetails() {
  
    console.log(this.paymentId);
    this.service
      .editPayment(this.paymentId, this.editPaymentForm.value)
      .subscribe({
        next: (response: any) => {
          console.log(response);
          this.toastrService.success('Payment details updated!', 'Success!');
          setTimeout(() => {
            location.reload();
          }, 2);
        },
        error: (err: any) => {
          console.log(err);
          this.toastrService.error(
            'Payment not updated, try again!',
            'Failed!'
          );
        },
      });
  }
 //session Actions functions
 confirmSession() {
  const options = {
    status: 'CONFIRMED',
  };
  console.log(this.confirmSessionForm.value);
  this.service
    .orderAction(this.orderId, this.confirmSessionForm.value, options)
    .subscribe({
      next: (res: any) => {
        console.log(res);
        this.toastrService.info('Order confirmed!', 'Info!');
        this.confirmSessionForm.reset();
        setTimeout(() => {
          location.reload();
        }, 5);
      },
      error: (err: { message: any; }) => {
        console.log('error->', err.message);
        this.toastrService.error(
          'Order status was not updated, try again',
          'Failed!'
        );
      },
    });
}

cancelSession() {
  const options = {
    status: 'CANCELLED',
  };
  this.service
    .orderAction(this.orderId, this.cancelOrderForm.value, options)
    .subscribe({
      next: (res: any) => {
        console.log(res);
        this.toastrService.info('Order status is cancelled!', 'Info!');
        this.cancelOrderForm.reset();
        setTimeout(() => {
          location.reload();
        }, 5);
      },
      error: (err: { message: any; }) => {
        console.log('error->', err.message);
        this.toastrService.error(
          'Order status was not updated, try again',
          'Failed!'
        );
      },
    });
}

ConductedSession() {
  this.deliveredOrderForm.patchValue({
    deliveredOn: this.deliveredDate + 'T' + this.deliveredTime,
  });
  const options = {
    status: 'DELIVERED',
  };
  this.service
    .orderAction(this.orderId, this.deliveredOrderForm.value, options)
    .subscribe({
      next: (res: any) => {
        console.log(res);
        this.toastrService.info('Order status is delivered!', 'Info!');
        this.deliveredOrderForm.reset();
        setTimeout(() => {
          location.reload();
        }, 5);
      },
      error: (err: { message: any; }) => {
        console.log('error->', err.message);
        this.toastrService.error(
          'Order status was not updated, try again',
          'Failed!'
        );
      },
    });
}

deleteSession() {
  const options = {
    status: 'DELETED',
  };
  this.service
    .orderAction(this.orderId, this.deleteOrderForm.value, options)
    .subscribe({
      next: (res: any) => {
        console.log(res);
        this.toastrService.info('Order status is deleted!', 'Info!');
        this.deleteOrderForm.reset();
        setTimeout(() => {
          location.reload();
        }, 5);
      },
      error: (err: { message: any; }) => {
        console.log('error->', err.message);
        this.toastrService.error(
          'Order status was not updated, try again',
          'Failed!'
        );
      },
    });
}

  sendReminder() {
    this.service.paymentReminder(this.sessionId).subscribe({
      next: (res: any) => {
        console.log(res);
        this.toastrService.info('Order reminder was sent!', 'Info!');
        setTimeout(() => {
          location.reload();
        }, 5);
      },
      error: (err:any) => {
        console.log('error->', err.message);
        this.toastrService.error(
          'Order reminder not sent, try again',
          'Failed!'
        );
      },
    });
  }
  back() {
    window.history.back();
  }
  reload() {
    location.reload();
  }


  savePdf() {

    let DATA: any = document.getElementById("invoice");
    // html2canvas(DATA, { logging: true, allowTaint: true, useCORS: true }).then(
    //   (canvas: { height: number; width: number; toDataURL: (arg0: string) => any; }) => {
    //     let fileWidth = 200;
    //    // let fileHeight =100;
    //    let fileHeight = (canvas.height * fileWidth) / canvas.width;
    //     const FILEURI = canvas.toDataURL("image/*");
    //     // let PDF = new jsPDF("p", "mm", "a4");
    //     let position = 0;
    //     // PDF.addImage(FILEURI, "image/*", 0, position, fileWidth, fileHeight);
    //     // PDF.save("invoice.pdf");

    //   }
    
  }
 // toggle paymentRef
 showPaymentRef(val: String): void {
  if (val === "MPESA") {
    this.isPaymentRef = true;
    this.selectedPaymentOption = 'MPESA';
  } else if(val === "BANK") {
    this.isPaymentRef = false;
    this.selectedPaymentOption = 'BANK';
  }
  else{
    this.isPaymentRef = false;
    this.selectedPaymentOption = 'CASH';
  }
}
}

