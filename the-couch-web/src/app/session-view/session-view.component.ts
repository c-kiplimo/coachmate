import { Component, ElementRef, HostListener, OnInit, ViewChild } from '@angular/core';
import { ClientService } from '../services/ClientService';
import { style, animate, transition, trigger } from '@angular/animations';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { HttpHeaders } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup } from '@angular/forms';
import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { ApiService } from '../services/ApiService';
import { FeedbackService } from '../services/feedback.service';
import { timeout } from 'rxjs';

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
  attachmentForm!: FormGroup;
  status!: string;
  orgId: any;
  organizationId: any;
  feedback: any;
  notification: any;
  addSessionForm: any;
  statusForm!: FormGroup;
  modalTitle: any;
  feedbacks: any = [];
  attachmentss: any = [];
  attachment: any;
  coachId: any;
  loadingsession: any;
  client: any;
  showHideMessage = true;
  notifications!: any[];
  searching = false;
  currentTab = 'feedback';
  loadingOrder = false;
  sessionVenue: any;
  sessionTime: any;
  sessionType: any;
  editedsessionForm!: FormGroup;
  feebackForm!: FormGroup<any>;
  loading = false;
  service: any;
  sessions: any;
  session: any;
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
  coachSessionData: any;
  coachData: any;
  userRole: any;
  orgIdId: any;
  createdBy: any;
  links: string[] = [];
  files: File[] = [];
  //Add Link Form
  Links = {
    link: ''
  };
  @ViewChild('attachmentModal', { static: false })
  attachmentModal!: ElementRef;
  attachments: any;
  user: any;
  OrgData: any;
  orgSession: any;
  currentSession!: any;
  constructor(
    private clientService: ClientService,
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
    private formbuilder: FormBuilder,
    private apiService: ApiService,
    private feedbackService: FeedbackService,
    private toastrService: ToastrService) { }

  ngOnInit() {
    this.coachSessionData = sessionStorage.getItem('user');
    this.user = JSON.parse(this.coachSessionData);

    this.userRole = this.user.userRole;
    this.route.params.subscribe((params) => {
      this.sessionId = params['id'];
    });

    if (this.userRole == 'COACH') {
      this.coachId = this.user.id;
    } else if (this.userRole == 'ORGANIZATION') {
      this.orgId = this.user.organization.id;
    } else if (this.userRole == 'CLIENT') {
      this.clientId = this.user.id;
      this.coachId = this.user.addedBy.id;
    }

    this.feebackForm = this.formbuilder.group({
      understandingScore: '',
      emotionalIntelligenceScore: '',
      listeningSkillsScore: '',
      clarificationScore: '',
      availabilityScore: '',
      comments: '',
    });
    this.attachmentForm = this.formbuilder.group({
      links: '',
      files: [[]],

    })

    this.getSession();
    this.getFeedback();
    this.getAttachment();
    this.editedsessionForm = this.formbuilder.group({
      sessionDate: '',
      sessionStartTime: '',
      sessionDuration: '',
      sessionType: '',
      sessionVenue: '',
      name: '',
      sessionDetails: '',
      sessionEndTime: '',
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
        console.log("feedback is here", this.feedbacks);
      },
      (error) => {
        console.log(error);
      }
    );
  }
  //get attachment for session
  getAttachment() {
    const params = {
      sessionId: this.sessionId,

    };
    console.log("session id", params)
    this.clientService.getAttachment(params).subscribe(
      (res: any) => {
        this.attachments = res.body;
        console.log("attachment is here", this.attachments);
      },
      (error) => {
        console.log(error);
      }
    );
  }
  editSession(session: any) {
    this.currentSession = session;
    console.log(this.currentSession);

    this.editedsessionForm = this.formbuilder.group({
      name: this.currentSession.name,
      sessionDate: this.currentSession.sessionDate,
      sessionStartTime: this.currentSession.sessionStartTime,
      sessionEndTime: this.currentSession.sessionEndTime,
      sessionType: this.currentSession.sessionType,
      sessionVenue: this.currentSession.sessionVenue,
      sessionDetails: this.currentSession.sessionDetails,


    });
  }
  @ViewChild('editsessionModal', { static: false })
  editsessionModal!: ElementRef;
  @ViewChild('addfeedbackModal', { static: false })
  addfeedbackModal!: ElementRef;
  editedSession(id: any) {
    console.log(id);
    this.currentSession = this.editedsessionForm.value;
    console.log(this.currentSession);
    console.log(this.editedsessionForm.value);
    var data = this.editedsessionForm.value;
    data.id = this.currentSession.id;
    console.log(data);
    this.clientService.editSession(data, id).subscribe(
      (response: any) => {
        this.toastrService.success('session updated!', 'Success!', { timeOut: 8000 });
        setTimeout(() => {
          location.reload();
        }, 1000);
        this.editsessionModal.nativeElement.classList.remove('show');
        this.editsessionModal.nativeElement.style.display = 'none';

      }, (error) => {
        console.log(error)
        this.toastrService.success('session not updated!', 'Failed!', { timeOut: 8000 });
        this.editsessionModal.nativeElement.classList.remove('show');
        this.editsessionModal.nativeElement.style.display = 'none';
      }
    );
  }
  viewComment(feedback: any): void {
    this.feedback = feedback;
    console.log(this.feedback);
  }
  @ViewChild('confirmsessionModal', { static: false })
  confirmsessionModal!: ElementRef;
  @ViewChild('conductedsessionModal', { static: false })
  conductedsessionModal!: ElementRef;
  @ViewChild('cancelsessionModal', { static: false })
  cancelsessionModal!: ElementRef;
  showStatus: any;
  // CONFIRMED
  // CANCELLED,  
  // CONDUCTED

  statusState(currentstatus: any) {
    console.log(currentstatus);
    if (currentstatus === 'CONFIRMED') {
      this.showStatus = "CONFIRMED";
      this.status = "CONFIRMED";
    } else if (currentstatus === 'CANCELLED') {
      this.showStatus = "CANCELLED";
      this.status = "CANCELLED";
    } else if (currentstatus === 'CONDUCTED') {
      this.showStatus = "CONDUCTED";
      this.status = "CONDUCTED";
    }

  }
  changeSessionStatus() {
    console.log(this.status);
    let data = {

      status: this.status,
    }
    console.log(data);
    if (this.status === "CONFIRMED") {
      this.clientService.changeSession(this.sessionId, data).subscribe(
        (res) => {
          console.log(res);
          this.toastrService.success('session confirmed!', 'Success!', { timeOut: 8000 });
          setTimeout(() => {
            location.reload();
          }, 1000);
          this.confirmsessionModal.nativeElement.classList.remove('show');
          this.confirmsessionModal.nativeElement.style.display = 'none';

        }, (error) => {
          console.log(error)
          this.toastrService.error('session not confirmed!', 'Failed!', { timeOut: 8000 });
          this.confirmsessionModal.nativeElement.classList.remove('show');
          this.confirmsessionModal.nativeElement.style.display = 'none';
        }
      );
    }

    if (this.status === "CANCELLED") {
      this.clientService.changeSession(this.sessionId, data).subscribe(
        (res) => {
          console.log(res);
          this.toastrService.success('cancelled successfully', 'Success!', { timeOut: 8000 });
          setTimeout(() => {
            location.reload();
          }, 1000);
          this.cancelsessionModal.nativeElement.classList.remove('show');
          this.cancelsessionModal.nativeElement.style.display = 'none';

        }, (error) => {
          console.log(error)
          this.toastrService.error('cancelled failed', 'Failed!', { timeOut: 8000 });
          this.cancelsessionModal.nativeElement.classList.remove('show');
          this.cancelsessionModal.nativeElement.style.display = 'none';
        }
      );
    }

    if (this.status === "CONDUCTED") {
      this.clientService.changeSession(this.sessionId, data).subscribe(
        (res) => {
          console.log(res);
          this.toastrService.success('Status changed successfully', 'Success!', { timeOut: 8000 });
          setTimeout(() => {
            location.reload();
          }, 1000);
          this.conductedsessionModal.nativeElement.classList.remove('show');
          this.conductedsessionModal.nativeElement.style.display = 'none';

        }, (error) => {
          console.log(error)
          this.toastrService.error('Status changed failed', 'Failed!', { timeOut: 8000 });
          this.conductedsessionModal.nativeElement.classList.remove('show');
          this.conductedsessionModal.nativeElement.style.display = 'none';
        }
      );
    }
  }
  getClass(session: any) {
    if (session.status === 'SUSPENDED') {
      return 'badge-warning';
    } else if (session.status === 'CONFIRMED') {
      return 'badge-success';
    }
    else if (session.status === 'NEW') {
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
      clientId: this.clientId,

    };
    console.log(params);
    console.log(this.feebackForm.value);
    this.feedbackService.addFeedback(this.feebackForm.value, params).subscribe(
      (response) => {
        console.log(response);
        this.toastrService.success('Feedback added successfully', 'Success!', { timeOut: 8000 });
        this.addfeedbackModal.nativeElement.classList.remove('show');
        this.addfeedbackModal.nativeElement.style.display = 'none';
      },
      (error) => {
        console.log(error);
        this.toastrService.error('Feedback not added', 'Failed!', { timeOut: 8000 });
      }
    );
  }

  onFileSelected(event: Event) {
    const files = (event.target as HTMLInputElement).files;
    if (files) {
      for (let i = 0; i < files.length; i++) {
        this.files.push(files[i]);
      }
    }
  }

  addFile() {
    const input = document.createElement('input');
    input.type = 'file';
    input.multiple = true;
    input.addEventListener('change', (event: Event) => {
      const files = (event.target as HTMLInputElement).files;
      if (files) {
        for (let i = 0; i < files.length; i++) {
          this.files.push(files[i]);
        }
      }
    });
    input.click();
    console.log(this.files);
  }


  removeFile(index: number) {
    this.files.splice(index, 1);
  }


  addLink() {

    console.log(this.Links);
    this.links.push(this.Links.link);
    console.log(this.links);
    this.Links.link = '';
    console.log(this.Links);
  }

  removeLink(index: number) {
    this.links.splice(index, 1);
  }
  addAttachment() {
    const boundary = '-------------------------' + Math.random().toString(16).substring(2);
    const headers = new HttpHeaders({
      'Content-Type': 'multipart/form-data; boundary=' + boundary
    });
    const formData = this.attachmentForm.value;
    formData.links = this.links;
    formData.files = this.files;
    const params = {
      sessionId: this.sessionId,
      coachId: this.coachId,
    };
    formData.sessionId = this.sessionId;
    formData.coachId = this.coachId;
    formData.clientId = this.clientId;
    formData.createdBy = this.createdBy;
    console.log(formData);
    console.log(this.files);
    // Send formData to backend using a service or API
    this.clientService.addAttachment(formData, params, headers).subscribe(
      (response) => {
        console.log(response);
        this.toastrService.success('Attachment added successfully', 'Success!', { timeOut: 8000 });
        this.attachmentModal.nativeElement.classList.remove('show');
        this.attachmentModal.nativeElement.style.display = 'none';
      },
      (error) => {
        console.log(error);
        this.toastrService.error('Attachment not added', 'Failed!', { timeOut: 8000 });
      }
    );
  }





  toggleTab(tab: string): void {
    this.currentTab = tab;
  }

  getSession() {
    this.loading = true;
    this.clientService.getOneSession(this.sessionId).subscribe((res: any) => {
      console.log("sessionId", this.sessionId);
      this.sessions = res.body;
      console.log(this.sessions);
      this.loading = false;


    });


  }
  viewNotification(notification: any): void {
    this.notification = notification;
    console.log(this.notification);
  }
  back() {
    window.history.back();
  }
  reload() {
    location.reload();
  }



}