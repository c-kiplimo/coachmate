import { Component, OnInit } from '@angular/core';
import { ClientService } from '../services/ClientService';
import { SessionsService } from '../services/SessionsService';
import { style, animate, transition, trigger } from '@angular/animations';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import {
  faCaretDown,
  faChevronRight,
  faPlus,
  faRedo,
  // faRefresh,
} from '@fortawesome/free-solid-svg-icons';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup } from '@angular/forms';
import { IconProp } from '@fortawesome/fontawesome-svg-core';

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
savePdf() {
throw new Error('Method not implemented.');
}
loadingsession: any;
sendReminder() {
throw new Error('Method not implemented.');
}
deleteSession() {
throw new Error('Method not implemented.');
}
cancelSession() {
throw new Error('Method not implemented.');
}
  cancelsessionForm!: FormGroup<any>;
deliveredSession() {
throw new Error('Method not implemented.');
}
confirmsession() {
throw new Error('Method not implemented.');
}
newPayment() {
throw new Error('Method not implemented.');
}
  alertIcon!: IconProp;
goToItem(arg0: string,_t163: any) {
throw new Error('Method not implemented.');
}
viewNotification(_t232: any) {
throw new Error('Method not implemented.');
}
showPaymentRef(arg0: string) {
throw new Error('Method not implemented.');
}
sessionVenue: any;
sessionTime: any;
sessionType: any;
  editedsessionForm!: FormGroup<any>;
showHideMessage: any;
isPaymentRef: any;
editedsession() {
throw new Error('Method not implemented.');
}

  paymentForm!: FormGroup;
  
  caretDown = faCaretDown;
  addIcon = faPlus;
  rightIcon = faChevronRight;
  refreshIcon =faRedo;
  payments!: any;
  notifications!: any;
  notification!: any;
  attachments!: any;
 
  payment!: any;
  order!: any;
  response_data!: any;
  searching = false;
  currentTab = 'payments';
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
  editPaymentForm: any;
  confirmSessionForm: any;
  cancelSessionForm: any;
  deliveredSessionForm: any;
  deleteSessionForm: any;
  paymentId: any;
  toastrService: any;
  sessionDate = '';
  sessionStartTime = '';
  sessionDuration = '';



  constructor(
    private restApiService:ClientService,
    private http: HttpClient,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private formbuilder: FormBuilder,
    private sessionService:SessionsService 
  ) {}

  ngOnInit() {
   this.activatedRoute.params.subscribe((params) => {
      this.sessionId = params['id'];
    });
    this.getSession();
  //  this.getAllSessions();

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

  
  }
  updatePaymentDetails() {
    // this.editPaymentForm.patchValue({
    //   dueDate: this.orderDueDate + 'T' + this.orderDueTime,
    // });
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
  toggleTab(tab: string): void {
    this.currentTab = tab;
  }
  // getAllSessions(){

  //   const options = {
  //     page: 1,
  //     per_page: this.itemsPerPage,
  //     status: this.filters.status,
  //     search: this.filters.searchItem,
  //   };
  //   this.restApiService.getSessions(options).subscribe(
  //     (response: any) => {
  //       console.log(response)
      
  //     }, (error: any) => {
  //       console.log(error)
  //     }
  //   )
  // }
  getSession() {
    this.loading = true;

    this.sessionService.getOneSession(this.sessionId).subscribe((res: any) => {
      
      this.loading = false;
      this.sessions = res.body;
      console.log(this.sessions);
   
    });
  }


}
