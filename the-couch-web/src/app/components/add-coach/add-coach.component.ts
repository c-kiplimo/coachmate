import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../services/LoginService';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import {
  faChevronLeft,
  faEye,
  faEyeSlash,
} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-add-coach',
  templateUrl: './add-coach.component.html',
  styleUrls: ['./add-coach.component.css']
})
export class AddCoachComponent implements OnInit {
  formData = {
    firstName: '',
    lastName: '',
    msisdn: '',
    email: '',
  };

  fieldTextType!: boolean;
  eyeIcon = faEye;
  eyeSlashIcon = faEyeSlash;
  backIcon = faChevronLeft;
  registrationSuccess = false;
  emailInvalid = false;
  passwordInvalid = false;
  errorMessage = '';
  SessionData: any;
  Data: any;
  OrgSession: any;
  orgData: any;
  toastrService: any;
  coachSessionData: any;
  userRole: any;
  coachData: any;
  orgId: any;


  constructor(private LoginService: LoginService, private router: Router, toastrService: ToastrService
  ) { }
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

    }
  }
  validateEmail(email: any) {
    const re = /\S+@\S+\.\S+/;
    return re.test(email);
  }


  signUp() {
    this.errorMessage = '';
    this.orgId = this.coachData.id;
    console.log('organization id =>', this.orgId);
    console.log("form data here", this.formData)
    console.log(this.formData)
    this.registrationSuccess = false;
    this.LoginService.registerCoach(this.formData).subscribe({
      next: (response) => {
        console.log(response);
        this.registrationSuccess = true;
        this.toastrService.success('Coach added successfully');
        this.router.navigate(['']);
      },
      error: (error) => {
        console.log(error);
        this.errorMessage = error.error.message;
        this.toastrService.error(this.errorMessage);
      }
    });
  }

  toggleFieldTextType() {
    this.fieldTextType = !this.fieldTextType;
  }



}


