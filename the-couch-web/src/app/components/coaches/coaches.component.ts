import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { style, animate, transition, trigger } from '@angular/animations';
import { CoachService } from 'src/app/services/CoachService';
import { ClientService } from 'src/app/services/ClientService';

@Component({
  selector: 'app-coaches',
  templateUrl: './coaches.component.html',
  styleUrls: ['./coaches.component.css'],
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
export class CoachesComponent implements OnInit {

  coachId: any;
  editedCoach: any;  
  loading = false;
  itemsPerPage = 20;
  filters: any = {
    status: '',
    searchItem: '',
  };
  

  updateCoach!: FormGroup;


  constructor(
    private coachService: CoachService, 
    private ClientService: ClientService,
    private router: Router,
    private toastrService: ToastrService,
    private formbuilder: FormBuilder,) { }
  
  coaches!: any;
  all_coaches!: any;

  coachToBeUpdated!: any;
  organizationSessionData: any;
  organizationData: any;
  showFilters = false;
  coachStatuses = ['ACTIVE', 'SUSPENDED', 'CLOSED',,'NEW']
  userRole: any;
  user: any;
  orgId!: number;
  page: number = 0;
  pageSize: number = 15;
  totalElements: any;

  ngOnInit(): void {

    this.organizationSessionData = sessionStorage.getItem('user'); 
    this.user = JSON.parse(this.organizationSessionData);
    this.userRole = this.user.userRole;
    console.log(this.userRole);
    

    if(this.userRole == 'ORGANIZATION'){
      this.orgId = this.user.id;
      console.log('ORGANIZATION');
      this.getCoaches(this.page);

    }

    this.updateCoach = this.formbuilder.group({
     
    firstName: ' ',
    lastName: ' ',
    coachType: ' ',
    msisdn: ' ',
    email: ' ',
    status: '',
    physicalAddress: ' ',
    profession: ' ',
    paymentMode: ' ',
    reason: '',

    });
    this.getCoaches(this.page);
  }
  getClass(client: any) {
    if (client.status === 'SUSPENDED') {
        return 'badge-warning';
    } else if (client.status === 'ACTIVE') {
        return 'badge-success';
    } else {
        return 'badge-danger';
    }
}


getCoaches(page: any) {
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
      orgId: this.orgId,
    };
    
    this.ClientService.getClients(options).subscribe(  // test the getAllOrgClients endpoint
      (response) => {
        this.loading = false;
        this.coaches = response.body;
        for (let coach of this.coaches) {
          if (coach.userRole != 'COACH') {
            this.coaches.splice(this.coaches.indexOf(coach), 1);
          }
        }
        this.totalElements = +response.headers.get('X-Total-Count');
        console.log('clients',this.coaches)
      }, (error) => {
        this.loading = false;
        console.log(error)
      }
    )
}

search() {
  this.page = 0;
  this.getCoaches(this.page);
}
filterByStatus() {
  this.page = 0;
  this.getCoaches(this.page);
}
toggleFilters() {
  this.showFilters = !this.showFilters;
}
resetStatuses(): void {
  this.filters.status = 'ALL';
  this.getCoaches(0);
}
  

  navigateToCoachView(id: any) {
    console.log(id)
    this.router.navigate(['/coachView', id]);
  }
  deleteCoach(client: any) {
 //To be checked

}
@ViewChild('editCoachModal', { static: false })
editCoachModal!: ElementRef;  
  editCoach(coach:any){
    this.coachToBeUpdated = coach;

    this.updateCoach = this.formbuilder.group({
      firstName: this.coachToBeUpdated.firstName,
      lastName: this.coachToBeUpdated.lastName,
      msisdn: this.coachToBeUpdated.msisdn,
      email: this.coachToBeUpdated.email,

      physicalAddress: this.coachToBeUpdated.physicalAddress,

      // profession: this.coachToBeUpdated.profession,
      // paymentMode: this.clientToBeUpdated.paymentMode,
      reason: this.coachToBeUpdated.reason,
    });
  
  }

  updateCoachDetails(id:any){
    this.coachToBeUpdated = this.updateCoach.value;
    console.log(this.coachToBeUpdated)
    console.log(id)  
    this.coachService.editCoach(this.coachToBeUpdated,id).subscribe(
      (data) => {
        console.log(data)
        this.toastrService.success('Coach Updated', 'Success!');
        setTimeout(() => {
          location.reload();
        }, 1000);
        this.editCoachModal.nativeElement.classList.remove('show');
        this.editCoachModal.nativeElement.style.display = 'none';

      }, (error) => {
        console.log(error)
      }
    );
  }

  suspendCoach(coach:any){
    this.coachService.suspendCoach(coach).subscribe(
      (response) => {
        this.getCoaches(this.page);
        this.loading = false;

      }, (error) => {
        console.log(error)
      }
    );
  }
  // filter clients by status
  filterCoachesByStatus(status: any) {
    this.filters.status = status;
    this.getCoaches(this.page);
  }
  onTableDataChange(event: any) {
    this.page = event;
    this.getCoaches(this.page);
  }
  
  
}

