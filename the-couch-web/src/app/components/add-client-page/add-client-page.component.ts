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
    console.log(this.coachData);
    this.userRole = this.coachData.userRole;
    console.log(this.userRole);
    console.log('user role=>', this.userRole);
    console.log('coach data=>', this.coachData);

    if(this.userRole == 'ORGANIZATION'){
      this.orgId = this.coachData.organization.id;
      this.getOrgCoaches(this.orgId);
      console.log('org id=>', this.orgId);
    }
    this.coachId = this.coachData.id;



  }

  getOrgCoaches(id: any) {
    const options = {
      orgId: this.orgId,
    }
    this.ClientService.getOrgCoaches(options).subscribe(
      (response: any) => {
        console.log('here Organization=>', response);
        this.OrgCoaches = response;
        console.log(this.OrgCoaches);
      },
      (error: any) => {
        console.log(error);
      }
    );
  }

  selectedCoach(event: any) {
    console.log(event.target.value);
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
    console.log(details);

    console.log('add client form=>', details);
    this.ClientService.addClient(details).subscribe(
      (response: any) => {
        console.log(response);
        this.toastrService.success('Client added!', 'Success!');
        this.router.navigate(['/clients']);
      },
      error => {
        console.log('error here',error);
        this.toastrService.error
      }
    );
  }
}