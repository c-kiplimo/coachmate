import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ClientService } from '../../services/ClientService';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiService } from '../../services/ApiService';
import { ContractsService } from 'src/app/services/contracts.service';
import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { ToastrService } from 'ngx-toastr';
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
  editingSettings = false;
  isSaving = false;
  disableButton = false;
  saveSuccess = false;
contractTemplates: any;
  user: any;
  contractTemplate: any;

  orgId!: number;
  coachId!: number;

  page: number = 0;
  pageSize: number = 15;
  totalElements: any;
  loading = false;
  clientId: any;


  constructor(private clientService : ClientService,
    private apiService:ApiService,
    private contractService:ContractsService,
    private formBuilder:FormBuilder,
    private router:Router,
    private route: ActivatedRoute,
    private toastrService: ToastrService
    ) { }

  ngOnInit(): void {
   // this.contractTemplates = this.user.contractTemplate;
   this.coachSessionData = sessionStorage.getItem('user');
   this.user = JSON.parse(this.coachSessionData);

   this.userRole = this.user.userRole;
   console.log(this.userRole);


   if (this.userRole == 'ORGANIZATION') {
     this.orgId = this.user.organization.id;
     this.getClients(this.page);

   } else if (this.userRole == 'COACH') {
     this.coachId = this.user.id;
     this.getClients(this.page);

   } else if (this.userRole == 'CLIENT') {
     this.clientId = this.user.id;
   }

  
      // } else if(this.userRole == 'ORGANIZATION'){
      //   console.log('ORGANIZATION');
      //   this.organizationId = this.coachData.organization.id;
      //   console.log("organization id",this.organizationId);
      //   this.getOrgClients();
      //   this.getOrgCoaches(this.organizationId);
       
      // }
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
        services:'',
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
  getClients(page: number){
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

    if(this.userRole == 'COACH'){
      options.coachId = this.coachId;
    }else if(this.userRole == 'CLIENT'){
      options.clientId = this.clientId;
    }else if(this.userRole == 'ORGANIZATION'){
      options.orgId = this.orgId;
    }

    this.clientService.getClients(options).subscribe(
      (response) => {
        this.loading = false;
        this.clients = response.body;
        this.totalElements = +response.headers.get('X-Total-Count');
        console.log('clients',this.clients)
        
      }, (error) => {
        this.loading = false;
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
    //this.contractTemplates = this.user.contractTemplate;
    console.log(this.contractTemplates);
    console.log('here');
    console.log(this.contractForm.value);
    var data = this.contractForm.value;
    data.coachId = this.coachId
    data.organizationId = this.orgId
    //Stringify the objectives array
    let objectives = JSON.stringify(this.objectives);
    data.objectives = objectives;
    console.log(data);
    this.contractService.addNewContract(data).subscribe(
      (response: any) => {
        this.toastrService.success('Contract added successfully');
        window.history.back();
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
