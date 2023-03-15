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
  status!: string;
  orgId: any;
  organizationId: any;
  feedback: any;
  notification: any;
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
feedbacks:any = [];
attachments:any = [];
attachment: any;
coachId: any;
loadingsession: any;
client: any;
  showHideMessage = true;
  caretDown = faCaretDown;
  addIcon = faPlus;
  rightIcon = faChevronRight;
  backIcon = faChevronLeft;
  alertIcon = faBell;
  notifications!: any[];
  searching = false;
  currentTab = 'feedback';
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
  deleteSessionForm: any;
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
  attachmentForm:any;


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
    private apiService:ApiService,
    private toastrService:ToastrService  ) {}

  ngOnInit() {

    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);
    this.userRole = this.coachData.userRole;
    console.log(this.userRole);

    if(this.userRole == 'COACH'){
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
    this.attachmentForm = this.formbuilder.group({
      uploads:[''],
      links:['']
    }

    )

    }
    if(this.userRole == 'COACH'){
      this.sessionId = this.route.snapshot.params['sessionId'];
      console.log(this.sessionId);
    
    }

   this.route.params.subscribe((params) => {

      this.sessionId = params['id'];
    });

    this.getSession();
    this.getFeedback();
    this. getAttachment()


    this.getNotifications();
    this.confirmSessionForm = this.formbuilder.group({
      narration: '',
    });
    this.cancelSessionForm = this.formbuilder.group({
      narration: '',
      isSendNotification: true,
    });
    this.conductedSessionForm = this.formbuilder.group({
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
      (res: any) => {
        this.feedbacks = res.body;
        console.log("feedback is here",this.feedbacks);
      },
      (error) => {
        console.log(error);
      }
    );
  }
    //get attachment for session
    getAttachment(){
      const params = {
        sessionId: this.sessionId,
       
      };
      this.clientService.getAttachment(params).subscribe(
        (res: any) => {
          this.attachments = res.body;
          console.log("attachment is here",this.attachments);
        },
        (error) => {
          console.log(error);
        }
      );
    }
    getNotifications(): void {
      this.searching = true;
      this.notifications = [];
      const options = {
         sessionId: this.sessionId,
         coachId :this.coachData.id,
        page: 1,
        per_page: 10,
      };
  
      this.clientService.getAllNotifications(options).subscribe((res: any) => {
        this.notifications = res.body;
        console.log('notification ni', this.notifications);
        this.searching = false;
      });
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
  viewComment(feedback: any): void {
    this.feedback = feedback;
    console.log(this.feedback);
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
      this.clientService.changeClientStatus(this.clientId, "ACTIVE").subscribe(
        (response) => {
          this.router.navigate(['/clients']);
        }, (error) => {
          console.log(error)
        }
      );
    }

    if(this.status === "SUSPENDED") {
      this.clientService.changeClientStatus(this.clientId, "SUSPENDED").subscribe(
        (response) => {
          this.router.navigate(['/clients']);
        }, (error) => {
          console.log(error)
        }
      );
    }

    if(this.status === "CLOSED") {
      this.clientService.changeClientStatus(this.clientId, "CLOSED").subscribe(
        (response) => {
          this.router.navigate(['/clients']);
        }, (error) => {
          console.log(error)
        }
      );
    }
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
  getClass(session: any) {
    if (session.status === 'SUSPENDED') {
        return 'badge-warning';
    } else if (session.status === 'CONFIRMED') {
        return 'badge-success';
    }
    else if (session.status === 'NEW'){
      return 'badge-success'
    }
    else {
        return 'badge-success';
    }
}
  giveFeedback() {
    const params = {
      sessionId: this.sessionId,
      coachId: this.coachId,
    
    };
    console.log(this.feebackForm.value);
    const data = this.feebackForm.value;

    data.sessionId = this.sessionId;
    
    data.coachId = this.coachId;
    data.clientId = this.clientId;
    data.createdBy = this.createdBy;


    this.clientService.addFeedback(data, params).subscribe(
      (response) => {
        console.log(response);
        this.toastrService.success('Feedback Added Successfully');
        this.feebackForm.nativeElement.classList.remove('show');
        this.feebackForm.nativeElement.style.display = 'none';
      },
      (error) => {
        console.log(error);
        this.toastrService.error('Error in adding Feedback');
      }
    );
  }
  addAttachment() {
    const params = {
      sessionId: this.sessionId,
      coachId: this.coachId,
    
    };
    console.log(this. attachmentForm.value);
    const data = this. attachmentForm.value;

    data.sessionId = this.sessionId;
    
    data.coachId = this.coachId;
    data.clientId = this.clientId;
    data.createdBy = this.createdBy;


    this.clientService.addAttachment(data, params).subscribe(
      (response) => {
        console.log(response);
        this.toastrService.success('Attachment Added Successfully');
        this. attachmentForm.nativeElement.classList.remove('show');
        this. attachmentForm.nativeElement.style.display = 'none';
      },
      (error) => {
        console.log(error);
        this.toastrService.error('Error in adding Attachment');
      }
    );
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
    
      
      this.clientId = this.sessions.client.id;
      this.orgIdId = this.sessions.orgId;
      console.log("org id",this.orgIdId);
      this.coachId = this.sessions.coach.id;
      console.log("coach id",this.coachId);
      this.createdBy = this.sessions.client.fullName;
   
     
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