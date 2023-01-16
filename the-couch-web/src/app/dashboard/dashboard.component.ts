import { Component, OnInit } from '@angular/core';
import { ClientService } from '../services/ClientService';
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit {
  // ClientService: any;
  clients: any;
  User: any;
  sessions: any;
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

  constructor(private clientService: ClientService) {}

  ngOnInit(): void {
    this.getClients();
    this.getUser();
    this.getNoOfSessions();
    this.getNoOfContracts();
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
  getNoOfSessions() {
    this.clientService.getSessions().subscribe(
      (response: any) => {
        console.log('here=>', response);
        this.sessions = response;
        this.numberOfSessions = this.sessions.length;
        let totalMinutes = 0;
        for (let i = 0; i < this.sessions.length; i++) {
          totalMinutes += Number(this.sessions[i].sessionDuration);

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
        console.log(response);
        this.contracts = response;
        this.numberOfContracts = this.contracts.length;
        console.log(this.numberOfContracts);
      },
      (error: any) => {
        console.log(error);
      }
    );
  }
  getUser() {
    this.User = JSON.parse(sessionStorage.getItem('user') as any);

    console.log(this.User.coach.businessName);
  }
}
