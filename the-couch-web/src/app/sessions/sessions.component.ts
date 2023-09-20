import { Component, OnInit } from '@angular/core';
import { ClientService } from '../services/ClientService';
import { SessionsService } from '../services/SessionsService';
import { Router, ActivatedRoute } from '@angular/router';
import { style, animate, transition, trigger } from '@angular/animations';
import { GoogleSignInService } from '../services/google-sign-in.service';

import { id } from 'date-fns/locale';

import { IconProp } from '@fortawesome/fontawesome-svg-core';

@Component({
  selector: 'app-sessions',
  templateUrl: './sessions.component.html',
  styleUrls: ['./sessions.component.css'],
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
export class SessionsComponent implements OnInit {
  filterIcon!: IconProp;
  rightIcon!: IconProp;
  searchIcon!: IconProp;
  session: any;
  loading: boolean = false;
  itemsPerPage = 20;
  filters: any = {
    status: '',
    searchItem: '',
  };
  filterOptions!: boolean;
  currentTab = 'sessions';
  totalLength!: number;
  noOfSessions: any;
  backIcon!: IconProp;
  addIcon!: IconProp;
  salesData: any;
  coachSessionData: any;
  coachData: any;
  userRole: any;
  OrgData: any;
  orgSession: any;
  user: any;
  sessions: any;
  clientId: any;
  id!: number;

  orgId!: number;
  showFilters = false;
  coachId!: number;

  page: number = 0;
  pageSize: number = 15;
  sessionStatuses = ['CONFIRMED', 'CANCELLED', 'COMPLETED']
  totalElements: any;

  googleCalendarEvents: any;
  googleCalenderConnected: boolean = false;

  constructor(private apiService: ClientService,
    private router: Router,
    private sessionService: SessionsService,
    private googleSignInService: GoogleSignInService,
    private activatedRoute: ActivatedRoute) { }


  ngOnInit(): void {
    this.coachSessionData = sessionStorage.getItem('user');
    this.user = JSON.parse(this.coachSessionData);

    this.userRole = this.user.userRole;
    console.log(this.userRole);


    if (this.userRole == 'ORGANIZATION') {
      this.orgId = this.user.organization.id;
      this.getAllSessions(this.page);

    } else if (this.userRole == 'COACH') {
      this.coachId = this.user.id;
      this.getAllSessions(this.page);

    } else if (this.userRole == 'CLIENT') {
      this.clientId = this.user.id;
      this.getAllSessions(this.page);
    }
    
    this.getCalendarEvents();
  }

  connectToGoogleCalendar() {
    this.googleSignInService.signIn().then((res: any) => {
      console.log(res);
      this.googleCalenderConnected = true;
      this.getCalendarEvents();
    }).catch((err: any) => {
      console.log(err);
      this.googleCalenderConnected = false;
    });
  }

  disconnectGoogleCalender() {
    this.googleSignInService.signOut();
    this.googleCalenderConnected = false;
  }

  getCalendarEvents() {
    this.googleSignInService.getCalendarEvents().subscribe((events: any) => {
    console.log(events);
    this.googleCalendarEvents = events;
    this.googleCalenderConnected = true;
  }
  );
}

toggleTab(tab: string): void {
  this.currentTab = tab;
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
      sessionStatus: this.filters.status,
      search: this.filters.searchItem,
      sort: 'id,desc',
    };
    if (this.userRole == 'COACH') {
      options.coachId = this.coachId;
    } else if (this.userRole == 'CLIENT') {
      options.clientId = this.clientId;
    } else if (this.userRole == 'ORGANIZATION') {
      options.orgId = this.orgId;
    }

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

  toggleFilters() {
    this.showFilters = !this.showFilters;
  }

  filterByStatus(event: any): any {
    this.page = 0;
    this.filters.status = event.target.value;
    this.getAllSessions(this.page);
  }

  navigateToSessionView(id: any) {
    console.log(id);
    this.router.navigate(['sessionView', id]);
  }
  search() {
    this.page = 0;
    this.getAllSessions(this.page);
  }

  onTableDataChange(event: any) {
    this.page = event;
    this.getAllSessions(this.page);
  }

  resetStatuses(): void {
    this.filters.status = '';
    this.getAllSessions(this.page);
  }
}
