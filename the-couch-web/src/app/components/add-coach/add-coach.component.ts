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
  coachSessionData: any;

  constructor(
    private coachService: CoachService,
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
    this.orgId = this.coachData.id;
    console.log('organization id =>', this.orgId);
    console.log('user role=>', this.userRole);
    console.log('coach data=>', this.coachData);

    if (this.userRole == 'ORGANIZATION') {
      this.orgId = this.coachData.organization.id;
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
    this.coachService.getOrgCoaches(options).subscribe(
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


