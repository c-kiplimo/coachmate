
import {
  Component,
  ElementRef,
  HostListener,
  OnInit,
  ViewChild,
} from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { ClientService } from '../../services/ClientService';
import { ApiService } from '../../services/ApiService'; 
import {SessionsService }  from '../../services/SessionsService';
import { style, animate, transition, trigger } from '@angular/animations';
import { FormBuilder, FormGroup } from '@angular/forms';
import { fromEvent, map, debounceTime, distinctUntilChanged } from 'rxjs';
import { th } from 'date-fns/locale';
import { ContractsService } from 'src/app/services/contracts.service';
import { options } from '@mobiscroll/angular';



@Component({
  selector: 'app-contract-details',
  templateUrl: './contract-details.component.html',
  styleUrls: ['./contract-details.component.css'],
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
export class ContractDetailsComponent implements OnInit {
  addsessionForm!:FormGroup;
  addClient!: FormGroup;
  activateCoachForm!: FormGroup;
  addSessionForm:any={

  }; 
  loading = true;
  itemsPerPage = 20;
  page: number = 0;
  filters: any = {
    status: '',
    searchItem: '',
  };
  firstName: any;
  lastName: any;
  user: any;
  newOrderMessage: any;
  addclientForm!: FormGroup;
  clientId: any;
  client: any;
  searchTerm = '';
  eventType = '';
  addNewClient:any;
  sessionDate = '';
  sessionStartTime = '';
  sessionDuration = '';
  searching = false;
  open = false;
  showHideMessage = true;
  sessionType = [
    
  ];

  contracts: any;
  coachSlots: any;
  createSessionClientId: any;
  selectedContract: any;
  statusForm!: FormGroup;
  contractToBeUpdated: any;
  updateContract!: FormGroup;


  @ViewChild('yourElement') yourElement!: ElementRef;
  createdclient: any;
  clients: any;
  numberOfClients!: number; 
  coachSessionData: any;
  coachData: any;
  coachId: any;
  sessionToBeUpdated: any;
  updateSession: any;
  userDetails: any;
  sessions: any;
  contractId: any;
  contract: any;

  @HostListener('document:click', ['$event']) onClick(event: any) {
  console.log(event.target.attributes.id.nodeValue);

    if (event.target.attributes && event.target.attributes.id) {
      if (event.target.attributes.id.nodeValue === 'open') {
        this.open = true;
      }
    } else {
      this.open = false;
    }
  }
    constructor(
    private apiService: ApiService,
    private http: HttpClient,
    private clientService : ClientService,
    private formbuilder: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private sessionService: ClientService,
    private toastrService: ToastrService,
    private contractService: ContractsService,
   
  ) {}
  ngOnInit(): void {
    this.route.params.subscribe((params: { [x: string]: any; }) => {
      const id = params['id'];
      this.contractId = id;
    this.contract = this.clientService.getContract(id).subscribe((data: any) => {
      this.contract = data.body;
      console.log(this.contract);
      const contractId = params['id'];
      console.log("contract id gottten", contractId);
      this.getSessionsBycontractId(contractId);
      this.getCoachSlots(this.page);
  }
  )},
    this.user = JSON.parse(sessionStorage.getItem('user') || '{}'));
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log("CoachData",this.coachData);
    this.coachId = this.coachData.id;
    this.updateSession = this.formbuilder.group({
      sessionDate: '',
      sessionStartTime: '',
      sessionDuration: '',
      name:'',
      sessionType:'',
      sessionDetails:'',
      sessionEndTime:'',
      sessionVenue:'',
      goals:'',
      
    });

    this.addsessionForm = this.formbuilder.group({
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

    this.updateContract = this.formbuilder.group({
      coachingTopic: '',
      coachingCategory: '',
      startDate:'',
      endDate:'',
      groupFeesPerSession:'',
      individualFeesPerSession:'',
      noOfSessions:'',
      objectives:'',
      services:'',
      practice:'',
      note:'',
      terms_and_conditions:'',
    });

    this.statusForm = this.formbuilder.group({
      narration: 'Test',
      isSendNotification: true
    });
    this.activateCoachForm = this.formbuilder.group({});  // TODO: Create the activateCoachForm 
  }
 
  onContractChange(event: any) {
    console.log(event.target.value);
    this.createSessionClientId = event.target.value;
  }

  @ViewChild('sessionModal', { static: false })
  sessionModal!: ElementRef;
  addSession () {
    this.loading = true;
    console.log(this.addSessionForm);
   console.log('add button clicked here')
   const session = this.addSessionForm;
   session.contractId = this.contractId;
   if (typeof this.sessionType === "string") {
    let stringValue = this.sessionType;
    if(this.sessionType === 'INDIVIDUAL') {
      session.sessionAmount = this.contract.individualFeesPerSession;
    }
    else
    {session.sessionAmount = this.contract.groupFeesPerSession;}
  }
   const params = {
      clientId: this.contract.client.id,
      
      contractId:this.contract.id
   };

   console.log(params);
 console.log("indivudual fee per person", session.sessionAmount)
    this.sessionService.addSession(this.addSessionForm, params).subscribe((res:any) => {
      console.log(res);
      this.toastrService.success('Session added!', 'Success!', { timeOut: 8000 });
      setTimeout(() => {
        location.reload();
      }, 5000);
      this.sessionModal.nativeElement.classList.remove('show');
      this.sessionModal.nativeElement.style.display = 'none';
      

      
    } , error => {
      console.log(error);
      this.toastrService.error(error.error, 'Error', { timeOut: 8000 });
      setTimeout(() => {
        location.reload();
      }, 5000);
      this.sessionModal.nativeElement.classList.remove('show');
      this.sessionModal.nativeElement.style.display = 'none';
      
    });
}

getCoachSlots(page: number) {
  const options = {
    page: page,
    size: this.itemsPerPage,
    coachId: this.coachId,
    sort: 'id,desc',
    status: false
  };
  this.apiService.getCoachSlots(options).subscribe({
    next: (response) => {
      this.coachSlots = response.body;
    }
  });
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

@ViewChild('modal', { static: false })
modal!: ElementRef;

@ViewChild('activateContractModal', { static: false })
activateContractModal!: ElementRef;
@ViewChild('editContractModal', { static: false })
editContractModal!: ElementRef;
@ViewChild('signContractModal', { static: false })
signContractModal!: ElementRef;
@ViewChild('finishContracttModal', { static: false })
finishContractModal!: ElementRef;

closeModal() {
  this.modal.nativeElement.style.display = 'none';
  document.body.classList.remove('modal-open');
}

  getSessionsBycontractId(contractId:any){
    console.log("contract id gottten", contractId);
    this.loading = true;
    this.clientService.getSessionsBycontractId(contractId).subscribe(
      (data: any) => {
        this.sessions = data.body;
        console.log(this.sessions);
        this.loading = false;
        console.log("sessions gotten here",this.sessions);
      },
      (error: any) => {
        console.log(error);
        this.loading = false;
      }
    );

  }
navigateToSessionView(id: any) {
      console.log(id);
      this.router.navigate(['sessionView', id]);
    }
editSession(client:any){
      this.updateSession = this.formbuilder.group({
        sessionDate:this.sessionToBeUpdated.sessionDate,
        sessionStartTime: this.sessionToBeUpdated.sessionStartTime,
        sessionDuration: this.sessionToBeUpdated.sessionDuration,
        name:this.sessionToBeUpdated.name,
        sessionType:this.sessionToBeUpdated.sessionType,
        sessionDetails:this.sessionToBeUpdated.sessionDetails,
        sessionEndTime:this.sessionToBeUpdated.sessionEndTime,
        sessionVenue:this.sessionToBeUpdated.sessionVenue,
        goals:this.sessionToBeUpdated.goals,
      });
    
    }

    selectedSessionSlot(slot: any) {
      console.log(slot);
      this.addSessionForm.sessionSchedules = slot;
    }

  deleteSession(id:any, userDetails: any) {
    this.clientService.deleteSession(id).subscribe(response => {
      console.log(response);
      console.log("user deleted")
    });
  }
   
    modalTitle = 'Add Session';
    sessionTime: any;
    sessionGoals: any;
    session: any;
    id:any;
    showStatus: any;
    status: any;

    editContract(contract: any) {}

    statusState(currentStatus: any) {
      console.log(currentStatus);
      if (currentStatus === 'SIGNED') {
        this.showStatus = "SIGNED";
        this.status = "SIGNED";
      } else if (currentStatus === 'ONGOING') {
        this.showStatus = "ONGOING";
        this.status = "ONGOING";
      } else if (currentStatus === 'CLOSED') {
        this.showStatus = "CLOSED";
        this.status = "CLOSED";
      }
    }

    changeContractStatus() {
      console.log(this.status);
      let data = {
        status: this.status,
      }
      console.log(data);
      if (this.status === "SIGNED") {
        this.contractService.changeContractStatus(this.contractId, data).subscribe(
          (response) => {
            console.log(response);
            this.toastrService.success('Contract Signed!', 'Success!', { timeOut: 8000 });
            setTimeout(() => {
              location.reload();
            }, 1000);
            this.signContractModal.nativeElement.classList.remove('show');
            this.signContractModal.nativeElement.style.display = 'none';
          }, (error) => {
            console.log(error)
            this.toastrService.success('Contract not Signed!', 'Failed!', { timeOut: 8000 });
            this.signContractModal.nativeElement.classList.remove('show');
            this.signContractModal.nativeElement.style.display = 'none';
          }
        );
      }

      if (this.status === "ONGOING") {
        this.contractService.changeContractStatus(this.contractId, data).subscribe(
          (response) => {
            console.log(response);
            this.toastrService.success('Contract Activated!', 'Success!', { timeOut: 8000 });
            setTimeout(() => {
              location.reload();
            }, 1000);
            this.signContractModal.nativeElement.classList.remove('show');
            this.signContractModal.nativeElement.style.display = 'none';
          }, (error) => {
            console.log(error)
            this.toastrService.success('Contract not Activated!', 'Failed!', { timeOut: 8000 });
            this.signContractModal.nativeElement.classList.remove('show');
            this.signContractModal.nativeElement.style.display = 'none';
          }
        );
      }

      if (this.status === "FINISHED") {
        this.contractService.changeContractStatus(this.contractId, data).subscribe(
          (response) => {
            console.log(response);
            this.toastrService.success('Contract Terminated!', 'Success!', { timeOut: 8000 });
            setTimeout(() => {
              location.reload();
            }, 1000);
            this.signContractModal.nativeElement.classList.remove('show');
            this.signContractModal.nativeElement.style.display = 'none';
          }, (error) => {
            console.log(error)
            this.toastrService.success('Contract not Terminated!', 'Failed!', { timeOut: 8000 });
            this.signContractModal.nativeElement.classList.remove('show');
            this.signContractModal.nativeElement.style.display = 'none';
          }
        );
      }
    }


  }
        


