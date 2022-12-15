import { Component, OnInit } from '@angular/core';
import { ClientService } from '../services/ClientService';
import { ApiService } from '../services/ApiService';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-client-view',
  templateUrl: './client-view.component.html',
  styleUrls: ['./client-view.component.css']
})
export class ClientViewComponent implements OnInit {
contracts:any;
sessions:any;
clients:any;
clientid:any;
  console: any;
  itemsPerPage = 20;
  filters: any = {
    status: '',
    searchItem: '',
  };
  constructor(private clientService:ClientService,private restApiService:ApiService,private router:Router) { }

  ngOnInit(): void {
    this.getAllContracts();
    this.getAllSessions();
    this.getClients();
  }
getAllContracts(){
  this.clientService.getContracts().subscribe(
    (response: any) => {
      console.log(response)
      this.contracts = response
    }, (error: any) => {
      console.log(error)
    }
  )
}
getAllSessions(){
  this.restApiService.getSessions().subscribe(
    (response: any) => {
      console.log(response)
      this.sessions = response
    }, (error: any) => {
      console.log(error)
    }
  )
}
navigateToSessionView(id: any) {
  console.log(id)
  this.router.navigate(['sessionView', id]);

}
getClients(){
  const options = {
    page: 1,
    per_page: this.itemsPerPage,
    status: this.filters.status,
    search: this.filters.searchItem,
  };
  this.clientService.getClient(options).subscribe(
    (response) => {
      console.log(response)
      this.clients = response
    }, (error) => {
      console.log(error)
    }
  )
}

getclient() {

  this.clientService.getOneClient(this.clientid).subscribe((res: any) => {
    this.console.log('here');
  });
}
}
