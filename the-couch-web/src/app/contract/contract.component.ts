import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ClientService } from '../services/ClientService';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '../services/ApiService';
import { IconProp } from '@fortawesome/fontawesome-svg-core';

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
  backIcon!: IconProp;
  userRole: any;
  organizationId: any;
  OrgCoaches: any;
  numberofCoaches: any;
  coachId: any;

  constructor(private clientService : ClientService,
    private apiService:ApiService,
    private formBuilder:FormBuilder,
    private router:Router,
    private route: ActivatedRoute
    ) { }

  ngOnInit(): void {
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);
    this.userRole = this.coachData.userRole;
    console.log(this.userRole);

    if(this.userRole == 'COACH'){
      this.getClients();
      this.coachId = this.coachData.id;
    
     
   
  
      } else if(this.userRole == 'ORGANIZATION'){
        console.log('ORGANIZATION');
        this.organizationId = this.coachData.organization.id;
        console.log("organization id",this.organizationId);
        this.getOrgClients();
        this.getOrgCoaches(this.organizationId);
       
      }
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
        console.log(response.body)
        this.clients = response.body.data;
        this.numberOfClients = this.clients.length;
        console.log(this.numberOfClients)
      }, (error: any) => {
        console.log(error)
      }
    )
  }
  getOrgCoaches(id: any) {
    const data = {
      orgId: id,
    }
    this.clientService.getOrgCoaches(data).subscribe(
      (response: any) => {
        console.log('here Organization=> coaches', response);
        this.OrgCoaches = response;
        console.log(this.OrgCoaches);
        console.log('here Organization=> coaches', response);
        this.numberofCoaches = this.OrgCoaches.length;
       
      },
      (error: any) => {
        console.log(error);
      }
    );
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
  getOrgClients(){
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
    };
    this.clientService.getOrgClients(this.organizationId).subscribe(
      (response) => {
        this.clients = response.body;
        console.log(response)
        console.log('clients',this.clients)
        this.numberOfClients = this.clients.length;
  
      }, (error) => {
        console.log(error)
      }
    )
  }
  addContract(){
    console.log('here');
    console.log(this.contractForm.value);
    var data = this.contractForm.value;
    data.coachId = this.coachId
    data.organizationId = this.organizationId
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
  back() {
    window.history.back();
  }
  reload() {
    location.reload();
  }

}
