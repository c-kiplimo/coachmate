
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
import { error } from 'jquery';
import {  ChangeDetectorRef } from '@angular/core';



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
  loading = false;
  pageSize = 20;
  page: number = 0;
  filters: any = {
    status: '',
    searchItem: '',
  };
  firstName: any;
  lastName: any;
  user: any;
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
  contractToBeUpdated: any;
  updateContractForm!: FormGroup;
  userRole: any;
  totalElements = 0;
  contractId: any;
  contract?: any;


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
    private cd: ChangeDetectorRef
   
  ) {}

  ngOnInit() {
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log("CoachData",this.coachData);
    this.coachId = this.coachData.id;

    this.route.params.subscribe((params: { [x: string]: any; }) => {
    const id = params['id'];
    this.contractId = id;
    this.userRole = this.coachData.userRole;
  });

  this.getContractData(this.contractId);

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

    this.updateContractForm = this.formbuilder.group({
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
      terms_and_conditions:'',
    });

  }

  getFeesPerSession(): number | null {
    if (this.contract?.coachingCategory === 'INDIVIDUAL') {
      return this.contract?.individualFeesPerSession;
    } else if (this.contract?.coachingCategory === 'GROUP') {
      return this.contract?.groupFeesPerSession;
    } else {
      return null;
    }
  }

  getContractData(id: any) {
    this.loading = true;
    this.clientService.getContract(id).subscribe((data: any) => {
      this.contract = data.body;
      this.getSessionsBycontractId(this.contractId);
      this.getCoachSlots(this.page);
      this.loading = false;
  }, (error) => {
    console.log(error);
    this.loading = false;
  }
  );
  }

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
  sessions?: any;
  modalTitle = 'Add Session';
  sessionTime: any;
  sessionGoals: any;
  session: any;

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
getSessionsBycontractId(contractId:any){
  console.log("contract id gottten", contractId);
  this.loading = true;
  this.clientService.getSessionsBycontractId(contractId).subscribe(
    (data: any) => {
      this.sessions = data.body;
      this.totalElements = +data.headers.get('X-Total-Count');
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
back() {
  window.history.back();
}

getCoachSlots(page: number) {
  const options = {
    page: page,
    size: this.pageSize,
    coachId: this.coachId,
    sort: 'id,desc',
    status: false
  };
  this.apiService.getCoachSlots(options).subscribe({
    next: (response) => {
      this.coachSlots = response.body.data;
    }
  });
}
calculateTotalFees() {
  if (this.contract && this.contract.coachingCategory) {
    if (this.contract.coachingCategory === 'INDIVIDUAL') {
      return this.contract.individualFeesPerSession * this.contract.noOfSessions;
    } else if (this.contract.coachingCategory === 'GROUP') {
      return this.contract.groupFeesPerSession * this.contract.noOfSessions;
    }
  }
  return ""; 
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

id:any;
showStatus: any;
status!: string;
contractUpdate: any;

editContract(contract: any) {
  this.contractToBeUpdated = contract;

  this.updateContractForm = this.formbuilder.group({
    coachingTopic: this.contractToBeUpdated.coachingTopic,
    coachingCategory: this.contractToBeUpdated.coachingCategory,
    startDate: this.contractToBeUpdated.startDate,
    endDate: this.contractToBeUpdated.endDate,
    groupFeesPerSession: this.contractToBeUpdated.groupFeesPerSession,
    individualFeesPerSession: this.contractToBeUpdated.individualFeesPerSession,
    noOfSessions: this.contractToBeUpdated.noOfSessions,
    objectives: this.contractToBeUpdated.objectives,
    services: this.contractToBeUpdated.services,
    practice: this.contractToBeUpdated.practice,
    terms_and_conditions: this.contractToBeUpdated.terms_and_conditions,
  });
  this.cd.detectChanges();
  this.calculateTotalFees();
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

navigateToSessionView(id: any) {
      console.log(id);
      this.router.navigate(['sessionView', id]);
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
   
    updateContract(id: any) {
      this.contractToBeUpdated = this.updateContractForm.value;
      console.log(this.contractToBeUpdated)
      this.contractService.editContract(id, this.contractToBeUpdated).subscribe(
        (response) => {
          console.log(response);
          this.toastrService.success("Contract updated", "success!", {timeOut: 8000});
          setTimeout(() => {
            location.reload();
          }, 1000);
          this.editContractModal.nativeElement.classList.remove('show');
          this.editContractModal.nativeElement.style.display('none');
        }, (error) => {
          console.log(error);
          this.toastrService.error('Error', 'Error!', {timeOut: 8000});
          this.editContractModal.nativeElement.classList.remove('show');
          this.editContractModal.nativeElement.style.display('none');
        }
      );
      this.cd.detectChanges();
      this.calculateTotalFees();
    }

    statusState(currentStatus: any) {
      console.log(currentStatus);
      if (currentStatus === 'SIGNED') {
        this.showStatus = "SIGNED";
        this.status = "SIGNED";
      } else if (currentStatus === 'ONGOING') {
        this.showStatus = "ONGOING";
        this.status = "ONGOING";
      } else if (currentStatus === 'FINISHED') {
        this.showStatus = "FINISHED";
        this.status = "FINISHED";
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
        


