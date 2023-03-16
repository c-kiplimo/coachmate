import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { ClientService } from '../services/ClientService';
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
  couchSessionData: any;
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
    coach_id: '',
    status: '',
    password: '',
    org_id_id: '',
    coachId: '',

  };

  constructor(
    private ClientService: ClientService,
    private router: Router,
    private formbuilder: FormBuilder,
    private toastrService: ToastrService
  ) {}

  ngOnInit(): void {
     this.couchSessionData = sessionStorage.getItem('user');
     this.coachData = JSON.parse(this.couchSessionData)
    console.log(this.coachData);
    this.userRole = this.coachData.userRole;
    console.log(this.userRole);

    this.Org = sessionStorage.getItem('Organization');
    this.orgData = JSON.parse(this.Org);
    console.log(this.orgData);
    this.getOrgCoaches(this.orgData.id);



  }

  getOrgCoaches(id: any) {
    const data = {
      OrgId: id,
    }
    this.ClientService.getOrgCoaches(data).subscribe(
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

  selectedClient(event: any) {
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
    details.org_id_id = this.orgData.id;
    details.coach_id = this.selectedCoachId;
    details.coachId = this.selectedCoachId;

  } else if (this.userRole == 'COACH'){
    details.org_id_id = this.coachData.coach.orgIdId;
    details.coach_id = this.coachData.coach.id;
    details.coachId = this.coachData.coach.id;
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