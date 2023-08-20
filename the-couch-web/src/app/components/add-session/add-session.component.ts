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

  currentDate: Date = new Date();
  daysOfWeek: string[] = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
  weeks: any[] = []; // This will hold the weeks and their dates

  // Dummy data representing booked sessions
  bookedSessions: any[] = [];


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
    //console.log(this.userRole);

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
  
  generateCalendar() {
    this.weeks = []; // Reset weeks
    const firstDayOfMonth = new Date(this.currentDate.getFullYear(), this.currentDate.getMonth(), 1);
    const lastDayOfMonth = new Date(this.currentDate.getFullYear(), this.currentDate.getMonth() + 1, 0);
    const daysInMonth = lastDayOfMonth.getDate();
    const firstDayOfWeek = firstDayOfMonth.getDay() === 0 ? 6 : firstDayOfMonth.getDay() - 1;
    
    let dateCounter = 1; // Start from 1 (first day of the month)
    for (let week = 0; week < 6; week++) {
      const weekDates: any[] = [];
      for (let dayOfWeek = 0; dayOfWeek < 7; dayOfWeek++) {
        if ((week === 0 && dayOfWeek < firstDayOfWeek) || dateCounter > daysInMonth) {
          weekDates.push(null);
        } else {
          const date = new Date(this.currentDate.getFullYear(), this.currentDate.getMonth(), dateCounter);
          date.setUTCHours(0, 0, 0, 0); // Set time to midnight in UTC
          weekDates.push({
            date: date.toISOString().split('T')[0], // Convert date to string in yyyy-mm-dd format
            day: dateCounter,
            bookedCount: this.getBookedSessionCount(date) // Corrected to use the current date
          });
          dateCounter++;
        }
      }
      this.weeks.push(weekDates);
    }
  }
  
  getBookedSessionCount(date: Date): number {
    const dateString = date.toISOString().split('T')[0];    // Convert date to string in yyyy-mm-dd format
    let session = this.bookedSessions.find(session => session.date === dateString);
    return session ? session.count : 0;
  }

  prevMonth() {
    const prevMonthDate = new Date(this.currentDate.getFullYear(), this.currentDate.getMonth() - 1, 1);
    this.currentDate = prevMonthDate;
    this.generateCalendar();
  }
  nextMonth() {
    const nextMonthDate = new Date(this.currentDate.getFullYear(), this.currentDate.getMonth() + 1, 1);
    this.currentDate = nextMonthDate;
    this.generateCalendar();
  }

  onContractChange(event: any) {
    this.getContractId = event.target.value;
    //get client details from contract id
    this.selectedContract = this.contracts.find((contract: any) => contract.id == event.target.value);
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
    this.loading = true;
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters?.status,
      search: this.filters?.searchItem,
    };
    this.clientService.getClients(options).subscribe(
      (response: any) => {
        this.clients = response.body.data;
        this.numberOfClients = this.clients.length;
        this.loading = false;
      }, (error: any) => {
        this.toastrService.error('Error getting clients', error.message);
        this.loading = false;
      }
    )
  }

  addSession() {
    const params = {
      clientId: this.selectedContract.clientId,
      contractId: this.getContractId,
    };

    this.sessionService.addSession(this.formData, params).subscribe((res: any) => {
      this.toastrService.success('Session added successfully');
      window.history.back();
    }, error => {
      this.toastrService.error(error.error.message);
    });
  }


  getContracts(page: number) {
    this.loading = true;
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
      this.contracts = res.body;
      this.totalElements = res.body.totalElements;
      this.loading = false;
    }, (error: any) => {
      this.toastrService.error('Error getting contracts', error.message);
      this.loading = false;
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
        this.loading = false;
        this.prepareCoachSlotsInCalendar(this.coachSlots);
      }
    });
  }

  prepareCoachSlotsInCalendar(coachSlots: any) {
    this.bookedSessions = [];
    let startDate = Date.now();
    let endDate = new Date();
    endDate.setMonth(endDate.getMonth() + 3);
    let dates = this.getDates(startDate, endDate);
    dates.forEach((date: any) => {
      coachSlots.forEach((slot: any) => {
        if (slot.dayOfTheWeek.day.toUpperCase() === date.dayOfTheWeek.toUpperCase() && slot.dayOfTheWeek.available === true) {
          //check if the date is already in the bookedSessions array
          if (this.bookedSessions !== undefined && this.bookedSessions.length > 0) {
            let dateObj = this.findDate(date.date);
            if (dateObj) {
              dateObj.count = dateObj.count + 1;
            } else {
              this.bookedSessions.push({
                date: date.date,
                day: date.dayOfTheWeek,
                count: 1
              });
            }
          } else {
            this.bookedSessions.push({
              date: date.date,
              day: date.dayOfTheWeek,
              count: 1
            });
          }
        }
      });
    });
    this.generateCalendar();
  }

  findDate(dateToFind: any) {
    return this.bookedSessions.find(dateObj => dateObj.date === dateToFind);
  }

  getDates(startDate: any, endDate: any) {
    let dates: any[] = [];
    let currentDate = startDate;
    while (currentDate <= endDate) {
      dates.push({
        date: this.formatDateToYYYYMMDD(currentDate),
        dayOfTheWeek: new Date(this.formatDateToYYYYMMDD(currentDate)).toLocaleDateString('en-US', { weekday: 'long' }),
      });
      currentDate += 24 * 60 * 60 * 1000;
    }
    return dates;
  }


  selectedSessionSlot(slot: any) {
    slot.coach = {};
    slot.dayOfTheWeek.coach = {};
    this.formData.sessionSchedules = slot;
  }

  onDateSelect(day: any): void {
    //format Wed Aug 16 2023 00:00:00 GMT+0300 (East Africa Time) to 2023-08-16
    this.formData.sessionDate = this.formatDateToYYYYMMDD(day.date)
    //get day of the week from the selected date
    let dayOfTheWeek = new Date(this.formData.sessionDate).toLocaleDateString('en-US', { weekday: 'long' });

    //loop through the coach slots and find slots days of the week that match the selected date and push them to the slots array
    this.slots = [];
    this.coachSlots.forEach((slot: any) => {
      if (slot.dayOfTheWeek.day === dayOfTheWeek.toUpperCase()) {
        this.slots.push(slot);
      }
    }
    );
  }

  formatDateToYYYYMMDD(inputDate: any) {
    const date = new Date(inputDate);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
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

