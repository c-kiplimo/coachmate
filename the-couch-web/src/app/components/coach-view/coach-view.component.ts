import { HttpClient } from '@angular/common/http';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { faCaretDown, faPlus, faPenSquare, faChevronRight, faChevronLeft } from '@fortawesome/free-solid-svg-icons';
import { ToastrService } from 'ngx-toastr';
import { ApiService } from '../../services/ApiService';
import { ContractsService } from '../../services/contracts.service';
import { CoachService } from 'src/app/services/CoachService';

@Component({
  selector: 'app-coach-view',
  templateUrl: './coach-view.component.html',
  styleUrls: ['./coach-view.component.css']
})
export class CoachViewComponent implements OnInit {

  Coaches!: [];
 
  organizationSessionData: any;
  organizationData: any;
  userRole: any;
  alertIcon!: IconProp;
  userIcon!: IconProp;
  calendarIcon: IconProp = "function";
  clockIcon: IconProp = "function";
  contracts!: any[];
  suspendCoachForm!: FormGroup;
  closeCoachForm!: FormGroup;
  activateCoachForm!: FormGroup;
  caretDown = faCaretDown;
  addIcon = faPlus;
  editIcon = faPenSquare;
  rightIcon = faChevronRight;
  backIcon = faChevronLeft;
  coachId: any;
  searchTerm = '';
  showHideMessage = true;
  

  sessionType = [

  ];

  currentTab = 'sessions';
  searching = false;
  actions = ['Activate', 'Close', 'Suspend'];


 
  loadingCoach = false;
  totalLength: any;
  itemsPerPage: number = 20;
  open = false;
  id: any;
  sessions!: any[];
  session: any;
  coaches: any;
  console: any;
  filters: any = {
    status: '',
    searchItem: '',
  };
  status: any;
  statusForm!: FormGroup;
  updateCoach!: FormGroup;
  sessionDueDate: any;
  sessionStartTime: any;
  sessionDuration: any;
  loading: any;
  refreshIcon!: IconProp;
  selectedContract: any;
  sessionId: any;
  coachToBeUpdated!: any;
  sessionModal: any;
  contractId: any;
  contract: any;
  contractForm!: FormGroup;
  numberOfCoachess!: number;
  numberOfSessions!: number;
  numberOfContracts!: number;
  objectives: string[] = [];
  //Add Objective Form
  Objectives = {
    objective: ''
  };

  user: any;
  orgId: any;
  page: number = 1;
  pageSize: number = 15;
  totalElements: any;
  constructor(
    private coachService: CoachService,
    private router: Router,
    private http: HttpClient,
    private formbuilder: FormBuilder,
    private toastrService: ToastrService,
    private route: ActivatedRoute,
    private apiService: ApiService,
    private contractsService: ContractsService,
    private activatedRoute: ActivatedRoute) { }

  ngOnInit() {

    // this.statusForm = this.formbuilder.group({
    //   narration: 'Test',
    //   isSendNotification: true
    // });

    this.organizationSessionData = sessionStorage.getItem('user');
    this.user = JSON.parse(this.organizationSessionData);
   
    this.userRole = this.user.userRole;
    this.route.params.subscribe((params) => {
      this.coachId = params['id'];
      this.id = params['id'];
    });
    
  

 if (this.userRole == 'ORGANIZATION') {
   this.orgId = this.user.id;
 }
 this.getCoachData(this.coachId)
 if (this.userRole == 'ORGANIZATION') {
      this.updateCoach = this.formbuilder.group({

        firstName: ' ',
        lastName: ' ',
        msisdn: ' ',
        email: ' ',
        status: ' ',
        physicalAddress: ' ',
        reason: '',

      });
      this.updateCoach = this.formbuilder.group({

        firstName: ' ',
        lastName: ' ',
        msisdn: ' ',
        email: ' ',
        status: ' ',
       

      });
     
    }

  }
  toggleTab(tab: string): void {
    this.currentTab = tab;
  }
  
