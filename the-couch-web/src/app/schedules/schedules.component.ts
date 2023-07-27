import {
  Component,
  OnInit,
  ChangeDetectionStrategy,
  ViewChild,
  TemplateRef,
} from '@angular/core';

import {
  startOfDay,
  endOfDay,
  subDays,
  addDays,
  endOfMonth,
  isSameDay,
  isSameMonth,
  addHours,
} from 'date-fns';
import { Subject } from 'rxjs';
// import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {
  CalendarEvent,
  CalendarEventAction,
  CalendarEventTimesChangedEvent,
  CalendarView,
} from 'angular-calendar';
import { EventColor } from 'calendar-utils';
import { ClientService } from '../services/ClientService';
import { SessionsService } from '../services/SessionsService';

import { GoogleSignInService } from '../services/google-sign-in.service';


import { setOptions, MbscDatepicker } from '@mobiscroll/angular';
import { Router } from '@angular/router';

const colors: Record<string, EventColor> = {
  red: {
    primary: '#ff3333',
    secondary: '#ffb84d',
  },
  blue: {
    primary: '#198754',
    secondary: '#198754',
  },
  yellow: {
    primary: '#198754',
    secondary: '#198754',
  },
};

@Component({
  selector: 'app-schedules',
  templateUrl: './schedules.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  styleUrls: ['./schedules.component.css'],
})
export class SchedulesComponent implements OnInit {
  sessions!: any;
  itemsPerPage = 20;
  filters: any = {
    status: '',
    searchItem: '',
  };

  coachSessionData: any;
  coachData: any;
  userRole: any;
  user: any;
  clientId: any;

  orgId!: number;
  coachId!: number;
  loading = true;

  page: number = 0;
  pageSize: number = 100;
  totalElements: any;


  @ViewChild('modalContent', { static: true }) modalContent:
    | TemplateRef<any>
    | undefined;

  view: CalendarView = CalendarView.Month;

  CalendarView = CalendarView;

  viewDate: Date = new Date();

  modalData:
    | {
      action: String;
      event: CalendarEvent;
    }
    | undefined;

  actions: CalendarEventAction[] = [
    {
      label: '<i class="fa-solid fa-arrow-up-right-from-square"></i>',
      a11yLabel: 'View',
      onClick: ({ event }: { event: CalendarEvent }): void => {
        this.handleEvent('Edited', event);
      },
    },
    // {
    //   label: '<i class="fas fa-fw fa-trash-alt"  style="color:black"></i>',
    //   a11yLabel: 'Delete',
    //   onClick: ({ event }: { event: CalendarEvent }): void => {
    //     this.events = this.events.filter((iEvent) => iEvent !== event);
    //     this.handleEvent('Deleted', event);
    //   },
    // },
  ];

  refresh = new Subject<void>();

  events: CalendarEvent[] = [];

  activeDayIsOpen: boolean = true;
  modal: any;

  constructor(
    private sessionService: SessionsService,
    private router: Router,
    private googleSignInService: GoogleSignInService
  ) { }

  ngOnInit(): void {
    this.coachSessionData = sessionStorage.getItem('user');
    this.user = JSON.parse(this.coachSessionData);

    this.userRole = this.user.userRole;
    console.log(this.userRole);


    if (this.userRole == 'ORGANIZATION') {
      this.orgId = this.user.id;
      this.getSessions(this.page);

    } else if (this.userRole == 'COACH') {
      this.coachId = this.user.id;
      this.getSessions(this.page);

    } else if (this.userRole == 'CLIENT') {
      this.clientId = this.user.id;
      this.getSessions(this.page);
    }
  }

  /////////////////////////////////

  onSignIn() {
    this.googleSignInService.signIn().subscribe((success) => {
      if (success) {
        // Sign-in successful, you can now access the user's Google information and proceed with other tasks.
      } else {
        // Sign-in failed or was canceled.
      }
    });
  }

  onSignOut() {
    this.googleSignInService.signOut();
  }

  /////////////////////////////////

  dayClicked({ date, events }: { date: Date; events: CalendarEvent[] }): void {
    if (isSameMonth(date, this.viewDate)) {
      if (
        (isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) ||
        events.length === 0
      ) {
        this.activeDayIsOpen = false;
      } else {
        this.activeDayIsOpen = true;
      }
      this.viewDate = date;
    }
  }

  eventTimesChanged({
    event,
    newStart,
    newEnd,
  }: CalendarEventTimesChangedEvent): void {
    this.events = this.events.map((iEvent) => {
      if (iEvent === event) {
        return {
          ...event,
          start: newStart,
          end: newEnd,
        };
      }
      return iEvent;
    });
    this.handleEvent('Dropped or resized', event);
  }

  handleEvent(action: string, event: CalendarEvent): void {
    //   this.modalData = { event, action };
    //   this.modal.open(this.modalContent, { size: 'lg' });

    //  this.router.navigate(['/terms', this.coachData.id]);
  }

  addEvent(): void {
    this.events = [
      ...this.events,
      {
        title: 'New event',
        start: startOfDay(new Date()),
        end: endOfDay(new Date()),
        color: colors['red'],
        draggable: true,
        resizable: {
          beforeStart: true,
          afterEnd: true,
        },
      },
    ];
  }

  deleteEvent(eventToDelete: CalendarEvent) {
    this.events = this.events.filter((event) => event !== eventToDelete);
  }

  setView(view: CalendarView) {
    this.view = view;
  }

  closeOpenMonthViewDay() {
    this.activeDayIsOpen = false;
  }

  getSessions(page: any) {
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
      //options.orgId = this.orgId;
      options.coachId = this.coachId;
    }

    this.sessionService.getSessions(options).subscribe(
      (response: any) => {
        console.log(response);
        this.sessions = response.body;
        this.setCalendarEvents(this.sessions);
        this.loading = false;
      },
      (error: any) => {
        console.log(error);
        this.loading = false;
      }
    );
  }

  setCalendarEvents(sessions: any) {
    console.log('here');
    this.events = [];
    sessions.forEach((session: any) => {
      const event = {
        start: new Date(`${session.sessionSchedulesSessionDate}T${session.sessionSchedulesStartTime}`),
        end: new Date(`${session.sessionSchedulesSessionDate}T${session.sessionSchedulesEndTime}`),
        title: session.name + " with " + session.clientFullName,
        color: { ...colors['green'] },
        actions: this.actions,
        allDay: false,
        resizable: {
          beforeStart: true,
          afterEnd: true,
        },
        // draggable: true,
      };
      this.events.push(event);
      this.view = CalendarView.Month; //Week, Month, Day
      this.refresh.next();
    });
    console.log(this.events);
  }
}
