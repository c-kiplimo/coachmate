
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
    sessionType:"INDIVIDUAL",
    sessionVenue:"VIRTUAL",
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
  itemsPerPage: any;
  filters: any;
  clients: any;
  numberOfClients!: number; 
  coachSessionData: any;
  coachData: any;
loading: any;
  sessionToBeUpdated: any;
  updateSession: any;
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
    private apiService:ApiService,
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
      console.log("contract details");
  }
  )},
    this.user = JSON.parse(sessionStorage.getItem('user') || '{}'));
    this.getClients();
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);
    
    this.getContracts();

    this.addsessionForm = this.formbuilder.group({
   
    });
    this.getClients();

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

    //get client details from contract id
    this.selectedContract = this.contracts.find((contract: any) => contract.id == event.target.value);
    console.log(this.selectedContract);

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

  getContracts() {
    this.sessionService.getContracts().subscribe((res:any) => {
      console.log(res);
      this.contracts = res;    });
  }

    navigateToSessionView(id: any) {
      console.log(id);
      this.router.navigate(['sessionView', id]);
    }
    sessions: any;
  
    editSession(client:any){
      this.sessionToBeUpdated = client;
  
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
    
    
