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
import { ClientService } from '../services/ClientService';
import { ApiService } from '../services/ApiService'; 
import {SessionsService }  from '../services/SessionsService';
import { style, animate, transition, trigger } from '@angular/animations';
import { FormBuilder, FormGroup } from '@angular/forms';
import { fromEvent, map, debounceTime, distinctUntilChanged } from 'rxjs';

@Component({
  selector: 'app-add-session',
  templateUrl: './add-session.component.html',
  styleUrls: ['./add-session.component.css'],
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
export class AddSessionComponent implements OnInit {
  
  addClient!: FormGroup;
  addSessionForm:any={
    sessionType:"INDIVIDUAL",
    sessionVenue:"VIRTUAL",
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

  coachSlots: any;


  contracts: any;
  createSessionClientId: any;
  selectedContract: any;


  formData = {
    sessionSchedules: {},
    sessionDuration: '',
    sessionType: '',
    sessionVenue: '',
    name:'',
    sessionDetails:'',
    attachments:'',
    notes:'',
    feedback:'',
    paymentCurrency:'USD',
    amountPaid:'',
    sessionAmount:'',
    sessionBalance:'',

  };

  

  @ViewChild('yourElement') yourElement!: ElementRef;
  createdclient: any;
  itemsPerPage: any;
  filters: any;
  clients: any;
  numberOfClients!: number; 
  coachSessionData: any;
  coachData: any;
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
    private apiService:ApiService,
    private http: HttpClient,
    private clientService : ClientService,
    private formbuilder: FormBuilder,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private sessionService: ClientService,
    private toastrService: ToastrService
  ) {
    
  }
  ngOnInit(): void {
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);
    
    this.getCoachSlots();
    this.getContracts();
    //this.user = JSON.parse(sessionStorage.getItem('user') as any);
    this.getClients();

  }
 
  onContractChange(event: any) {
    console.log(event.target.value);
    this.createSessionClientId = event.target.value;

    //get client details from contract id
    this.selectedContract = this.contracts.find((contract: any) => contract.id == event.target.value);
    console.log(this.selectedContract);

  }
  @ViewChild('modal', { static: false })
modal!: ElementRef;
closeModal() {
  this.modal.nativeElement.style.display = 'none';
  document.body.classList.remove('modal-open');
}
  
  getClients(){
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
    };
    this.clientService.getClient(options).subscribe(
      (response: any) => {
        console.log(response.body.data);
        console.log("here");
        this.clients = response.body.data;
        this.numberOfClients = this.clients.length;
        console.log(this.numberOfClients)
      }, (error: any) => {
        console.log(error)
      }
    )
  }

  addSession () {
    console.log('add button clicked here')
    console.log(this.formData);
    if (this.selectedContract && this.selectedContract.client) {
      this.addSessionForm.clientId = this.selectedContract.client.id;
    }
   console.log(this.formData);
   console.log('add button clicked here')
   const params = {
      clientId: this.selectedContract.client.id,
      
      contractId: this.createSessionClientId,
      sessionScheduleId: this.formData.sessionSchedules,
   };

   console.log(params);
 
   this.sessionService.addSession(this.formData, params).subscribe((res:any) => {
    console.log(res);
    this.toastrService.success('Session added successfully');
    this.router.navigate(['/sessions']);
  }, error => {
    console.log(error);
    this.toastrService.error(error.error, 'Maximum sessions reached contact coach');
    this.router.navigate(['/sessions']);
  });
  
  }


  getContracts() {
    this.sessionService.getContracts().subscribe((res:any) => {
      console.log(res);
      this.contracts = res; });
  }


  getCoachSlots() {
    const coachId = this.coachData.coach.id;
    this.apiService.getCoachSlots(coachId).subscribe({
      next: (response) => {
        this.coachSlots = response.body;
      }
    });
  }

  selectedSessionSlot(slot: any) {
    console.log(slot);
    this.formData.sessionSchedules = slot;
  }

}