  getCoachData(id: any) {
    console.log("Get Coach");
    this.loadingCoach = true;

    this.coachService.getOneCoach(id).subscribe((data) => {
      this.loadingCoach = false;
      this.coaches = data.body;
      console.log(this.coaches);

    },
      (error: any) => {
        console.log(error);
        this.loadingCoach = false;

      });
    }
  changeCoachStatus() {
    console.log(this.status);
    if (this.status === "ACTIVE") {
      this.coachService.changeCoach(this.coachId, "ACTIVE", this.statusForm.value).subscribe(
        (res) => {
          console.log(res);
          this.toastrService.success('Status Changed!', 'Success!', { timeOut: 8000 });
          setTimeout(() => {
            location.reload();
          }, 1000);
          this.activatecoachModal.nativeElement.classList.remove('show');
          this.activatecoachModal.nativeElement.style.display = 'none';

        }, (error) => {
          console.log(error)
          this.toastrService.success('Status not Changed!', 'Failed!', { timeOut: 8000 });
          this.activatecoachModal.nativeElement.classList.remove('show');
          this.activatecoachModal.nativeElement.style.display = 'none';
        }
      );
    }

    if (this.status === "SUSPENDED") {
      this.coachService.changeCoach(this.coachId, "SUSPENDED", this.statusForm.value).subscribe(
        (res) => {
          console.log(res);
          this.toastrService.success('Status Changed!', 'Success!', { timeOut: 8000 });
          setTimeout(() => {
            location.reload();
          }, 1000);
          this.suspendcoachModal.nativeElement.classList.remove('show');
          this.suspendcoachModal.nativeElement.style.display = 'none';
        }, (error) => {
          console.log(error)
          this.toastrService.success('Status not Changed!', 'Failed!', { timeOut: 8000 });
          this.suspendcoachModal.nativeElement.classList.remove('show');
          this.suspendcoachModal.nativeElement.style.display = 'none';
        }
      );
    }

    if (this.status === "CLOSED") {
      this.coachService.changeCoach(this.coachId, "CLOSED", this.statusForm.value).subscribe(
        (response) => {
          console.log(response);
          this.toastrService.success('Status Changed!', 'Success!', { timeOut: 8000 });
          setTimeout(() => {
            location.reload();
          }, 1000);
          this.closecoachModal.nativeElement.classList.remove('show');
          this.closecoachModal.nativeElement.style.display = 'none';
        }, (error: any) => {
          console.log(error)
          this.toastrService.success('Status not Changed!', 'Failed!', { timeOut: 8000 });
          this.closecoachModal.nativeElement.classList.remove('show');
          this.closecoachModal.nativeElement.style.display = 'none';
        }
      );
    }

  }
 

  @ViewChild('modal', { static: false })
  modal!: ElementRef;
  closeModal() {
    this.modal.nativeElement.style.display = 'none';
    document.body.classList.remove('modal-open');
  }

  getClass(Coaches: any) {
    if (Coaches.status === 'SUSPENDED') {
      return 'badge-warning';
    } else if (Coaches.status === 'ACTIVE') {
      return 'badge-success';
    } else {
      return 'badge-danger';
    }
  }
  @ViewChild('editCoachModal', { static: false })
  editCoachModal!: ElementRef;
  @ViewChild('activatecoachModal', { static: false })
  activatecoachModal!: ElementRef;

  @ViewChild('suspendcoachModal', { static: false })
  suspendcoachModal!: ElementRef;
  @ViewChild('closecoachModal', { static: false })
  closecoachModal!: ElementRef;
  editCoach(client: any) {
    this.coachToBeUpdated = client;

    this.updateCoach = this.formbuilder.group({
      firstName: this.coachToBeUpdated.firstName,
      lastName: this.coachToBeUpdated.lastName,
      msisdn: this.coachToBeUpdated.msisdn,
      email: this.coachToBeUpdated.email,

      physicalAddress: this.coachToBeUpdated.physicalAddress,

    });

  }

  updateCoachDetails(id: any) {
    this.coachToBeUpdated = this.updateCoach.value;
    console.log(this.coachToBeUpdated)
    console.log(id)
    this.coachService.editCoach(this.coachToBeUpdated, id).subscribe(
      (data) => {
        console.log(data)
        this.toastrService.success('updated!', 'Success!', { timeOut: 8000 });
        setTimeout(() => {
          location.reload();
        }, 1000);
        this.editCoachModal.nativeElement.classList.remove('show');
        this.editCoachModal.nativeElement.style.display = 'none';

      }, (error) => {
        console.log(error)
        this.toastrService.error('Error!', 'Error!', { timeOut: 8000 });
        this.editCoachModal.nativeElement.classList.remove('show');
        this.editCoachModal.nativeElement.style.display = 'none';
      }
    );
  }

  showStatus: any;

  statusState(currentstatus: any) {
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
  back() {
    window.history.back();
  }

}
