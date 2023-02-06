import { Component, OnInit } from '@angular/core';
import { ClientService } from '../services/ClientService';
import { CoachEducationService } from '../services/CoachEducationService';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit {
  // ClientService: any;
  clients: any;
  User: any;
  Organization: any;
  sessions: any;
  orgName: any;
  contracts: any;
  numberOfClients!: number;
  numberOfSessions!: number;
  numberOfContracts!: number;
  numberOfHours: any;
  numberOfMinutes: any;
  rightIcon: any;
  itemsPerPage = 20;
  filters: any = {
    status: '',
    searchItem: '',
  };
  coachEducationData: any;
  numberOfHoursCoachEducation: any;
  coachSessionData: any;
  coachData: any;
  userRole: any;

  contractId: any;
  loading!: boolean;

  OrgCoaches: any;
  numberofCoaches: any;
  

  

  constructor(
    private clientService: ClientService,
    private CoachEducationService: CoachEducationService,
    private route: Router,
    private router:ActivatedRoute
    ) {}

  ngOnInit(): void {
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);
    this.userRole = this.coachData.userRole;
    console.log(this.userRole);
    this.getAllContracts();
    this.router.params.subscribe((params: { [x: string]: any; }) => {
      const id = params['id'];
      // Retrieve the contract from the database using the id
      this.contracts = this.clientService.getContract(id);
    });

    if(this.userRole == 'COACH'){
    this.getClients();
    this.getUser();
    this.getNoOfSessions();
    this.getNoOfContracts();
    this.getCoachEducation(this.coachData.id);

    } else if(this.userRole == 'ORGANIZATION'){
      console.log('ORGANIZATION');
      this.getUserOrg();
     

     
    }else {
      console.log('not coach');
      this.getUser();
     
      this.getClientByEmail();
      
    }

  }
  getClients() {
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
    };
    this.clientService.getClient(options).subscribe(
      (response: any) => {
        console.log('here clients=>', response.body.data);
        this.clients = response.body.data;

        this.numberOfClients = this.clients.length;
        console.log(this.numberOfClients);
      },
      (error: any) => {
        console.log(error);
      }
    );
  }
  getOrganization(id: any) {
    const data = {
      superCoachId: id,
    }
    this.clientService.getOrganization(data).subscribe(
      (response: any) => {
        console.log('here Organization=>', response);
        this.Organization = response;
        this.orgName = this.Organization.orgName;

        this.getOrgCoaches(this.Organization.id);



        sessionStorage.setItem('Organization', JSON.stringify(this.Organization));
        

      },
      (error: any) => {
        console.log(error);
      }
    );
  }

  getOrgCoaches(id: any) {
    const data = {
      OrgId: id,
    }
    this.clientService.getOrgCoaches(data).subscribe(
      (response: any) => {
        console.log('here Organization=>', response);
        this.OrgCoaches = response;
        console.log(this.OrgCoaches);
        this.numberofCoaches = this.OrgCoaches.length;
       
      },
      (error: any) => {
        console.log(error);
      }
    );
  }
  getClientContracts(id: any) {
    // const data = {
    //   clientId: id,
    // }
    this.clientService.getClientContracts(id).subscribe(
      (response: any) => {
        console.log('here contracts=>', response);
        this.contracts = response;
        console.log(this.contracts);
      
      },
      (error: any) => {
        console.log(error);
      }
    );
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
  
        for (let i = 0; i < this.sessions.length; i++) {
          if (this.sessions[i].sessionStatus === 'CONFIRMED'){
          let startTime = this.sessions[i].sessionStartTime;
          let endTime = this.sessions[i].sessionEndTime;
        
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
      }
        
      },
      (error: any) => {
        console.log(error);
      }
    );
  }
  
  getNoOfContracts() {
    this.clientService.getContracts().subscribe(
      (response: any) => {
        
        this.contracts = response;
        console.log(this.contracts);
        this.numberOfContracts = this.contracts.length;
        console.log(this.numberOfContracts);
      },
      (error: any) => {
        console.log(error);
      }
    );
  }
  getAllContracts() {
    this.loading = true;
    this.clientService.getContracts().subscribe(
      (response: any) => {
        console.log(response);
        this.contracts = response;
        this.loading = false;
      },
      (error: any) => {
        console.log(error);
      }
    );
  }
  navigateToContractDetail(id: any) {
    console.log("contractId on navigate",id);
    this.contractId = id;
    this.route.navigate(['/contractDetail/' + id]);


  }


  getUser() {
    this.User = JSON.parse(sessionStorage.getItem('user') as any);
    console.log(this.User);
  }
  getUserOrg() {
    this.User = JSON.parse(sessionStorage.getItem('user') as any);
    console.log(this.User);
    this.getOrganization(this.User.id);

  }


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
        this.sessions = response;
        console.log(this.sessions);
        this.numberOfSessions = this.sessions.length;
      },
      (error: any) => {
        console.log(error);
      }
    );
  }
  getClass(session: any) {
    if (session.status === 'SUSPENDED') {
        return 'badge-warning';
    } else if (session.status === 'CONFIRMED') {
        return 'badge-success';
    }
    else if (session.status === 'NEW'){
      return 'badge-success'
    }
    else {
        return 'badge-success';
    }
}

  getClientByEmail() {
    const email = {
      email: this.User.email
    }
    this.clientService.getClientByEmail(email).subscribe(
      (response: any) => {
        console.log(response);
        this.clients = response;
        this.numberOfClients = this.clients.length;
        this.getClientSessions(response[0].id);

        this.getClientContracts(response[0].id);
      },
      (error: any) => {
        console.log(error);
      }
    );
  }
}
