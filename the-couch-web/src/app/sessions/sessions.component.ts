import { Component, OnInit } from '@angular/core';
import { ClientService } from '../services/ClientService';
import { Router, ActivatedRoute } from '@angular/router';
import { style, animate, transition, trigger } from '@angular/animations';

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
  loading = true;
  itemsPerPage = 20;
  filters: any = {
    status: '',
    searchItem: '',
  };
  filterOptions!: boolean;
  currentTab!: string;
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
  User: any;
  sessions: any;
  page: number = 1;
  clientId: any;
  id!: number;
  orgId!: number;
  constructor(private apiService: ClientService, private router: Router,private activatedRoute: ActivatedRoute) {}

  
  ngOnInit(): void { 
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);
    this.userRole = this.coachData.userRole;
    console.log(this.userRole);

 
    if(this.userRole == 'ORGANIZATION'){
      this.orgId = this.coachData.organization.id;
      console.log('organization id =>', this.orgId);
      console.log('user role=>', this.userRole);
      console.log('coach data=>', this.coachData);
    this.OrgData = sessionStorage.getItem('Organization');

    this.getAllOrgSessions();
      
    } else if(this.userRole == 'COACH'){
      this.getAllSessions();
    } else if(this.userRole == 'CLIENT'){
      this.User = JSON.parse(sessionStorage.getItem('user') as any);
      console.log(this.User);
      this.clientId = this.User.id
      console.log("client id",this.clientId)
      this.id = this.clientId
      this.getClientSessions() 
    }
  }

  getClientSessions() {
    console.log("client id",this.clientId)
    this.loading = true;
    this.apiService.getClientSessions(this.clientId)
      .subscribe((data: any) => {
        this.sessions = data.body;
        this.loading = false;
        console.log("client sessions gotten here",this.sessions)
      },
      (error: any) => {
        console.log(error);
        this.loading = false;
      }
      );
  }

  getAllOrgSessions() {
    this.loading = true;
    
    this.page = 1;
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
      orgId: this.orgId
    };
    this.apiService.getOrgSessions(options,this.orgId).subscribe(
      (response: any) => {
        this.sessions = response;
        console.log("org sessions gotten here",this.sessions)
        this.loading = false;
      },
      (error: any) => {
        console.log(error);
      }
    );
  }

  getAllSessions() {
    this.loading = true;
    this.sessions = [];
    
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
    };
    this.apiService.getSessions(options).subscribe(
      (response: any) => {
        this.sessions = response.body.data;
        console.log("all sessions gotten here",this.sessions)
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
