import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ClientService } from '../services/ClientService';
import { Router } from '@angular/router';
import { ApiService } from '../services/ApiService';

@Component({
  selector: 'app-add-objective',
  templateUrl: './contract.component.html',
  styleUrls: ['./contract.component.css']
})
export class contractComponent implements OnInit{
  contractForm!: FormGroup;
  clients: any;
  sessions:any;
  contracts:any;
  numberOfClients!: number;
  numberOfSessions!:number;
  numberOfContracts!:number;
  rightIcon: any;
  objectives: string[] = [];
  itemsPerPage = 20;
  filters: any = {
    status: '',
    searchItem: '',
  };
  coachSessionData: any;
  coachData: any;

  //Add Objective Form
  Objectives = {
    objective: ''
  };
coachingCategory: any;

  constructor(private clientService : ClientService,
    private apiService:ApiService,
    private formBuilder:FormBuilder, private router:Router) { }

  ngOnInit(): void {
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);


    this.contractForm = this.formBuilder.group(
      {
        coachingCategory:'',
        coachingTopic:'',
        clientId:'',
        startDate:'',
        endDate:'',
        groupFeesPerSession:'',
        individualFeesPerSession:'',
        noOfSessions:'',
        objectives:'',
      }
    );
    this.getClients();
    this.getNoOfSessions();
    this.getNoOfContracts();
    this.addContract();
    this.coachingCategory();
    this.coachingCategory=this.contractForm
  }
  getClients(){
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
    };
    this.clientService.getClient(options).subscribe(
      (response: any) => {
        console.log(response.body.data)
        this.clients = response.body.data;
        this.numberOfClients = this.clients.length;
        console.log(this.numberOfClients)
      }, (error: any) => {
        console.log(error)
      }
    )
  }

  getNoOfSessions(){
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
    };
    this.clientService.getSessions(options).subscribe(
      (response:any) =>{
        console.log(response)
        this.sessions = response
        this.numberOfSessions = this.sessions.length;
        console.log(this.numberOfSessions)
        
      },
      (error: any) => {
        console.log(error)
      }
    )
  }
  getNoOfContracts(){
    this.clientService.getContracts().subscribe(
      (response:any) =>{
        console.log(response)
        this.contracts = response
        this.numberOfContracts = this.contracts.length;
        console.log(this.numberOfContracts)
        
      },
      (error: any) => {
        console.log(error)
      }
    )
  }
  addContract(){
    console.log('here');
    console.log(this.contractForm.value);
    var data = this.contractForm.value;
    data.coachId = this.coachData.id;
    data.objectives = this.objectives;
    console.log(data);
    this.apiService.addNewContract(data).subscribe(
      (response: any) => {
        
        this.router.navigate(['/contracts']);
      }, (error: any) => {
        console.log(error)
      }
    )
  }
  addObjective(){
    
    console.log(this.Objectives);
    this.objectives.push(this.Objectives.objective);
    console.log(this.objectives);
    this.Objectives.objective = '';
  }

  removeObjective(index: number){
    this.objectives.splice(index, 1);
  }

}
