import { HttpClient } from '@angular/common/http';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { faCaretDown, faPlus, faPenSquare, faChevronRight, faChevronLeft } from '@fortawesome/free-solid-svg-icons';
import { ToastrService } from 'ngx-toastr';
import { ApiService } from '../../services/ApiService';
import { ContractsService } from '../../services/contracts.service';
<<<<<<< HEAD
import { ClientService } from '../../services/ClientService';
import { NotificationsService } from '../../services/notifications.service';
import { error } from 'jquery';
=======
import { CoachService } from 'src/app/services/CoachService';
>>>>>>> 93909a3cd5a3b9fae945f015ffc98bb0832bcb81

@Component({
  selector: 'app-coach-view',
  templateUrl: './coach-view.component.html',
  styleUrls: ['./coach-view.component.css']
})
export class CoachViewComponent implements OnInit {

<<<<<<< HEAD
  addsessionForm!: FormGroup;
  payment: any;
  addSessionForm: any = {

  };
  coachSessionData: any;
  coachData: any;
  coachId: any;
=======
  Coaches!: [];
 
  organizationSessionData: any;
  organizationData: any;
>>>>>>> 93909a3cd5a3b9fae945f015ffc98bb0832bcb81
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
<<<<<<< HEAD
  notificationType = ['sms', 'email'];
  loadingClient = false;
  page: number = 1;
=======


 
  loadingCoach = false;
  totalLength: any;
>>>>>>> 93909a3cd5a3b9fae945f015ffc98bb0832bcb81
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
  loading = true;
  refreshIcon!: IconProp;
  selectedContract: any;
  sessionId: any;
<<<<<<< HEAD
  pageSize: number = 15;
  totalElements: any;
  clientToBeUpdated!: any;
=======
  coachToBeUpdated!: any;
>>>>>>> 93909a3cd5a3b9fae945f015ffc98bb0832bcb81
  sessionModal: any;
  contractId: any;
  contract: any;
  contractForm!: FormGroup;
<<<<<<< HEAD
=======
  numberOfCoachess!: number;
  numberOfSessions!: number;
  numberOfContracts!: number;
>>>>>>> 93909a3cd5a3b9fae945f015ffc98bb0832bcb81
  objectives: string[] = [];
  //Add Objective Form
  Objectives = {
    objective: ''
  };
<<<<<<< HEAD
  coachingCategory: any;
coach: any;
=======

