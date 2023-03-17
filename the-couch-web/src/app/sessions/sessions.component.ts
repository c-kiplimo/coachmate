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
  id: any;
  constructor(private apiService: ClientService, private router: Router,private activatedRoute: ActivatedRoute) {}

  
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
    } else if(this.userRole == 'CLIENT'){
      this.User = JSON.parse(sessionStorage.getItem('user') as any);
      console.log(this.User);
      this.clientId = this.User.id
      console.log("client id",this.clientId)

      this.id = this.clientId
      const email = {
        email: this.User.email
      }
      
      this.apiService.getClientByEmail(email).subscribe(
        (response: any) => {
          console.log(response);
          this.getClientSessions() 
        },
        (error: any) => {
          console.log(error);
        }
      );
    }
  }

  getClientSessions() {
    console.log("client id",this.clientId)
    this.loading = true;
    this.apiService.getClientSessions(this.clientId)
      .subscribe((data: any) => {
        this.sessions = data.body;
        console.log(this.sessions);
        this.loading = false;
        console.log("sessions gotten here",this.sessions)
      },
      (error: any) => {
        console.log(error);
        this.loading = false;
      }
      );
  }

  getAllOrgSessions(id: any) {
    this.loading = true;
    window.scroll(0, 0);
    this.page = 1;
    const options = {
      page: 1,
      id: id,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
    };
    this.apiService.getOrgSessions(options).subscribe(
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
    this.sessions = [];
    window.scroll(0, 0);
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
  getFiltersessions(page: number, period: string, navigate?: boolean):void {
    this.sessions = [];
    this.loading = true;

    window.scroll(0, 0);
    this.page = page;

    const options = {
      page: page,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.search,
      period: period,
      balance: this.filters.balance,
    };

    this.apiService.getFiltered(options).subscribe(
      (res: any) => {
        // console.log( res.body.data);
        this.loading = false;
        this.sessions = res.body.data;
        this.noOfSessions = this.sessions.length;
        // console.log('here are orders', this.orders);

        this.totalLength = Number(res.body.totalElements);

      },
      (error: any) => {
        console.log(error);
        this.loading = false;
        
        // return this.noOfOrders;
      }
    );


  }
}
