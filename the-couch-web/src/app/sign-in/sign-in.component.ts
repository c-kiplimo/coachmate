import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { LoginService } from '../services/LoginService';
import { Router, ActivatedRoute } from '@angular/router';
import {
  faArrowLeft,
  faArrowRight,
  faChevronLeft,
  faEye,
  faEyeSlash,
  faSign,
} from '@fortawesome/free-solid-svg-icons';
import { ToastrService } from 'ngx-toastr';
// import { faEyeSlash } from '@fortawesome/free-regular-svg-icons';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css'],
})
export class SignInComponent implements OnInit {
  errorMessage = '';
  loginSuccess = false;
  fieldTextType!: boolean;

  formData = {
    username: '',
    password: '',
  };
  eyeIcon = faEye;
  eyeSlashIcon = faEyeSlash;
  loginIcon = faSign;
  loginForm!: FormGroup;
  backIcon = faChevronLeft;
  token!: string;
  businessName!: string;
  constructor(
    private LoginService: LoginService, 
    private router: Router,
    private toastrService: ToastrService
    ) {}

  ngOnInit(): void {}

  userLogin() {
    this.errorMessage = '';
    this.loginSuccess = false;
    this.LoginService.login(this.formData).subscribe({
      next: (response) => {
        console.log(response);
        if (response.error) {
          this.errorMessage = response.body.message;
        } else {
          this.loginSuccess = true;
          const data = response;
          this.token = data.token;
          console.log(this.token);
          if (data.user.coach === null) {
            this.businessName = data.user.fullName;
          } else{
            this.businessName = data.user.coach.businessName;
          }
          console.log(this.businessName);

          sessionStorage.setItem('token', this.token);
          sessionStorage.setItem('businessName', this.businessName);
          sessionStorage.setItem('user', JSON.stringify(data.user));
          sessionStorage.setItem('notificationSettings', JSON.stringify(data.user.notificationSettings));
          
          this.router.navigate(['dashboard']);

          // setTimeout(() => {
          //   location.reload();
          // }, 4);
          this.toastrService.success(
            'You are loggged in',
            'Login successfull!'
          );
          console.log('here')

        }
      },
      error: (error: any): any => {
        this.toastrService.error(
          'wrong Username/Password',
          'Failled to log in'
        );

        return (this.errorMessage = error.error.message);
      },
    });
  }

  toggleFieldTextType() {
    this.fieldTextType = !this.fieldTextType;
  }
}