  user: any;
  orgId: any;
  page: number = 1;
  pageSize: number = 15;
  totalElements: any;
>>>>>>> 93909a3cd5a3b9fae945f015ffc98bb0832bcb81
  constructor(
    private coachService: CoachService,
    private router: Router,
    private http: HttpClient,
    private formbuilder: FormBuilder,
    private toastrService: ToastrService,
    private route: ActivatedRoute,
    private apiService: ApiService,
    private contractsService: ContractsService,
    private activatedRoute: ActivatedRoute,
    private notificationsService: NotificationsService,
    ) { }

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
    
  

<<<<<<< HEAD
    this.coachSessionData = sessionStorage.getItem('user');
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);
    this.userRole = this.coachData.userRole;
    console.log(this.userRole);

    if (this.userRole == 'COACH') {
      this.sessionId = this.route.snapshot.params['sessionId'];
      console.log(this.sessionId);
      this.clientId = this.activatedRoute.snapshot.params['id'];
      this.getCoachData(this.coachId);
      this.id = this.clientId

      this.getContracts(this.id);
      this.contractForm = this.formbuilder.group(
        {
          coachingCategory: '',
          coachingTopic: '',
          clientId: '',
          startDate: '',
          endDate: '',
          groupFeesPerSession: '',
          individualFeesPerSession: '',
          noOfSessions: '',
          objectives: '',
        }
      );
      this.updateClient = this.formbuilder.group({
=======
 if (this.userRole == 'ORGANIZATION') {
   this.orgId = this.user.id;
 }
 this.getCoachData(this.coachId)
 if (this.userRole == 'ORGANIZATION') {
      this.updateCoach = this.formbuilder.group({
>>>>>>> 93909a3cd5a3b9fae945f015ffc98bb0832bcb81

        firstName: ' ',
        lastName: ' ',
        msisdn: ' ',
        email: ' ',
        status: ' ',
        physicalAddress: ' ',
        reason: '',

      });
<<<<<<< HEAD
      this.getClientSessions()
      this.getNotifications(this.page);
      this.getPaymentsByCoachId(this.page);
    }
    else if (this.userRole == 'ORGANIZATION') {
      this.clientId = this.activatedRoute.snapshot.params['id'];
      this.id = this.clientId
      this.coachId = this.coachData.id;
      this.getContracts(this.page);
      this.getCoachData(this.coachId);
      this.getClientSessions()
      this.getNotifications(this.page);
      this.getPaymentsByCoachId(this.page);
      this.contractForm = this.formbuilder.group(
        {
          coachingCategory: '',
          coachingTopic: '',
          clientId: '',
          startDate: '',
          endDate: '',
          groupFeesPerSession: '',
          individualFeesPerSession: '',
          noOfSessions: '',
          objectives: '',
        }
      );
      this.updateClient = this.formbuilder.group({
=======
      this.updateCoach = this.formbuilder.group({
>>>>>>> 93909a3cd5a3b9fae945f015ffc98bb0832bcb81

        firstName: ' ',
        lastName: ' ',
        msisdn: ' ',
        email: ' ',
        status: ' ',
       

      });
<<<<<<< HEAD
    }

    this.addsessionForm = this.formbuilder.group({
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

  getClientSessions() {
    console.log("client id", this.clientId)
    this.loading = true;
    this.ClientService.getClientSessions(this.clientId)
      .subscribe((data: any) => {
        this.sessions = data.body;
        this.totalElements = data.body.totalElements;
        this.loading = false;
        console.log("sessions gotten here", this.sessions)
      },
        (error: any) => {
          console.log(error);
          this.loading = false;
        }
      );
  }

  getContracts(page: any) {
    this.loading = true;
    this.page = page;
    //if page is 0, don't subtract 1
    if (page === 0 || page < 0) {
      page = 0;
    } else {
      page = page - 1;
    }

    const options: any = {
      page: page,
      size: this.pageSize,
      sessionStatus: this.filters.status,
      search: this.filters.searchItem,
      sort: 'id,desc',
    };

    if(this.userRole == 'COACH'){
      options.coachId = this.coachId;
    }else if(this.userRole == 'CLIENT'){
      options.clientId = this.clientId;
    }else if(this.userRole == 'ORGANIZATION'){
      //options.orgId = this.orgId;
      options.coachId = this.coachId;
    }

    this.contractsService.getContracts(options).subscribe(
      (res: any) => {
        console.log("res",res);
        this.contracts = res.body;
        this.totalElements = +res.headers.get('X-Total-Count');
        this.loading = false;
      }, (err: any) => {
        console.log(err);
        this.loading = false;
      }
    );
  }

  getNotifications(page: any) {
    this.loading = true;
    this.page = page;
    //if page is 0, don't subtract 1
    if (page === 0 || page < 0) {
      page = 0;
    } else {
      page = page - 1;
    }

    const options: any = {
      page: page,
      size: this.pageSize,
      sort: 'id,desc',
      coachId: this.coachId,
    };

    this.notificationsService.getNotifications(options).subscribe((response: any) => {
      this.notifications = response.body;
      this.totalElements = +response.headers.get('X-Total-Count');
      console.log('notification', this.notifications);
      this.loading= false;
    }, (error) => {
      console.log(error);
      this.loading = false;
    });
  }

  getPaymentsByCoachId(page: any){
    this.loading = true;
    this.page = page;

    if (page === 0 || page < 0) {
      page = 0;
    } else {
      page = page - 1;
    }
    const options = {
      page: page,
      per_page: this.itemsPerPage,
      coachId: this.coachId,  
    };

    this.ClientService.getPaymentsByCoachId(options).subscribe(
      (response) => {
          this.loading = false;
          this.payments = response.body.data;
          console.log('payments', this.payments);
        }, (error) => {
        console.log(error);
      }
    )
  }

  onContractChange(event: any) {
    console.log(event.target.value);
    this.createSessionClientId = event.target.value;

    //get client details from contract id
    this.selectedContract = this.contracts.find((contract: any) => contract.id == event.target.value);
    console.log(this.selectedContract);

  }
  addSession() {
    console.log('add button clicked here')
    console.log(this.addSessionForm);
    const session = this.addSessionForm;
    session.contractId = this.contractId;
    if (this.selectedContract && this.selectedContract.client) {
      this.addSessionForm.clientId = this.selectedContract.client.id;
    }
    if (typeof this.sessionType === "string") {
      let stringValue = this.sessionType;
      if (this.sessionType === 'INDIVIDUAL') {
        session.sessionAmount = this.contract.individualFeesPerSession;
      }
      else { session.sessionAmount = this.contract.groupFeesPerSession; }
    }
    console.log(this.addSessionForm);
    console.log('add button clicked here')
    const params = {
      clientId: this.selectedContract.client.id,

      contractId: this.createSessionClientId
    };

    console.log(params);

    this.ClientService.addSession(this.addSessionForm, params).subscribe((res: any) => {
      console.log(res);
      this.toastrService.success('Session added successfully');
      setTimeout(() => {
        location.reload();
      }, 1000);
      this.sessionModal.nativeElement.classList.remove('show');
      this.sessionModal.nativeElement.style.display = 'none';
    }, error => {
      console.log(error);
      this.toastrService.error(error.error, 'Maximum sessions reached contact coach');
      this.sessionModal.nativeElement.classList.remove('show');
      this.sessionModal.nativeElement.style.display = 'none';

    });

  }

  navigateToSessionView(id: any) {
    console.log(id);
    this.router.navigate(['sessionView', id]);
  }
  navigateToTerms(id: any) {
    console.log("contractId on navigate", id);
    this.contractId = id;
    if (this.userRole == 'COACH') {

      this.router.navigate(['/contractDetail', id]);
    } else if (this.userRole == 'CLIENT') {
      this.router.navigate(['/terms', id]);
    }


=======
     
    }

>>>>>>> 93909a3cd5a3b9fae945f015ffc98bb0832bcb81
  }
  toggleTab(tab: string): void {
    this.currentTab = tab;
  }
  
  getCoachData(id: any) {
    console.log("Get Coach");
    this.loadingCoach = true;

<<<<<<< HEAD
  getCoachData(id: any) {
    this.loading = true;
    console.log("Get Coach");

    this.ClientService.getOneClient(id).subscribe((data) => {
      this.coach = data.body;
      this.totalElements = +data.headers.get('X-Total-Count');
      console.log(this.coach);
      this.loading = false;
    },
      (error: any) => {
        console.log(error);
        this.loading = false;
=======
    this.coachService.getOneCoach(id).subscribe((data) => {
      this.loadingCoach = false;
      this.coaches = data.body;
      console.log(this.coaches);

    },
      (error: any) => {
        console.log(error);
        this.loadingCoach = false;

>>>>>>> 93909a3cd5a3b9fae945f015ffc98bb0832bcb81
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
  editClient(client: any) {
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
<<<<<<< HEAD


  // TODO: change to changeCoachStatus
  changeCoachStatus() {
    console.log(this.status);
    if (this.status === "ACTIVE") {
      this.ClientService.changeClient(this.clientId, "ACTIVE", this.statusForm.value).subscribe(
        (res) => {
          console.log(res);
          this.toastrService.success('Status Changed!', 'Success!', { timeOut: 8000 });
          setTimeout(() => {
            location.reload();
          }, 1000);
          this.activateclientModal.nativeElement.classList.remove('show');
          this.activateclientModal.nativeElement.style.display = 'none';

        }, (error) => {
          console.log(error)
          this.toastrService.success('Status not Changed!', 'Failed!', { timeOut: 8000 });
          this.activateclientModal.nativeElement.classList.remove('show');
          this.activateclientModal.nativeElement.style.display = 'none';
        }
      );
    }

    if (this.status === "SUSPENDED") {
      this.ClientService.changeClient(this.clientId, "SUSPENDED", this.statusForm.value).subscribe(
        (res) => {
          console.log(res);
          this.toastrService.success('Status Changed!', 'Success!', { timeOut: 8000 });
          setTimeout(() => {
            location.reload();
          }, 1000);
          this.suspendclientModal.nativeElement.classList.remove('show');
          this.suspendclientModal.nativeElement.style.display = 'none';
        }, (error) => {
          console.log(error)
          this.toastrService.success('Status not Changed!', 'Failed!', { timeOut: 8000 });
          this.suspendclientModal.nativeElement.classList.remove('show');
          this.suspendclientModal.nativeElement.style.display = 'none';
        }
      );
    }

    if (this.status === "CLOSED") {
      this.ClientService.changeClient(this.clientId, "CLOSED", this.statusForm.value).subscribe(
        (response) => {
          console.log(response);
          this.toastrService.success('Status Changed!', 'Success!', { timeOut: 8000 });
          setTimeout(() => {
            location.reload();
          }, 1000);
          this.closeclientModal.nativeElement.classList.remove('show');
          this.closeclientModal.nativeElement.style.display = 'none';
        }, (error: any) => {
          console.log(error)
          this.toastrService.success('Status not Changed!', 'Failed!', { timeOut: 8000 });
          this.closeclientModal.nativeElement.classList.remove('show');
          this.closeclientModal.nativeElement.style.display = 'none';
        }
      );
    }

  }


  viewNotification(notification: any): void {
    this.notification = notification;
    console.log(this.notification);
  }
  goToItem(type: any, entityObj: any): void {
    this.router.navigate([type, entityObj.id]);
  }
  viewPayment(payment: any): void {
    this.payment = payment.value;
    this.paymentId = this.payment.id;
  }

  addContract() {
    console.log('here');
    console.log(this.contractForm.value);
    var data = this.contractForm.value;
    data.coachId = this.coachData.id;
    data.objectives = this.objectives;
    data.clientId = this.clientId;
    console.log(data);
    this.contractsService.addNewContract(data).subscribe(
      (response: any) => {
        this.toastrService.success('Contract added!', 'Success!', { timeOut: 8000 });
        setTimeout(() => {
          location.reload();
        }, 5000);
        this.addContractModal.nativeElement.classList.remove('show');
        this.addContractModal.nativeElement.style.display = 'none';
      }, (error: any) => {
        console.log(error);
        this.toastrService.error('Contract not added!', 'Error!', { timeOut: 8000 });
        setTimeout(() => {
          location.reload();
        }, 5000);
        this.addContractModal.nativeElement.classList.remove('show');
        this.addContractModal.nativeElement.style.display = 'none';

      }
    )
  }
  addObjective() {

    console.log(this.Objectives);
    this.objectives.push(this.Objectives.objective);
    console.log(this.objectives);
    this.Objectives.objective = '';
  }

  removeObjective(index: number) {
    this.objectives.splice(index, 1);
  }
=======
>>>>>>> 93909a3cd5a3b9fae945f015ffc98bb0832bcb81
  back() {
    window.history.back();
  }

}
