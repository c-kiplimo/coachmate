import { Component, OnInit } from '@angular/core';
import { LoginService } from '../services/LoginService';
import { Router } from '@angular/router';
// import { ToastrService } from 'ngx-toastr';
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
    userRole: 'COACH',
    firstName: '',
    lastName: '',
    businessName: '',
    msisdn: '',
    email: '',
    password: '',
    passwordConfirm: '',
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

  
  constructor(private LoginService: LoginService, private router: Router) { }

  ngOnInit(): void {
    this.SessionData = sessionStorage.getItem('user'); 
    this.Data = JSON.parse(this.SessionData);
    console.log(this.Data);

    this.OrgSession = sessionStorage.getItem('Organization'); 
    this.orgData = JSON.parse(this.OrgSession);
    console.log(this.orgData);


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
      console.log(this.formData)
      this.registrationSuccess = false;
      this.LoginService.signUp(this.formData).subscribe({
        next: (response) => {
          console.log(response);
          console.log('here');
  
          this.registrationSuccess = true;
          this.router.navigate(['registration/confirm']);
          
          if (response.body.error) {
            this.errorMessage = response.body.message;
            console.log('here');
          } else {
            this.registrationSuccess = true;
            this.router.navigate(['registration/confirm']);
            // this.toastrService.success(
            //   'Confirm account',
            //   'Registration successfull!'
            // );
  
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


