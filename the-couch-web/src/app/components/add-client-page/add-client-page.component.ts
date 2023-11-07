import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { ClientService } from '../../services/ClientService';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-add-client-page',
  templateUrl: './add-client-page.component.html',
  styleUrls: ['./add-client-page.component.css'],
})
export class AddClientPageComponent implements OnInit {
  firstName: any;
  lastName: any;
  coachData: any;
  Org: any;
  orgData: any;
  userRole: any;
  OrgCoaches: any;

  selectedCoachId: any;

  addClient = {
    firstName: ' ',
    lastName: ' ',
    clientType: '',
    msisdn: ' ',
    email: ' ',
    physicalAddress: '',
    profession: ' ',
    paymentMode: ' ',
    reason: '',
    id: '',
    createdBy: '',
    status: '',
    password: '',
    coachId: '',
    organizationId: '',

  };
  orgId: any;
  coachSessionData: any;
  coachId: any;

  constructor(
    private ClientService: ClientService,
    private router: Router,
    private formbuilder: FormBuilder,
    private toastrService: ToastrService  
  ) {}

  ngOnInit(): void {
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    this.userRole = this.coachData.userRole;

    if(this.userRole == 'ORGANIZATION'){
      this.orgId = this.coachData.organization.id;
      this.getOrgCoaches(this.orgId);
    }
    this.coachId = this.coachData.id;



  }

  back() {
    window.history.back();
  }


  getOrgCoaches(id: any) {
    const options = {
      orgId: this.orgId,
    }
    this.ClientService.getOrgCoaches(options).subscribe(
      (response: any) => {
        this.OrgCoaches = response.body;
      },
      (error: any) => {
        this.toastrService.error('Error getting coaches', 'Error!');
      }
    );
  }

  selectedCoach(event: any) {
    this.selectedCoachId = event.target.value;
  }

  
  // Client = {};
  newClient() {
    var details = this.addClient;
    details.createdBy = this.coachData.fullName;
    details.status = 'NEW';
    details.password = '12345678';
   
    
  if(this.userRole == 'ORGANIZATION'){
    details.organizationId = this.orgId;
    details.coachId = this.selectedCoachId;

  } else if (this.userRole == 'COACH'){
    details.organizationId = this.coachData?.organization?.id;
    details.coachId = this.coachData.id;
  }

    this.ClientService.addClient(details).subscribe(
      (response: any) => {
        this.toastrService.success('Client added!', 'Success!');
        // this.router.navigate(['/clients']);
        this.back();
      },
      error => {
        this.toastrService.error
      }
    );
  }
}