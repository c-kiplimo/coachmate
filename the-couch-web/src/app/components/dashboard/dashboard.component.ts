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
    console.log(this.user);
    this.userRole = this.user.userRole;
    console.log(this.userRole);
    } else {
      this.router.navigate(['/signin']);
    }

    if (this.userRole == 'COACH') {
      this.coachId = this.user.id;

      this.getRecentSessions(this.page);
      this.getAllContracts(this.page);
      this.getClients(this.page);

      // t.getAllContracts
      // t.getAllContracts
      // this.getNoOfContracts();
      // this.getCoachEducation(this.user.id);
      // this.getCoachFeedbacks(this.user.id);
      // this.getAllContracts();

    } else if (this.userRole == 'ORGANIZATION') {
      this.orgId = this.user.organization.id;
      console.log('ORGANIZATION');
      // this.getUserOrg();
      // this.getClients(this.page);
      //this.getCoaches(this.page);
      // this.getAllContracts(this.page);
      // this.orgData = sessionStorage.getItem('Organization');
      // this.orgSession = JSON.parse(this.orgData);
      // console.log(this.orgSession);
      // this.getOrgContracts(this.orgId);
      // this.getAllOrgSessions(this.orgId);
      // this.getOrgFeedbacks(this.orgId);
      this.getOrgCoaches(this.page);

    } else if (this.userRole == 'CLIENT') {
      this.clientId = this.user.id;

      this.getRecentSessions(this.page);
      this.getAllContracts(this.page);
      // console.log('not coach');
      // this.getClientContracts(this.user.id);
      // this.getClientSessions(this.user.id);
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
        console.log(error);
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
      //options.orgId = this.orgId;
      options.coachId = this.coachId;
    }

    this.contractsService.getContracts(options).subscribe(
      (res: any) => {
        console.log("res",res);
        this.contracts = res.body;
        this.loading = false;
        this.numberOfContracts = this.contracts.length;
      }, (err: any) => {
        console.log(err);
        this.loading = false;
      }
    );

  }

  getCoachFeedbacks(coachId: any) {
    this.loading = true;
    this.clientService.getCoachFeedbacks(coachId).subscribe(
      (response: any) => {
        console.log(response);
        this.feedbacks = response.body;
        console.log(this.feedbacks);
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
        console.log(response);
        this.feedbacks = response.body;
        console.log(this.feedbacks);
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
        console.log('here=>', response);
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
        console.log(error);
      }
    );
  }

  // getOrgContracts(id: any) {
  //   this.loading = true;
  //   this.coachService.getContracts(id).subscribe(
  //     (response: any) => {
  //       console.log(response);
  //       this.contracts = response.body;
  //       this.loading = false;
  //       this.numberOfContracts = this.contracts.length;
  //     },
  //     (error: any) => {
  //       console.log(error);
  //     }
  //   );
  // }

  // getOrgClients() {
  //   const options = {
  //     page: 1,
  //     per_page: this.itemsPerPage,
  //     status: this.filters.status,
  //     search: this.filters.searchItem,
  //   };
  //   const id = this.user.id;
  //   this.loading = true;
  //   this.clientService.getOrgClients(id).subscribe(
  //     (response) => {
  //       this.loading = false;
  //       this.clients = response.body;
  //       console.log(response)
  //       console.log('clients', this.clients)
  //       this.numberOfClients = this.clients.length;

  //     }, (error) => {
  //       console.log(error)
  //     }
  //   )
  // }

  navigateToSessionView(id: any) {
    console.log(id);
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
        console.log('clients',this.clients)
      }, (error) => {
        this.loading = false;
        console.log(error)
      }
    )
  }

  navigateToTerms(id: any) {
    console.log("contractId on navigate", id);
    this.contractId = id;
    if (this.userRole == 'COACH') {

      this.router.navigate(['/contractDetail', id]);
    } else if (this.userRole == 'CLIENT' && id.contractStatus == null) {
      this.router.navigate(['/terms', id]);
    } else (this.userRole == 'CLIENT' && id.contractStatus == "SIGNED")
    this.router.navigate(['/contractDetail', id]);


  }
  // getOrganization(id: any) {
  //   const data = {
  //     superCoachId: id,
  //   }
  //   this.clientService.getOrganization(data).subscribe(
  //     (response: any) => {
  //       console.log('here Organization=>', response);
  //       this.Organization = response.body;
  //       console.log(this.Organization);
  //       console.log('here Organization=>', response);




  //       sessionStorage.setItem('Organization', JSON.stringify(this.Organization));


  //     },
  //     (error: any) => {
  //       console.log(error);
  //     }
  //   );
  // }


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
          this.coachesImpl = response.body;
          this.totalElements = +response.headers.get('X-Total-Count');
          console.log('coaches',this.coachesImpl)
        }, (error) => {
          this.loading = false;
          console.log(error)
        }
      )
  }
  getClientContracts(id: any) {
    // const data = {
    //   clientId: id,
    // }
    this.clientService.getContractsByClientId(id).subscribe(
      (response: any) => {
        console.log('here contracts=>', response);
        this.contracts = response.body;
        console.log(this.contracts);

      },
      (error: any) => {
        console.log(error);
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
    console.log("contractId on navigate", id);
    this.contractId = id;
    this.router.navigate(['/contractDetail/' + id]);
  }

  navigateToCoachView(id : number) {
    console.log("coachId on navigate", id);
    this.router.navigate(['/coachView/' + id]);
  }


  getUser() {
    this.user = JSON.parse(sessionStorage.getItem('user') as any);
    console.log(this.user);
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

        console.log(response);
        this.coachEducationData = response;
        this.calculateNumberOfHours(this.coachEducationData);
      }, (error: any) => {
        console.log(error);
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
        console.log(response);
        this.sessions = response.body;
        console.log(this.sessions.body);
        this.numberOfSessions = this.sessions.length;
      },
      (error: any) => {
        console.log(error);
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
