import { HttpClient } from '@angular/common/http';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { faCaretDown, faPlus, faPenSquare, faChevronRight, faChevronLeft } from '@fortawesome/free-solid-svg-icons';
import { ToastrService } from 'ngx-toastr';
import { ApiService } from '../../services/ApiService';
import { ContractsService } from '../../services/contracts.service';
import { ClientService } from '../../services/ClientService';
import { NotificationsService } from '../../services/notifications.service';
import { error } from 'jquery';

@Component({
  selector: 'app-coach-view',
  templateUrl: './coach-view.component.html',
  styleUrls: ['./coach-view.component.css']
})
export class CoachViewComponent implements OnInit {

  addsessionForm!: FormGroup;
  payment: any;
  addSessionForm: any = {

  };
  coachSessionData: any;
  coachData: any;
  coachId: any;
  userRole: any;
  alertIcon!: IconProp;
  userIcon!: IconProp;
  calendarIcon: IconProp = "function";
  clockIcon: IconProp = "function";
  contracts!: any[];
  suspendClientForm!: FormGroup;
  closeClientForm!: FormGroup;
  activateClientForm!: FormGroup;
  editPaymentForm!: FormGroup;
  caretDown = faCaretDown;
  addIcon = faPlus;
  editIcon = faPenSquare;
  rightIcon = faChevronRight;
  backIcon = faChevronLeft;
  clientId: any;
  orderId: any;
  paymentId: any;
  searchTerm = '';
  orders!: any;
  showHideMessage = true;
  payments!: any[];
  notifications!: any[];
  notification!: any;

  sessionType = [

  ];

  currentTab = 'sessions';
  searching = false;
  actions = ['Activate', 'Close', 'Suspend'];
  notificationType = ['sms', 'email'];
  loadingClient = false;
  page: number = 1;
  itemsPerPage: number = 20;
  open = false;
  id: any;
  sessions!: any[];
  session: any;
  clients: any;
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
  createSessionClientId: any;
  selectedContract: any;
  sessionId: any;
  pageSize: number = 15;
  totalElements: any;
  coachToBeUpdated!: any;
  sessionModal: any;
  contractId: any;
  contract: any;
  contractForm!: FormGroup;
  objectives: string[] = [];
  //Add Objective Form
  Objectives = {
    objective: ''
  };
  coachingCategory: any;
coach: any;
  addContractModal: any;
  closeclientModal: any;
  activateclientModal: any;
  suspendclientModal: any;
  editClientModal: any;
  // coachToBeUpdated: any;
  constructor(
    private ClientService: ClientService,
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

    this.statusForm = this.formbuilder.group({
      narration: 'Test',
      isSendNotification: true
    });

    this.coachSessionData = sessionStorage.getItem('user');
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);
    this.userRole = this.coachData.userRole;
    console.log(this.userRole);
    this.route.params.subscribe((params) => {
      this.coachId = params['id'];
    });

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
      this.updateCoach = this.formbuilder.group({

        firstName: ' ',
        lastName: ' ',
        clientType: ' ',
        msisdn: ' ',
        email: ' ',
        physicalAddress: ' ',
        profession: ' ',
        paymentMode: ' ',
        reason: '',

      });
      this.getCoachSessions(this.page);
      this.getNotifications(this.page);
      this.getPaymentsByCoachId();
    }
    else if (this.userRole == 'ORGANIZATION') {
      this.clientId = this.activatedRoute.snapshot.params['id'];
      this.id = this.clientId
      this.getContracts(this.page);
      this.getCoachData(this.coachId);
      this.getCoachSessions(this.page);
      this.getNotifications(this.page);
      this.getPaymentsByCoachId();
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
      this.updateCoach = this.formbuilder.group({

        firstName: ' ',
        lastName: ' ',
        clientType: ' ',
        msisdn: ' ',
        email: ' ',
        physicalAddress: ' ',
        profession: ' ',
        paymentMode: ' ',
        reason: '',

      });
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

  getCoachSessions(page: any) {
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
    }else if(this.userRole == 'ORGANIZATION'){
      options.coachId = this.coachId;
    }

    this.ClientService.getSessions(options).subscribe(
      (response) => {
        this.sessions = response.body;
        this.totalElements = +response.headers.get('X-Total-Count');
        console.log('sessions got', this.sessions);
        this.loading = false;
      }, (error) => {
        console.log('error');
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

  getPaymentsByCoachId(){
    this.loading = true;

    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      coachId: this.coachId,  
    };

    this.ClientService.getPaymentsByCoachId(options).subscribe(
      (response) => {
          this.payments = response.body.data;
          console.log('payments', this.payments);
          this.loading = false;
        }, (error) => {
        console.log(error);
        this.loading = false;
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


  }
  toggleTab(tab: string): void {
    this.currentTab = tab;
  }

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
      });
  }

  @ViewChild('modal', { static: false })
  modal!: ElementRef;
  closeModal() {
    this.modal.nativeElement.style.display = 'none';
    document.body.classList.remove('modal-open');
  }

  getClass(Clients: any) {
    if (Clients.status === 'SUSPENDED') {
      return 'badge-warning';
    } else if (Clients.status === 'ACTIVE') {
      return 'badge-success';
    } else {
      return 'badge-danger';
    }
  }
  // @ViewChild('editClientModal', { static: false })
  // editClientModal!: ElementRef;
  // @ViewChild('activateclientModal', { static: false })
  // activateclientModal!: ElementRef;
  // @ViewChild('addContractModal', { static: false })
  // addContractModal!: ElementRef;
  // @ViewChild('suspendclientModal', { static: false })
  // suspendclientModal!: ElementRef;
  // @ViewChild('closeclientModal', { static: false })
  // closeclientModal!: ElementRef;

  editCoach(coach: any) {
    this.coachToBeUpdated = coach;

    this.updateCoach = this.formbuilder.group({
      firstName: this.coachToBeUpdated.firstName,
      lastName: this.coachToBeUpdated.lastName,
      msisdn: this.coachToBeUpdated.msisdn,
      email: this.coachToBeUpdated.email,
      physicalAddress: this.coachToBeUpdated.physicalAddress,
      profession: this.coachToBeUpdated.profession,
      reason: this.coachToBeUpdated.reason,
    });
  }

  updateCoachDetails(id: any) {
    this.coachToBeUpdated = this.updateCoach.value;
    console.log(this.coachToBeUpdated)
    console.log(id)
    this.ClientService.editClient(this.coachToBeUpdated, id).subscribe(
      (data) => {
        console.log(data)
        this.toastrService.success('updated!', 'Success!', { timeOut: 8000 });
        setTimeout(() => {
          location.reload();
        }, 1000);
        this.editClientModal.nativeElement.classList.remove('show');
        this.editClientModal.nativeElement.style.display = 'none';

      }, (error) => {
        console.log(error)
        this.toastrService.error('Error!', 'Error!', { timeOut: 8000 });
        this.editClientModal.nativeElement.classList.remove('show');
        this.editClientModal.nativeElement.style.display = 'none';
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


  // TODO: change to changeCoachStatus
  changeCoachStatus() {
    console.log(this.status);
    if (this.status === "ACTIVE") {
      this.ClientService.changeClient(this.coachId, "ACTIVE", this.statusForm.value).subscribe(
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
      this.ClientService.changeClient(this.coachId, "SUSPENDED", this.statusForm.value).subscribe(
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
      this.ClientService.changeClient(this.coachId, "CLOSED", this.statusForm.value).subscribe(
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
  back() {
    window.history.back();
  }

}
