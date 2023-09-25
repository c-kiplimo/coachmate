import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
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
editingSettings = false;


  constructor(private clientService : ClientService,
    private apiService:ApiService,
    private contractService:ContractsService,
    private formBuilder:FormBuilder,
    private router:Router,
    private route: ActivatedRoute,
    private toastrService: ToastrService
    ) { 
      // this.contractForm = this.formBuilder.group(
      //   {
      //     coachingCategory:'',
      //     coachingTopic:'',
      //     clientId:'',
      //     // coachId: '',
      //     startDate:['', Validators.required, this.pastDateValidator],
      //     endDate: ['', Validators.required, this.pastDateValidator],
      //     groupFeesPerSession:'',
      //     individualFeesPerSession:'',
      //     noOfSessions:'',
      //     objectives:'',
      //     services:'',
      //     practice:'',
      //     note:'',
      //     terms_and_conditions:'',
       
      //   }
      // );
    }

  ngOnInit(): void {
   // this.contractTemplates = this.user.contractTemplate;
   this.coachSessionData = sessionStorage.getItem('user');
   this.user = JSON.parse(this.coachSessionData);

   this.userRole = this.user.userRole;

   if (this.userRole == 'ORGANIZATION') {
     this.orgId = this.user.organization.id;
     this.getClients(this.page);
     this.getOrgCoaches(this.page);

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
        // coachId: '',
        startDate:['', [Validators.required, this.pastDateValidator()]],
        endDate: ['', [Validators.required, this.pastDateValidator()]],
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

  pastDateValidator() {
    return (control: any) => {
      const selectedDate = new Date(control.value);
      const currentDate = new Date();

      // Check if the selected date is in the past
      if (selectedDate < currentDate) {
        return { pastDate: true };
      }

      return null;
    };
  };

  resetTemplate(template: string): void {
    this.contractTemplates[template] = this.user.coach.contractTemplates[template];
  }

  back() {
    window.history.back();
  }

  setFields(): void {
    this.contractTemplate = JSON.parse(
      sessionStorage.getItem('contractTemplate') || '{}'
    );
  }
  toggleEditingSettings(target: string): void {
    const isServiceSection = target === 'collapseService';
    const isTermsSection = target === 'collapseTerms';
    const isPracticeSection = target === 'collapsePractice';
  
    if ((isServiceSection || isTermsSection || isPracticeSection) && !this.editingSettings) {
      this.editingSettings = true;
    } else if ((isServiceSection || isTermsSection || isPracticeSection) && this.editingSettings) {
      this.editingSettings = false;
    }
  
    this.setFields();
  
    this.disableButton = true;
  
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
      }, (error) => {
        this.loading = false;
        this.toastrService.error('Error getting clients', error.message);
      }
    )
  }

  getOrgCoaches(page: any) {
    this.loading = true;
      this.page = 0;
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
          this.OrgCoaches = response.body;
          this.totalElements = +response.headers.get('X-Total-Count');
        }, (error) => {
          this.loading = false;
          this.toastrService.error('Error getting coaches', error.message);        }
      );
  }

  getNoOfContracts(){
    this.loading = true;
    this.clientService.getContracts().subscribe(
      (response:any) =>{
        this.contracts = response
        this.numberOfContracts = this.contracts.length;        
      },
      (error: any) => {
        this.toastrService.error('Error getting contracts', error.message);
        this.loading = false;
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
        this.numberOfClients = this.clients.length;
  
      }, (error) => {
        this.toastrService.error('Error getting clients', error.message);
      }
    )
  }
  
  addContract(){
    //this.contractTemplates = this.user.contractTemplate;
    var data = this.contractForm.value;
    data.organizationId = this.orgId
    data.coachId = this.coachId
    // if (this.userRole == 'COACH') {
    //   data.coachId = this.coachId
    // }
    
    //Stringify the objectives array
    let objectives = JSON.stringify(this.objectives);
    data.objectives = objectives;
    this.contractService.addNewContract(data).subscribe(
      (response: any) => {
        this.toastrService.success('Contract added successfully');
        window.history.back();
      }, (error: any) => {
        this.toastrService.error('Error adding contract', 'Failed');
      }
    )
  }

  addObjective(){
    
    this.objectives.push(this.Objectives.objective);
    this.Objectives.objective = '';
  }

  removeObjective(index: number){
    this.objectives.splice(index, 1);
  }
 
  reload() {
    location.reload();
  }

}
