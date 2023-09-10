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
import { AttachmentService } from '../services/AttachmentService';

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
onLinkChange() {
throw new Error('Method not implemented.');
}
  conductedSessionForm!: FormGroup<any>;
  attachmentNo: any;
  status!: string;
  orgId: any;
  organizationId: any;
  feedback: any;
  notification: any;
  addSessionForm: any;
  statusForm!: FormGroup;
  modalTitle: any;
  feedbacks: any = [];
  attachments: any = [];
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
  links: any = [];
  linksObj: any = {};
  files: File[] = [];
  addlink: any;
  @ViewChild('attachmentModal', { static: false })
  attachmentModal!: ElementRef;
 
  user: any;
  OrgData: any;
  orgSession: any;
  currentSession!: any;

  @ViewChild('stickyMenu')
  menuElement!: ElementRef;
  sticky: boolean = false;
  elementPosition: any;
  proBonoHoursForm!: FormGroup<any>;

  constructor(
    private clientService: ClientService,
    private http: HttpClient,
    private attachmentService: AttachmentService,
    private router: Router,
    private route: ActivatedRoute,
    private formbuilder: FormBuilder,
    private apiService: ApiService,
    private feedbackService: FeedbackService,
    private toastrService: ToastrService) {}

  ngOnInit() {
    this.coachSessionData = sessionStorage.getItem('user');
    this.user = JSON.parse(this.coachSessionData);

    this.userRole = this.user.userRole;
    this.route.params.subscribe((params) => {
      this.sessionId = params['id'];
      const sessionId = params['id'];
      this.getAttachment(sessionId);
      this.getFeedback(sessionId);
    });

    if (this.userRole == 'COACH') {
      this.coachId = this.user.id;
    } else if (this.userRole == 'ORGANIZATION') {
      this.orgId = this.user.organization.id;
    } else if (this.userRole == 'CLIENT') {
      this.clientId = this.user.id;
      this.coachId = this.user.addedBy.id;
      this.currentTab = 'attachments';
    }

    this.feebackForm = this.formbuilder.group({
      understandingScore: '',
      emotionalIntelligenceScore: '',
      listeningSkillsScore: '',
      clarificationScore: '',
      availabilityScore: '',
      comments: '',
    });


    this.getSession();
    
   
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

    this.proBonoHoursForm = this.formbuilder.group({
      proBonoHours: [0],
    });
  }

  ngAfterViewInit() {
    this.elementPosition = this.menuElement?.nativeElement?.offsetTop;
  }
  @HostListener('window:scroll', ['$event'])
  handleScroll() {
    const windowScroll = window.pageYOffset;
    if (windowScroll >= this.elementPosition) {
      this.sticky = true;
    } else {
      this.sticky = false;
    }
  }

  //get feedback for session
  getFeedback(sessionId: any) {
    const params = {
      sessionId: this.sessionId,

    };
    this.clientService.getFeedback(params).subscribe(
      (res: any) => {
        this.feedbacks = res.body;
      },
      (error) => {
      }
    );
  }
  resetForm() {
    this.addlink = '';
  }
  //get attachment for session
  getAttachment( sessionId: any) {
    this.loading = true;
    this.clientService.getAttachment(sessionId).subscribe(
      (data: any) => {
        this.attachments = data.body;
        this.loading = false;
      },
      (error: any) => {
        this.loading = false;
      }
    );
  }
  
  editSession(session: any) {
    this.currentSession = session;

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
    this.currentSession = this.editedsessionForm.value;
    var data = this.editedsessionForm.value;
    data.id = this.currentSession.id;
    this.clientService.editSession(data, id).subscribe(
      (response: any) => {
        this.toastrService.success('session updated!', 'Success!', { timeOut: 8000 });
        setTimeout(() => {
          location.reload();
        }, 1000);
        this.editsessionModal.nativeElement.classList.remove('show');
        this.editsessionModal.nativeElement.style.display = 'none';

      }, (error) => {
        this.toastrService.success('session not updated!', 'Failed!', { timeOut: 8000 });
        this.editsessionModal.nativeElement.classList.remove('show');
        this.editsessionModal.nativeElement.style.display = 'none';
      }
    );
  }
  viewComment(feedback: any): void {
    this.feedback = feedback;
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
    if (currentstatus === 'CONFIRMED') {
      this.showStatus = "CONFIRMED";
      this.status = "CONFIRMED";
    } else if (currentstatus === 'CANCELLED') {
      this.showStatus = "CANCELLED";
      this.status = "CANCELLED";
    } else if (currentstatus === 'COMPLETED') {
      this.showStatus = "COMPLETED";
      this.status = "COMPLETED";
    }

  }
  changeSessionStatus() {
    let data = {

      status: this.status,
    }

    let options = {
      proBonoHours: this.proBonoHoursForm.value.proBonoHours,
    };

    if (this.status === "CONFIRMED") {
      this.clientService.changeSession(this.sessionId, data, options).subscribe(
        (res) => {
          this.toastrService.success('session confirmed!', 'Success!', { timeOut: 8000 });
          setTimeout(() => {
            location.reload();
          }, 1000);
          this.confirmsessionModal.nativeElement.classList.remove('show');
          this.confirmsessionModal.nativeElement.style.display = 'none';

        }, (error) => {
          this.toastrService.error('session not confirmed!', 'Failed!', { timeOut: 8000 });
          this.confirmsessionModal.nativeElement.classList.remove('show');
          this.confirmsessionModal.nativeElement.style.display = 'none';
        }
      );
    }

    if (this.status === "CANCELLED") {
      this.clientService.changeSession(this.sessionId, data, options).subscribe(
        (res) => {
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

    if (this.status === "COMPLETED") {
      this.clientService.changeSession(this.sessionId, data, options).subscribe(
        (res) => {
          this.toastrService.success('Status changed successfully', 'Success!', { timeOut: 8000 });
          setTimeout(() => {
            location.reload();
          }, 1000);
          this.conductedsessionModal.nativeElement.classList.remove('show');
          this.conductedsessionModal.nativeElement.style.display = 'none';

        }, (error) => {
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
    
    this.feedbackService.addFeedback(this.feebackForm.value, params).subscribe(
      (response) => {
        this.toastrService.success('Feedback added successfully', 'Success!', { timeOut: 8000 });
        
      this.getFeedback(this.sessionId);
      this.addfeedbackModal.nativeElement.classList.remove('show');
      this.addfeedbackModal.nativeElement.style.display='none';
      },
      (error) => {
        this.toastrService.error('Feedback not added', 'Failed!', { timeOut: 8000 });
      }
    );
  }

  closeFeedbackFormModal() {
    if (this.addfeedbackModal && this.addfeedbackModal.nativeElement) {
      this.addfeedbackModal.nativeElement.classList.remove('show');
      this.addfeedbackModal.nativeElement.style.display = 'none';
    }
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
  }


  removeFile(index: number) {
    this.files.splice(index, 1);
  }


  addLink() {
    //push object to array
    let linkObj = {
      link: this.addlink,
    };
    this.links.push(linkObj);
    this.addlink = '';
  }
  

  removeLink(index: number) {
    this.links.splice(index, 1);
  }

  addAttachment() {
    
    const params = {
      sessionId: this.sessionId,
    };    
    this.clientService.addAttachment(this.links,params).subscribe(
      (response) => {
        this.toastrService.success('Attachment added successfully', 'Success!', { timeOut: 8000 });
        this.getAttachment(this.sessionId);
        this.attachmentModal.nativeElement.classList.remove('show');
        this.attachmentModal.nativeElement.style.display = 'none';
      }
    );
  }

  deleteAttachment(id: any) {
    this.clientService.deleteAttachment(id).subscribe(
      (response) => {
        this.toastrService.success('Attachment deleted successfully', 'Success!', { timeOut: 8000 });
        this.getAttachment(this.sessionId);
      }
    );
  }
  
  toggleTab(tab: string): void {
    this.currentTab = tab;
  }

  getSession() {
    this.loading = true;
    this.clientService.getOneSession(this.sessionId).subscribe((res: any) => {
      this.sessions = res.body;
      this.loading = false;
    });
  }

  viewNotification(notification: any): void {
    this.notification = notification;
  }
  back() {
    window.history.back();
  }
  reload() {
    location.reload();
  }

}