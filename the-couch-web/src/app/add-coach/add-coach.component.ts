import { Component, OnInit } from '@angular/core';
import { LoginService } from '../services/LoginService';
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
    userRole: 'ORGANIZATION',
    firstName: '',
    lastName: '',
    businessName: '',
    msisdn: '',
    email: '',
    password: '',
    passwordConfirm: '',
    orgIdId: '',
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
  coachSessionData:any;
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
  
      if(this.userRole == 'ORGANIZATION'){
    
      }
    }
    
    validateEmail(): void {
      /\S+@\S+\.\S+/.test(this.formData.email)
        ? ((this.emailInvalid = false), this.validatePassword())
        : (this.emailInvalid = true);
    }
    validatePassword() {
      if (this.formData.password === this.formData.passwordConfirm) {
        this.passwordInvalid = false;
        this.signUp();
      } else {
        this.passwordInvalid = true;
      }
    }

    signUp() {
      this.errorMessage = '';
console.log(this.orgData.id)
console.log(this.orgData.orgName)
console.log("form data here",this.formData)
      this.formData.orgIdId = this.orgData.id;
      this.formData.businessName = this.orgData.orgName;

      console.log(this.formData)
      this.registrationSuccess = false;
      this.LoginService.signUp(this.formData).subscribe({
        next: (response) => {
          console.log(response);
          console.log('here');
  
          
          if (response.body.error) {
            this.errorMessage = response.body.message;
            console.log('here');
          } else {
            this.registrationSuccess = true;
            this.router.navigate(['registration/confirm']);
            this.toastrService.success(
             'Confirm account',
             'Registration successfull!'
            );
  
            console.log('here');
          }
        },
        error: (error: any) => {
          // console.log(error);
          this.errorMessage = error.error.message ?? error.error.error.message;
          // this.toastrService.error('Please retry', 'Registration failled!');
        },
      });
    }

    toggleFieldTextType() {
      this.fieldTextType = !this.fieldTextType;
    }

  

}


