import {
  Component,
  ElementRef,
  HostListener,
  OnInit,
  ViewChild,
} from '@angular/core';
// import { ToastrService } from 'ngx-toastr';
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


  deliveryOption = ['DELIVERED', 'PICKUP'];
  @ViewChild('yourElement') yourElement!: ElementRef;
  createdclient: any;
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
    private service: ApiService  ,
    private http: HttpClient,
    private ClientService: ClientService,
    private formbuilder: FormBuilder,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private sessionService: ClientService,
    // private toastrService: ToastrService
  ) {}
  ngOnInit(): void {
    this.user = JSON.parse(sessionStorage.getItem('user') || '{}');
    
    this.getContracts();

    this.addsessionForm = this.formbuilder.group({
   
    });
  }
 
  onContractChange(event: any) {
    console.log(event.target.value);
    this.createSessionClientId = event.target.value;

    //get client details from contract id
    this.selectedContract = this.contracts.find((contract: any) => contract.id == event.target.value);
    console.log(this.selectedContract);

  }

  addSession () {
    
   this.addSessionForm.clientId = this.selectedContract.client.id;
   console.log(this.addSessionForm);
   const params = {
      clientId: this.selectedContract.client.id,
      
      contractId: this.createSessionClientId
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






}

