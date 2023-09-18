import { Component, OnInit } from '@angular/core';
import { ClientService } from '../../services/ClientService';
import { CoachEducationService } from '../../services/CoachEducationService';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { SessionsService } from 'src/app/services/SessionsService';
import { ContractsService } from 'src/app/services/contracts.service';
import { animate, style, transition, trigger } from '@angular/animations';
import { th } from 'date-fns/locale';
import { CoachService } from 'src/app/services/CoachService';
import { set } from 'date-fns';



@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
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
export class DashboardComponent implements OnInit {
  user: any;
  clients: any;
  organization: any;
  sessions: any;
  orgName: any;
  contracts: any;
  coachesImpl: any;
  numberOfClients!: number;
  numberOfSessions!: number;
  numberOfContracts!: number;
  numberOfHours: any;
  numberOfMinutes: any;
  success_rate_percent: any;
  rightIcon: any;
  itemsPerPage = 20;
  filters: any = {
    status: '',
    searchItem: '',
  };
  coachEducationData: any;
  numberOfHoursCoachEducation: any;
  numberOfHoursCoachingHours: any;
  coachSessionData: any;
  userRole: any;

  contractId: any;
  loading!: boolean;
  orgId!: number;

  coaches: any;
  numberofCoaches!: number;

  orgData: any;
  orgSession: any;
  orgContracts: any;

  session: any;
  feedbacks: any;
  userData: any;

  page: number = 0;
  pageSize: number = 5;
  totalElements: any;

  currentTab = 'sessions';

  coachId!: number;
  clientId!: number;

  todaysSessions: any = [];
  upcomingSessions: any = [];

  constructor(
    private clientService: ClientService,
    private coachService: CoachService,
    private CoachEducationService: CoachEducationService,
    private router: Router,
    private route: ActivatedRoute,
    private sessionService: SessionsService,
    private contractsService: ContractsService,
  ) { }

  ngOnInit(): void {
    
    this.userData = sessionStorage.getItem('user');

    if(this.userData != null){
    this.user = JSON.parse(this.userData);
    this.userRole = this.user.userRole;
    } else {
      this.router.navigate(['/signin']);
    }

    if (this.userRole == 'COACH') {
      this.coachId = this.user.id;

      this.getRecentSessions(this.page);
      this.getAllContracts(this.page);
      this.getClients(this.page);
      this.filterTodaysSessions(this.page);
      this.filterUpcomingSessions(this.page);
    } else if (this.userRole == 'ORGANIZATION') {
      this.orgId = this.user.organization.id;
      console.log('ORGANIZATION');
      this.getAllContracts(this.page)
      this.getOrgCoaches(this.page);
      this.getClients(this.page);

    } else if (this.userRole == 'CLIENT') {
      this.clientId = this.user.id;
      this.getRecentSessions(this.page);
      this.getAllContracts(this.page);
      this.filterTodaysSessions(this.page);
      this.filterUpcomingSessions(this.page);
    }

  }

  reload() {
    location.reload();
  }

  getRecentSessions(page: any) {
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
      sort: 'id,desc',
    };
    if(this.userRole == 'COACH'){
      options.coachId = this.coachId;
    }else if(this.userRole == 'CLIENT'){
      options.clientId = this.clientId;
    }else if(this.userRole == 'ORGANIZATION'){
      //options.orgId = this.orgId;
      options.coachId = this.coachId;
    }

