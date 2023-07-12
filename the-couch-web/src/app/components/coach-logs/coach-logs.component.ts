import { Component, OnInit } from '@angular/core';
import { style, animate, transition, trigger } from '@angular/animations';
import { CoachLogsService } from '../../services/coach-logs.service';

@Component({
  selector: 'app-coach-logs',
  templateUrl: './coach-logs.component.html',
  styleUrls: ['./coach-logs.component.css'],
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
export class CoachLogsComponent implements OnInit {

  coachingLog = {
      noInGroup: '',
      clientName: '',
      clientEmail: '',
      startDate: '',
      endDate: '',
      paidHours: '',
      proBonoHours: '',
      createdAt: '',
      createdBy: ''
  };
  loading = false;
  itemsPerPage = 20;
  filters: any = {
    status: '',
    searchItem: '',
  };
  coachingLogs: any;
  userRole: any;
  page: number = 0;
  pageSize: number = 15;
  totalElements: any;

  constructor(private coachLogsService: CoachLogsService) { }

  ngOnInit(): void {

    this.getCoachingLogs(0);
  }

  getCoachingLogs(page: any) {
    this.loading = true;
    const options = {
      page: page,
      sort: 'id,desc',
      size: this.itemsPerPage,
      search: this.filters.searchItem,
    };
    this.coachLogsService.getCoachLogs(options).subscribe({
      next: (response: any) => {
        this.coachingLogs = response.body;
        this.totalElements = response.headers.get('X-Total-Count');
        this.loading = false;
      }
    });

  }


  onTableDataChange(event: any) {
    this.page = event;
    this.getCoachingLogs(this.page);
  }

  addCoachingLog() {
    console.log(this.coachingLog);
    this.loading = true;
    this.coachLogsService.addCoachLogs(this.coachingLog).subscribe({
      next: (response) => {
        console.log(response);
        this.loading = false;
        this.getCoachingLogs(0);
      }
    });

  }

}
