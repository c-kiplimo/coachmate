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


  // Client = {};
  newClient() {
    var details = this.addClient;
    details.createdBy = this.coachData.fullName;
    details.coach_id = this.coachData.id;
    details.status = 'NEW';
    details.password = '12345678';
   
    
  if(this.userRole == 'ORGANIZATION'){
    details.org_id_id = this.orgData.id;
  }

    
    console.log(details);

    console.log('add client form=>', details);
    this.ClientService.addClient(details).subscribe(
      (response: any) => {
        console.log(response);
        this.toastrService.success('Client added!', 'Success!');
        this.router.navigate(['/clients']);
      }
    );
  }
}
