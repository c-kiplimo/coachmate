import { Component, OnInit } from '@angular/core';
import { CoachService } from 'src/app/services/CoachService';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import {
  faChevronLeft,
  faEye,
  faEyeSlash,
} from '@fortawesome/free-solid-svg-icons';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-add-coach',
  templateUrl: './add-coach.component.html',
  styleUrls: ['./add-coach.component.css']
})
export class AddCoachComponent implements OnInit {
  firstName: any;
  lastName: any;
  coachData: any;
  org: any;
  orgData: any;
  userRole: any;
  coaches: any;

  selectedCoachId: any;

  addCoach = {
    firstName: ' ',
    lastName: ' ',
    msisdn: ' ',
    email: ' ',
    physicalAddress: '',
    reason: '',
    id: '',
    coachId: '',
    createdBy: '',
    status: '',
    password: '',
    organizationId: '',

  };
  orgId: any;
  organizationSessionData: any;

  constructor(
    private coachService: CoachService,
    private router: Router,
    private formbuilder: FormBuilder,
    private toastrService: ToastrService  
  ) {}
  ngOnInit(): void {
    this.organizationSessionData = sessionStorage.getItem('user');
    this.orgData = JSON.parse(this.organizationSessionData);
    console.log(this.coachData);
    this.userRole = this.orgData.userRole;
    console.log(this.userRole);
    console.log('user role=>', this.userRole);
    console.log('organization data=>', this.orgData);

    if (this.userRole == 'ORGANIZATION') {
      this.orgId = this.orgData.organization.id;
      this.getCoaches(this.orgId);
      console.log('org id=>', this.orgId);
    }
  }
  validateEmail(email: any) {
    const re = /\S+@\S+\.\S+/;
    return re.test(email);
  }
  getCoaches(id: any) {
    const options = {
      orgId: this.orgId,
    }
    this.coachService.getCoaches(options).subscribe(
      (response: any) => {
        console.log('here Organization=>', response);
        this.coaches = response;
        console.log(this.coaches);
      },
      (error: any) => {
        console.log(error);
      }
    );
  }


  newCoach() {
    var details = this.addCoach;

    details.status = 'NEW';
    details.password = '12345678';

   
    
  if(this.userRole == 'ORGANIZATION'){
    details.organizationId = this.orgId;
    details.coachId = this.selectedCoachId;

  } 
    console.log(details);

    console.log('add coach form=>', details);
    this.coachService.addCoach(details).subscribe(
      (response: any) => {
        console.log(response);
        this.toastrService.success('Coach added!', 'Success!');
        this.router.navigate(['/coaches']);
      },
      error => {
        console.log('error here',error);
        this.toastrService.error
      }
    );
  }

 



}

