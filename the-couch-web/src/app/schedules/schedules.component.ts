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

import { setOptions, MbscDatepicker  } from '@mobiscroll/angular';

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
      label: '<i class="fas fa-fw fa-pencil-alt" style="color:black"></i>',
      a11yLabel: 'Edit',
      onClick: ({ event }: { event: CalendarEvent }): void => {
        this.handleEvent('Edited', event);
      },
    },
    {
      label: '<i class="fas fa-fw fa-trash-alt"  style="color:black"></i>',
      a11yLabel: 'Delete',
      onClick: ({ event }: { event: CalendarEvent }): void => {
        this.events = this.events.filter((iEvent) => iEvent !== event);
        this.handleEvent('Deleted', event);
      },
    },
  ];

  refresh = new Subject<void>();

  events: CalendarEvent[] = [];
    // {
    //   start: subDays(startOfDay(new Date()), 1),
    //   end: addDays(new Date(), 1),
    //   title: 'erere',
    //   color: { ...colors['red'] },
    //   actions: this.actions,
    //   allDay: true,
    //   resizable: {
    //     beforeStart: true,
    //     afterEnd: true,
    //   },
    //   draggable: true,
    // },
    // {
    //   start: startOfDay(new Date()),
    //   title: 'Hellow',
    //   color: { ...colors['red'] },
    //   actions: this.actions,
    // },
    // {
    //   start: subDays(endOfMonth(new Date()), 3),
    //   end: addDays(endOfMonth(new Date()), 3),
    //   title: 'Hello',
    //   color: { ...colors['blue'] },
    //   allDay: true,
    // },
  //   {
  //     start: addHours(startOfDay(new Date()), 2),
  //     end: addHours(new Date(), 2),
  //     title: 'A draggable and resizable event',
  //     color: { ...colors['yellow'] },
  //     actions: this.actions,
  //     resizable: {
  //       beforeStart: true,
  //       afterEnd: true,
  //     }
  //   },
  // ];

  activeDayIsOpen: boolean = true;
  modal: any;

  constructor(private restApiService: ClientService) { }

  ngOnInit(): void {
    this.getSessions();
  }

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
    this.modalData = { event, action };
    this.modal.open(this.modalContent, { size: 'lg' });
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

  getSessions() {
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
    };
    this.restApiService.getSessions(options).subscribe(
      (response: any) => {
        console.log(response);
        this.sessions = response.body.data;
        this.setCalendarEvents(this.sessions);
      },
      (error: any) => {
        console.log(error);
      }
    );
  }
  setCalendarEvents(sessions: any) {
    console.log('here');
    this.events = [];
    sessions.forEach((session: any) => {
      const event = {
        start: new Date(`${session.sessionSchedules.sessionDate}T${session.sessionSchedules.startTime}`),
        end: new Date(`${session.sessionSchedules.sessionDate}T${session.sessionSchedules.endTime}`),
        title: session.name + " with " + session.client.fullName,
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
      this.view = CalendarView.Week;
      this.refresh.next();
    });
    console.log(this.events);
  }
}
