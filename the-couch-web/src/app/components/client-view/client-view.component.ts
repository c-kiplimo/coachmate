import { Component, ElementRef, HostListener, OnInit, ViewChild } from '@angular/core';
import { ClientService } from '../../services/ClientService';
import { SessionsService } from '../../services/SessionsService';
import { NotificationsService } from '../../services/notifications.service';
import { style, animate, transition, trigger } from '@angular/animations';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import {
  faBell,
  faCaretDown,
  faChevronLeft,
  faChevronRight,
  faPenSquare,
  faPlus,
  faRedo,
  // faRefresh,
} from '@fortawesome/free-solid-svg-icons';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup } from '@angular/forms';
import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { ApiService } from '../../services/ApiService';
import { ContractsService } from 'src/app/services/contracts.service';

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

  Clients!: [];
  addsessionForm!: FormGroup;
  payment: any;
  addSessionForm: any = {

  };
  editedClient: any;
  coachSessionData: any;
  coachData: any;
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

  sessionType = [ ];

  currentTab = 'sessions';
  searching = false;
  actions = ['Activate', 'Close', 'Suspend'];

  notificationOptions = [false, true];
  notificationType = ['sms', 'email'];
  loadingClient = false;
  totalLength: any;
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
  updateClient!: FormGroup;
  sessionDueDate: any;
  sessionStartTime: any;
  sessionDuration: any;
  loading: any;
  refreshIcon!: IconProp;
  createSessionClientId: any;
  selectedContract: any;
  sessionId: any;
  clientToBeUpdated!: any;
  sessionModal: any;
  contractId: any;
  contract: any;
  contractForm!: FormGroup;
  numberOfClients!: number;
  numberOfSessions!: number;
  numberOfContracts!: number;
  objectives: string[] = [];
  //Add Objective Form
  Objectives = {
    objective: ''
  };
  coachingCategory: any;
  user: any;
  coachId: any;
  orgId: any;
  page: number = 0;
  pageSize: number = 15;
  totalElements: any;

  constructor(
    private ClientService: ClientService,
    private notificationsService: NotificationsService,
    private router: Router,
    private http: HttpClient,
    private formbuilder: FormBuilder,
    private toastrService: ToastrService,
    private route: ActivatedRoute,
    private apiService: ApiService,
    private contractsService: ContractsService,
    private sessionService: SessionsService,
    private activatedRoute: ActivatedRoute) { }

  ngOnInit() {

    this.statusForm = this.formbuilder.group({
      narration: 'Test',
      isSendNotification: true
    });

    this.coachSessionData = sessionStorage.getItem('user');
    this.user = JSON.parse(this.coachSessionData);

    this.userRole = this.user.userRole;
    this.route.params.subscribe((params) => {
      this.clientId = params['id'];
    });

    if (this.userRole == 'COACH') {
      this.coachId = this.user.id;
      
    } else if (this.userRole == 'ORGANIZATION') {
      this.orgId = this.user.organization.id;
    } else if (this.userRole == 'CLIENT') {
      this.clientId = this.user.id;
      this.coachId = this.user.addedBy.id;
    }
    this.getClientData(this.clientId);

    if (this.userRole == 'COACH') {
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
  getAllSessions(page: any) {
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
      // coachId: this.coachId,
      clientId: this.clientId,
      //contractId: this.coachData.contractId,
      //sessionDate: this.filters.sessionDate,
    };

    this.sessionService.getSessions(options).subscribe(
      (response: any) => {
        this.sessions = response.body;
        this.totalElements = +response.headers.get('X-Total-Count');
        this.loading = false;
      },
      (error: any) => {
        console.log(error);
        this.loading = false;
      }
    );
  }
  getNotifications(page: any) {
    const options = {
      page: page,
      size: this.pageSize,
      sort: 'id,desc',
      clientId: this.clientId,
    };

    this.loading = true;
    this.notificationsService.getNotifications(options).subscribe((res: any) => {
      this.notifications = res.body;
      console.log('notification ni', this.notifications);
      this.searching = false;
    });
  }
  getPaymentsByClientId(id: any) {
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
      client_id: id,
    };

    this.loading = true;
    this.ClientService.getPaymentsByClientId(options).subscribe(
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


  }
  toggleTab(tab: string): void {
    this.currentTab = tab;
  }

  getClientData(id: any) {
    console.log("Get Client");
    this.loadingClient = true;

    this.ClientService.getOneClient(id).subscribe((data) => {
      this.loadingClient = false;
      this.clients = data.body;
      console.log(this.clients);
      this.getAllSessions(this.page = 0);
      this.getAllContracts(this.page = 0);
      this.getNotifications(this.page = 0);
      this.getPaymentsByClientId(id);
    },
      (error: any) => {
        console.log(error);
        this.loadingClient = false;

      });
  }

  getAllContracts(page: any) {

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
      clientId: this.clientId,
      
    };

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
  @ViewChild('editClientModal', { static: false })
  editClientModal!: ElementRef;
  @ViewChild('activateclientModal', { static: false })
  activateclientModal!: ElementRef;
  @ViewChild('addContractModal', { static: false })
  addContractModal!: ElementRef;
  @ViewChild('suspendclientModal', { static: false })
  suspendclientModal!: ElementRef;
  @ViewChild('closeclientModal', { static: false })
  closeclientModal!: ElementRef;
  editClient(client: any) {
    this.clientToBeUpdated = client;

    this.updateClient = this.formbuilder.group({
      firstName: this.clientToBeUpdated.firstName,
      lastName: this.clientToBeUpdated.lastName,
      clientType: this.clientToBeUpdated.clientType,
      msisdn: this.clientToBeUpdated.msisdn,
      email: this.clientToBeUpdated.email,

      physicalAddress: this.clientToBeUpdated.physicalAddress,

      profession: this.clientToBeUpdated.profession,
      paymentMode: this.clientToBeUpdated.paymentMode,
      reason: this.clientToBeUpdated.reason,
    });

  }

  updateClientDetails(id: any) {
    this.clientToBeUpdated = this.updateClient.value;
    console.log(this.clientToBeUpdated)
    console.log(id)
    this.ClientService.editClient(this.clientToBeUpdated, id).subscribe(
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
  changeClientStatus() {
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

  getContracts(id: any) {
    this.contracts = [];
    this.ClientService.getContractsByClientId(id).subscribe((res: any) => {
      console.log("client sessions here", res);
      this.contracts = res.body;
    });
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


  // Get Notification for specific customer




