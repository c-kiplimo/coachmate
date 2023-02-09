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
  conductedSessionForm!: FormGroup<any>;
deleteSession() {
throw new Error('Method not implemented.');
}
ConductedSession() {
throw new Error('Method not implemented.');
}
addSessionForm: any;
modalTitle: any;
currentSessionName: any;
currentdetails: any;
currentSessionDate: any;
currentsessionStartTime: any;
currentsessionEndTime: any;
currentsessionVenue: any;
currentgoals: any;
currentSession: any;
feedback: any;
coachId: any;
loadingsession: any;
client: any;
  showHideMessage = true;
  caretDown = faCaretDown;
  addIcon = faPlus;
  rightIcon = faChevronRight;
  backIcon = faChevronLeft;
  alertIcon = faBell;
  notifications!: any;
  notification!: any;
  attachments!: any;
  searching = false;
  currentTab = 'payments';
  loadingOrder = false;
  sessionVenue: any;
  sessionTime: any;
  sessionType: any;
  editedsessionForm!: FormGroup;
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
  coachSessionData: any;
  coachData: any;
  userRole: any;
  orgIdId: any;
  createdBy: any;

  feebackForm: any;


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
    private route: ActivatedRoute,
    private formbuilder: FormBuilder,
    private apiService:ApiService  ) {}

  ngOnInit() {

    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);
    this.userRole = this.coachData.userRole;
    console.log(this.userRole);

    if(this.userRole == 'CLIENT'){
    this.sessionId = this.route.snapshot.params['sessionId'];
    console.log(this.sessionId);

    this.feebackForm = this.formbuilder.group({
      understandingScore: [''],
      emotionalIntelligenceScore: [''],
      listeningSkillsScore: [''],
      clarificationScore: [''],
      availabilityScore: [''],
      comments: ['']
    });

    }

   this.route.params.subscribe((params) => {

      this.sessionId = params['id'];
    });

    this.getSession();
    this.getFeedback();


    this.getNotifications();
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
    this.editedsessionForm = this.formbuilder.group({
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
  //get feedback for session
  getFeedback() {
    const params = {
      sessionId: this.sessionId,
     
    };
    this.clientService.getFeedback(params).subscribe(
      (data: any) => {
        this.feedback = data.body;
        console.log("feedback is here",this.feedback);
      },
      (error) => {
        console.log(error);
      }
    );
  }
  setCurrectSession(session: any) {
    this.currentSession = session;
    console.log(this.currentSession);
  
    this.editedsessionForm = this.formbuilder.group({
     name: this.currentSession.name,
      sessionDate: this.currentSession.sessionDate,
      sessionStartTime: this.currentSession.sessionStartTime,
      sessionEndTime: this.currentSession.sessionEndTime,
      sessionType: this.currentSession.sessionType,
      sessionVenue: this.currentSession.sessionVenue,
      
    });
  }
  editedSession() {
    console.log(this.editedsessionForm.value);
    this.loading = true;
    var data = this.editedsessionForm.value;
    data.id = this.currentSession.id;
    console.log(data);
    this.clientService.editSession(data).subscribe(
      (response: any) => {
        this.loading = false;
        console.log(response);
      }, (error: any) => {
        console.log(error);
      }
    )
  }


  giveFeedback() {
    const params = {
      sessionId: this.sessionId,
      coachId: this.coachId,
      orgIdId: this.orgIdId,
    };
    console.log(this.feebackForm.value);
    const data = this.feebackForm.value;

    data.sessionId = this.sessionId;
    data.orgIdId = this.orgIdId;
    data.coachId = this.coachId;
    data.clientId = this.clientId;
    data.createdBy = this.createdBy;


    this.clientService.addFeedback(data, params).subscribe(
      (response) => {
        console.log(response);
        this.toastrService.success('Feedback Added Successfully');
        this.router.navigate(['sessionView', this.sessionId]);
      },
      (error) => {
        console.log(error);
        this.toastrService.error('Error in adding Feedback');
      }
    );
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
    
      console.log("coach id",this.coachId);
      this.clientId = this.sessions.client.id;
      this.orgIdId = this.sessions.coach.orgIdId;
      this.coachId = this.sessions.coach.id;
      this.createdBy = this.sessions.client.fullName;
      this.getNotifications();
   
     
    });

   
  }


  navigateToSessionView(id: any) {
    console.log(id);
    this.router.navigate(['sessionView', id]);
  }
  getNotifications(navigate?: boolean): void {
    this.searching = true;
    this.notifications = [];
    const options = {
      page: 1,
      per_page: 10,
    };

    this.service.getNotificationsbyOrderId(options).subscribe((res: any) => {
      this.notifications = res.body.data;
      console.log('notification ni', this.notifications);
      this.searching = false;
    });
  }
  viewNotification(notification: any): void {
    this.notification = notification;
    console.log(this.notification);
  }
 //session Actions functions
 confirmSession() {
  const options = {
    status: 'CONFIRMED',
  };
  console.log(this.confirmSessionForm.value);
  this.service
    .orderAction(this.sessionId, this.confirmSessionForm.value, options)
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
    .orderAction(this.sessionId, this.cancelSessionForm.value, options)
    .subscribe({
      next: (res: any) => {
        console.log(res);
        this.toastrService.info('Order status is cancelled!', 'Info!');
        this.cancelSessionForm.reset();
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


 
}