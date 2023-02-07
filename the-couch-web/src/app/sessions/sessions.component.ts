import { Component, OnInit } from '@angular/core';
import { ClientService } from '../services/ClientService';
import { Router, ActivatedRoute } from '@angular/router';
import { style, animate, transition, trigger } from '@angular/animations';
import { id } from 'date-fns/locale';
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

  loading = true;
  itemsPerPage = 20;
  filters: any = {
    status: '',
    searchItem: '',
  };

  coachSessionData: any;
  coachData: any;
  userRole: any;

  OrgData: any;
  orgSession: any;



  constructor(private apiService: ClientService, private router: Router) {}

  sessions: any;

  ngOnInit(): void { 
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);
    this.userRole = this.coachData.userRole;
    console.log(this.userRole);

 
    if(this.userRole == 'ORGANIZATION'){
    this.OrgData = sessionStorage.getItem('Organization');
    this.orgSession = JSON.parse(this.OrgData);
    console.log(this.orgSession);

    this.getAllOrgSessions(this.orgSession.id);
      
    } else if(this.userRole == 'COACH'){
      this.getAllSessions();
    }
  }

  getAllOrgSessions(id: any) {
    this.loading = true;
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
    };
    this.apiService.getOrgSessions(id).subscribe(
      (response: any) => {
        console.log(response);
        this.sessions = response;
        this.loading = false;
      },
      (error: any) => {
        console.log(error);
      }
    );
  }

  getAllSessions() {
    this.loading = true;
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
    };
    this.apiService.getSessions(options).subscribe(
      (response: any) => {
        console.log(response.body.data);
        this.sessions = response.body.data;
        this.loading = false;
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
}