    this.sessionService.getSessions(options).subscribe(
      (response: any) => {
        this.sessions = response.body;
        this.totalElements = +response.headers.get('X-Total-Count');
        this.loading = false;
      },
      (error: any) => {
        this.loading = false;
      }
    );
  }
  filterUpcomingSessions(page: any) {
    this.loading = true;
  
   
    let startDate = new Date();
   
  
 
    let endDate = new Date(startDate);
    endDate.setDate(endDate.getDate() + 6); 
  
    const options: any = {
      page: 0, 
      size: this.pageSize,
      sessionStartDate: startDate.toISOString().slice(0, 10), 
      sessionEndDate: endDate.toISOString().slice(0, 10), 
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
        
        const allSessions = response.body;
  
        
        const futureSessions = allSessions.filter((session: any) => {
          const sessionDate = new Date(session.sessionDate);
          return sessionDate >= startDate;
        });
  
        
        futureSessions.sort((a: any, b: any) => {
          const dateA = new Date(a.sessionDate).getTime();
          const dateB = new Date(b.sessionDate).getTime();
          return dateA - dateB;
        });
  
        
        this.upcomingSessions = futureSessions;
  
        
        this.totalElements = futureSessions.length as number;
  
        this.loading = false;
      },
      (error: any) => {
        this.loading = false;
      }
    );
  }
  
  
  
  filterTodaysSessions(page: any) {
    this.loading = true;
    let today = new Date().toISOString().slice(0, 10);
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
      sessionDate: today,
      sort: 'id,desc',
    };
    if(this.userRole == 'COACH'){
      options.coachId = this.coachId;
    }else if(this.userRole == 'CLIENT'){
      options.clientId = this.clientId;
    }else if(this.userRole == 'ORGANIZATION'){
      options.orgId = this.orgId;
    }

    this.sessionService.getSessions(options).subscribe(
      (response: any) => {
        this.todaysSessions = response.body;
        this.totalElements = +response.headers.get('X-Total-Count');
        this.loading = false;
      },
      (error: any) => {
        this.loading = false;
      }
    );
  }

  getAllContracts(page: any) {

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

    if(this.userRole == 'COACH'){
      options.coachId = this.coachId;
    }else if(this.userRole == 'CLIENT'){
      options.clientId = this.clientId;
    }else if(this.userRole == 'ORGANIZATION'){
      options.orgId = this.orgId;
    }

    this.contractsService.getContracts(options).subscribe(
      (res: any) => {
        this.contracts = res.body;
        this.loading = false;
        this.numberOfContracts = this.contracts.length;
      }, (err: any) => {
        this.loading = false;
      }
    );

  }

  getCoachFeedbacks(coachId: any) {
    this.loading = true;
    this.clientService.getCoachFeedbacks(coachId).subscribe(
      (response: any) => {
        this.feedbacks = response.body;
        let totalScore = 0;

        for (let i = 0; i < Math.max(this.feedbacks.length, 1); i++) {
          totalScore += this.feedbacks[i].overallScore;
        }
        const success_rate = totalScore / (Math.max(this.feedbacks.length, 1) * 25);
        this.success_rate_percent = Math.round(success_rate * 100);

        this.loading = false;
      }
    );

  }

  getOrgFeedbacks(orgId: any) {

    this.loading = true;
    this.clientService.getOrgFeedbacks(orgId).subscribe(
      (response: any) => {
        this.feedbacks = response.body;
        let totalScore = 0;

        for (let i = 0; i < Math.max(this.feedbacks.length, 1); i++) {
          totalScore += this.feedbacks[i].overallScore;
        }
        const success_rate = totalScore / (Math.max(this.feedbacks.length, 1) * 25);
        this.success_rate_percent = Math.round(success_rate * 100);

        this.loading = false;
      }
    );

  }
  // get all feedbacks overrall score and get a percentage
  getAllOrgSessions(id: any) {
    this.sessions = [];

    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
    };

    // this.clientService.getOrgSessions(options, id).subscribe(
    //   (response: any) => {
    //     console.log(response);
    //     this.sessions = response;
    //     this.loading = false;
    //     this.numberOfSessions = this.sessions.length;
    //     (error: any) => {
    //       console.log(error);
    //     }
    //   }
    // );

  }
  getNoOfSessions() {
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
    };
    this.clientService.getSessions(options).subscribe(
      (response: any) => {
        this.sessions = response.body.data;
        this.numberOfSessions = response.body.totalElements;
        let totalMinutes = 0;
        for (let i = 0; i < Math.max(this.sessions.length, 1); i++) {
          let startTime = i < this.sessions.length ? this.sessions[i].sessionStartTime : "00:00";
          let endTime = i < this.sessions.length ? this.sessions[i].sessionEndTime : "00:00";

          let start = startTime.split(':');
          let end = endTime.split(':');

          let startMinutes = parseInt(start[0]) * 60 + parseInt(start[1]);
          let endMinutes = parseInt(end[0]) * 60 + parseInt(end[1]);

          if (startMinutes >= 0 && endMinutes >= 0 && endMinutes >= startMinutes) {
            totalMinutes += endMinutes - startMinutes;
          }
        }

        this.numberOfHours = Math.floor(totalMinutes / 60);
        this.numberOfMinutes = totalMinutes - this.numberOfHours * 60;

      },
      (error: any) => {
        this.loading = false;
      }
    );
  }


  navigateToSessionView(id: any) {
    this.router.navigate(['sessionView', id]);
  }

  
  getClients(page: any) {
    this.loading = true;
    this.page = page;
    //if page is 0, don't subtract 1
    if (page === 0 || page < 0) {
      page = 0;
    } else {
      page = page - 1;
    }
    if(this.filters.status == 'ALL'){
      this.filters.status = '';
    }
    const options: any = {
      page: page,
      size: this.pageSize,
      status: this.filters.status,
      search: this.filters.searchItem,
      sort: 'id,desc',
    };

    if(this.userRole == 'COACH'){
      options.coachId = this.coachId;
    }else if(this.userRole == 'CLIENT'){
      options.clientId = this.clientId;
    }else if(this.userRole == 'ORGANIZATION'){
      options.orgId = this.orgId;
    }
    
    this.clientService.getClients(options).subscribe(  // test the getAllOrgClients endpoint
      (response) => {
        this.loading = false;
        this.clients = response.body;
        for (let client of this.clients) {
          if (client.userRole != 'CLIENT') {
            this.clients.splice(this.clients.indexOf(client), 1);
          }
        }
        this.totalElements = +response.headers.get('X-Total-Count');
        this.numberOfClients = this.clients.length;
      }, (error) => {
        this.loading = false;
      }
    )
  }

  navigateToTerms(id: any) {
    this.contractId = id;
    if (this.userRole == 'COACH') {

      this.router.navigate(['/contractDetail', id]);
    } else if (this.userRole == 'CLIENT' && id.contractStatus == null) {
      this.router.navigate(['/terms', id]);
    } else (this.userRole == 'CLIENT' && id.contractStatus == "SIGNED")
    this.router.navigate(['/contractDetail', id]);


  }

  getOrgCoaches(page: any) {
 
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
        status: this.filters.status,
        search: this.filters.searchItem,
        sort: 'id,desc',
      };
    
     if(this.userRole == 'ORGANIZATION'){
        options.orgId = this.orgId;
      }
      
      this.clientService.getOrgCoaches(options).subscribe(
        (response) => {
          this.loading = false;
          this.coaches = response.body;
          this.totalElements = +response.headers.get('X-Total-Count');
        }, (error) => {
          this.loading = false;
        }
      )
  }


  getClientContracts(id: any) {
    // const data = {
    //   clientId: id,
    // }
    this.clientService.getContractsByClientId(id).subscribe(
      (response: any) => {
        this.contracts = response.body;

      },
      (error: any) => {
        this.loading = false;
      }
    );
  }

  // getNoOfContracts() {
  //   this.clientService.getContracts().subscribe(
  //     (response: any) => {

  //       this.contracts = response.body;
  //       console.log(this.contracts);
  //       this.numberOfContracts = this.contracts.length;
  //       console.log(this.numberOfContracts);
  //     },
  //     (error: any) => {
  //       console.log(error);
  //     }
  //   );
  // }


  navigateToContractDetail(id: any) {
    this.contractId = id;
    this.router.navigate(['/contractDetail/' + id]);
  }

  navigateToCoachView(id : number) {
    this.router.navigate(['/coachView/' + id]);
  }


  getUser() {
    this.user = JSON.parse(sessionStorage.getItem('user') as any);
  }
  // getUserOrg() {
  //   this.User = JSON.parse(sessionStorage.getItem('user') as any);
  //   console.log(this.User);
  //   this.getOrganization(this.User.id);

  // }


  getCoachEducation(id: any) {
    const options = {
      coachId: id,
    }

    this.CoachEducationService.getCoachEducation(options).subscribe(
      (response: any) => {
        this.coachEducationData = response;
        this.calculateNumberOfHours(this.coachEducationData);
      }, (error: any) => {
        this.loading = false;
      }
    )

  }

  calculateNumberOfHours(data: any) {
    //calculate total number of hours
    let totalHours = 0;
    data.forEach((element: any) => {
      totalHours += parseInt(element.trainingHours);
    });
    this.numberOfHoursCoachEducation = Math.floor(totalHours);
  }

  getClientSessions(id: any) {

    this.clientService.getClientSessions(id).subscribe(
      (response: any) => {
        this.sessions = response.body;
        this.numberOfSessions = this.sessions.length;
        this.loading = false;
      },
      (error: any) => {
        this.loading = false;
      }
    );

  }


  getClass(sessions: any) {
    if (sessions.status === 'CONDUCTED') {
      return 'badge-warning';
    } else if (sessions.status === 'CONFIRMED') {
      return 'badge-success';
    }
    else (sessions.status === 'CANCELLED')
    return 'badge-success';
  }

  toggleTab(tab: string): void {
    this.currentTab = tab;
  }

}
