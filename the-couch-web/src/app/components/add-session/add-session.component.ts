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
import { SessionsService } from '../../services/SessionsService';
import { ContractsService } from '../../services/contracts.service';
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

  firstName: any;
  lastName: any;
  user: any;
  addclientForm!: FormGroup;
  clientId: any;
  client: any;
  addNewClient: any;
  searching = false;
  open = false;
  showHideMessage = true;
  sessionType = [];

  coachSlots: any;
  slots: any;
  orgId!: number;
  coachId!: number;

  page: number = 0;
  pageSize: number = 15;
  totalElements: any;


  contracts: any;
  getContractId: any;
  selectedContract: any;

  formData = {
    sessionSchedules: {},
    sessionDuration: '',
    sessionType: '',
    sessionVenue: '',
    name: '',
    sessionDetails: '',
    attachments: '',
    notes: '',
    feedback: '',
    paymentCurrency: 'USD',
    amountPaid: '',
    sessionAmount: '',
    sessionBalance: '',
    sessionDate: '',
  };

  @ViewChild('yourElement') yourElement!: ElementRef;
  createdclient: any;
  itemsPerPage: any;
  filters: any;
  clients: any;
  numberOfClients!: number;
  coachSessionData: any;
  coachData: any;
  userRole: any;

  loading: boolean = false;

  // displayMonths = 2;
  // navigation = 'select';
  // showWeekNumbers = false;
  // outsideDays = 'visible';


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
    private apiService: ApiService,
    private http: HttpClient,
    private clientService: ClientService,
    private formbuilder: FormBuilder,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private sessionService: ClientService,
    private contractsService: ContractsService,
    private toastrService: ToastrService
  ) {

  }
  ngOnInit(): void {
    this.coachSessionData = sessionStorage.getItem('user');
    this.user = JSON.parse(this.coachSessionData);

    this.userRole = this.user.userRole;
    console.log(this.userRole);

    if (this.userRole == 'ORGANIZATION') {
      this.orgId = this.user.organization.id;
      this.getContracts(this.page);
      this.getClients();
    } else if (this.userRole == 'COACH') {
      this.coachId = this.user.id;
      this.getCoachSlots();
      this.getContracts(this.page);
      //this.getClients();
    } else if (this.userRole == 'CLIENT') {
      this.clientId = this.user.id;
      this.getContracts(this.page);
      this.getClients();
    }
  }

  onContractChange(event: any) {
    this.getContractId = event.target.value;
    //get client details from contract id
    this.selectedContract = this.contracts.find((contract: any) => contract.id == event.target.value);
    console.log(this.selectedContract);
    this.coachId = this.selectedContract.coachId;
    //get select contract coach slots
    this.getCoachSlots();
  }

  @ViewChild('modal', { static: false })
  modal!: ElementRef;
  closeModal() {
    this.modal.nativeElement.style.display = 'none';
    document.body.classList.remove('modal-open');
  }

  getClients() {
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters?.status,
      search: this.filters?.searchItem,
    };
    this.clientService.getClients(options).subscribe(
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

  addSession() {
    console.log(this.formData);

    const params = {
      clientId: this.selectedContract.clientId,
      contractId: this.getContractId,
    };

    this.sessionService.addSession(this.formData, params).subscribe((res: any) => {
      console.log(res);
      this.toastrService.success('Session added successfully');
      window.history.back();
    }, error => {
      console.log(error);
      this.toastrService.error(error.error.message);
    });
  }


  getContracts(page: number) {
    const options: any = {
      page: page,
      size: this.pageSize,
      sort: 'id,desc',
    };
    if (this.userRole == 'COACH') {
      options.coachId = this.coachId;
    } else if (this.userRole == 'CLIENT') {
      options.clientId = this.clientId;
    } else if (this.userRole == 'ORGANIZATION') {
      options.organisationId = this.orgId;
    }
    this.contractsService.getContracts(options).subscribe((res: any) => {
      console.log(res);
      this.contracts = res.body;
    }, (error: any) => {
      console.log(error);
    });
  }


  getCoachSlots() {
    this.loading = true;
    const options = {
      coachId: this.coachId,
    };
    this.apiService.getCoachSlots(options).subscribe({
      next: (response) => {
        this.coachSlots = response.body;
        this.slots = this.coachSlots;
        this.loading = false;
      }
    });
  }

  selectedSessionSlot(slot: any) {
    console.log(slot);
    slot.coach = {};
    slot.dayOfTheWeek.coach = {};
    this.formData.sessionSchedules = slot;
  }

  onDateSelect(): void {
    console.log('Selected date:', this.formData.sessionDate);
    //get day of the week from the selected date
    let dayOfTheWeek = new Date(this.formData.sessionDate).toLocaleDateString('en-US', { weekday: 'long' });
    console.log(dayOfTheWeek);

    //loop through the coach slots and find slots days of the week that match the selected date and push them to the slots array
    this.slots = [];
    this.coachSlots.forEach((slot: any) => {
      if (slot.dayOfTheWeek.day === dayOfTheWeek.toUpperCase()) {
        this.slots.push(slot);
      }
    }
    );
    console.log(this.slots);
  }

  formatTime(timeString: string): Date {
    const [hours, minutes, seconds] = timeString.split(':');
    const date = new Date();
    date.setHours(Number(hours));
    date.setMinutes(Number(minutes));
    date.setSeconds(Number(seconds));
    //allow return null date is empty or null
    return date;
  }

}

