import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ClientService } from '../../services/ClientService';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '../../services/ApiService';
import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { style, animate, transition, trigger } from '@angular/animations';
@Component({
  selector: 'app-add-objective',
  templateUrl: './contract.component.html',
  styleUrls: ['./contract.component.css'],
  animations:[
    trigger('fadeIn', [
      transition(':enter', [
        // :enter is alias to 'void => *'
        style({ opacity: 0 }),
        animate(600, style({ opacity: 1 })),
      ]),
    ]),
  ],
})
export class AddContractComponent implements OnInit{
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
  editingSettings = false;
  isSaving = false;
  disableButton = false;
  saveSuccess = false;
contractTemplates: any;
  user: any;
  contractTemplate: any;

  constructor(private clientService : ClientService,
    private apiService:ApiService,
    private formBuilder:FormBuilder,
    private router:Router,
    private route: ActivatedRoute
    ) { }

  ngOnInit(): void {
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    this.user = this.coachData;
    console.log(this.user);
    this.contractTemplates = this.user.contractTemplate;
    console.log(this.contractTemplates);
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
        service:'',
        practice:'',
        note:'',
        terms_and_conditions:'',
     
      }
    );
    this.coachingCategory=this.contractForm
  }
  resetTemplate(template: string): void {
    this.contractTemplates[template] = this.user.coach.contractTemplates[template];
  }

  setFields(): void {
    this.contractTemplate = JSON.parse(
      sessionStorage.getItem('contractTemplate') || '{}'
    );
    console.log(this.contractTemplate);
  }
  toggleEditingSettings(): void {
    this.editingSettings = !this.editingSettings;
    this.disableButton = true;
    this.setFields();

    setTimeout(() => {
      this.disableButton = false;
    }, 2000);
  }
  getClients(){
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
    };
    this.clientService.getClients(options).subscribe(
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
    this.contractTemplates = this.user.contractTemplate;
    console.log(this.contractTemplates);
    console.log('here');
    console.log(this.contractForm.value);
    var data = this.contractForm.value;
    data.coachId = this.coachId
    data.organizationId = this.organizationId
    data.objectives = this.objectives;
    data.service = this.contractTemplates.serviceTemplate;
    data.practice = this.contractTemplates.practiceTemplate;
    data.note = this.contractTemplates.noteTemplate;
    data.terms_and_conditions = this.contractTemplates.terms_and_conditionsTemplate;
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
