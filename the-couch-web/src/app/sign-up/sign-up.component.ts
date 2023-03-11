import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { LoginService } from '../services/LoginService';
import { Router } from '@angular/router';
 import { ToastrService } from 'ngx-toastr';
 import intlTelInput from 'intl-tel-input';
import 'intl-tel-input/build/js/utils';
import {
  faChevronLeft,
  faEye,
  faEyeSlash,
} from '@fortawesome/free-solid-svg-icons';
import { fromEvent, map, debounceTime, distinctUntilChanged } from 'rxjs';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css'],
})
export class SignUpComponent implements OnInit {
  formData = {
    userRole: '',
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
  passwordErrorMessage = '';

  @ViewChild('yourElement') yourElement!: ElementRef;
  isPasswordActive!: boolean;
  iti!: intlTelInput.Plugin;

  constructor(private LoginService: LoginService, private router: Router,private toastrService :ToastrService ) {}

  ngOnInit(): void {
    const input = document.querySelector("#phone");

  }
  ngAfterViewInit(): void {
    fromEvent(this.yourElement.nativeElement, 'input')
      .pipe(map((event: any) => (event.target as HTMLInputElement).value))
      .pipe(debounceTime(2000))
      .pipe(distinctUntilChanged())
      .subscribe((data) => this.validatePasswordRule());
  }

  validateEmail(): void {
    /\S+@\S+\.\S+/.test(this.formData.email)
      ? ((this.emailInvalid = false), this.validatePassword())
      : (this.emailInvalid = true);
  }

  onPasswordFocus() {
    this.isPasswordActive = true;
  }

  onPasswordBlur() {
    this.isPasswordActive = false;
  }
  validatePasswordRule() {
    const REGEXP =
      /^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$/;
    const upperCase = /[A-Z]/g;

    const lowerCase = /[a-z]/g;

    const specialCharacters = /[-._!"`'#%&,:;<>=@{}~\$\(\)\*\+\/\\\?\[\]\^\|]+/;

    const digit = /[0-9]/g;

    if (this.formData.password.length < 7) {
      this.passwordInvalid = true;
      this.passwordErrorMessage = 'Password should be atleast 8 characters';
    } else if (!upperCase.test(this.formData.password)) {
      this.passwordInvalid = true;
      this.passwordErrorMessage = 'Password should contain uppecase characters';
    } else if (!lowerCase.test(this.formData.password)) {
      this.passwordInvalid = true;
      this.passwordErrorMessage =
        'Password should contain lowercase characters';
    } else if (!specialCharacters.test(this.formData.password)) {
      this.passwordInvalid = true;
      this.passwordErrorMessage = 'Password should contain special characters';
    } else if (!digit.test(this.formData.password)) {
      this.passwordInvalid = true;
      this.passwordErrorMessage = 'Password should contain atleat one digit';
    } else {
      this.passwordInvalid = false;
      this.passwordErrorMessage = '';
    }
  }
  validatePassword() {
    if(this.formData.password!=null && this.formData.passwordConfirm!=null){
      // validate password rule
      //check if pass
      this.validatePasswordRule();
      if (this.passwordInvalid){
          return;
      }else if(this.formData.password != this.formData.passwordConfirm){
        this.passwordInvalid=true;
        this.passwordErrorMessage="Passwords don't match";
        return;

      }else if (this.formData.password.length>7) {
        this.passwordInvalid = false;     
        this.signUp();  
      }

  }else{
    this.passwordInvalid = true;
    this.passwordErrorMessage = "Password Fields cannot be empty";

  }
  }
  signUp() {
    sessionStorage.setItem('formData', JSON.stringify(this.formData));
    this.errorMessage = ''
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
          this.toastrService.success(
            'Confirm account',
            'Registration successfull!'
          );

          console.log('here');
        }
      },
      error: (error: any) => {
        console.log(error);
        this.errorMessage = error.error.message ?? error.error.error.message;
        this.toastrService.error('Please retry', 'Registration failled!');
      },
    });
  }
  toggleFieldTextType() {
    this.fieldTextType = !this.fieldTextType;
  }
}
