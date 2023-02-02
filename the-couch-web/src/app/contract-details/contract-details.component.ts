
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
  addSessionForm:any={

  }; 
  loading = true;
  itemsPerPage = 20;
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
  createSessionClientId: any;
  selectedContract: any;


  @ViewChild('yourElement') yourElement!: ElementRef;
  createdclient: any;
  clients: any;
  numberOfClients!: number; 
  coachSessionData: any;
  coachData: any;
  sessionToBeUpdated: any;
  updateSession: any;
userDetails: any;
  sessions: any;
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
    private apiService:ClientService,
    private http: HttpClient,
    private clientService : ClientService,
    private formbuilder: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private sessionService: ClientService,
    private toastrService: ToastrService
  ) {}
  ngOnInit(): void {
    this.route.params.subscribe((params: { [x: string]: any; }) => {
      const id = params['id'];
    this.contract = this.clientService.getContract(id).subscribe((data: any) => {
      this.contract = data.body;
      console.log(this.contract);
      const contractId = params['id'];
      console.log("contract id gottten", contractId);
      this.getSessionsBycontractId(contractId)

  }
  )},
    this.user = JSON.parse(sessionStorage.getItem('user') || '{}'));
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log("CoachData",this.coachData);
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
      addSessionForm: {
        sessionType: "INDIVIDUAL",
        sessionVenue: "VIRTUAL",
      } 
    });
  }
 
  onContractChange(event: any) {
    console.log(event.target.value);
    this.createSessionClientId = event.target.value;
  }
  addSession () {
   this.addSessionForm.clientId = this.selectedContract.client.id;
   console.log(this.addSessionForm);
   const params = {
      clientId: this.selectedContract.client.id,
      
      contractId: this.addSessionForm.contractId
   };

   console.log(params);
 
    this.sessionService.addSession(this.addSessionForm, params).subscribe((res:any) => {
      console.log(res);
      this.router.navigate(['/sessions']);
    });
  }
  getSessionsBycontractId(contractId:any){
    console.log("contract id gottten", contractId);
    this.loading = true;
    this.clientService.getSessionsBycontractId(contractId).subscribe(
      (data: any) => {
        this.sessions = data.body;
        console.log(this.sessions);
        this.loading = false;
        console.log("sessions gotten here",this.sessions)
      },
      (error: any) => {
        console.log(error);
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
  deleteSession(id: number, userDetails: any) {
    this.clientService.deleteSession(id, userDetails).subscribe(response => {
      console.log(response);
    });
  }
   
    modalTitle: any;
    sessionTime: any;
    sessionGoals: any;
    session: any;
    id:any;
    contract:any;
    contractId!: number;
    closeModal() {
    throw new Error('Method not implemented.');
    }
    
    
      }
    
    
