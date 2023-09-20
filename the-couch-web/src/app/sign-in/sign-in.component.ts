import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { LoginService } from '../services/LoginService';
import { Router, ActivatedRoute } from '@angular/router';
import { style, animate, transition, trigger } from '@angular/animations';


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
  styleUrls: ['./sign-in.component.scss'],
  animations: [
    trigger('fadeIn', [
      transition(':enter', [
        // :enter is alias to 'void => *'
        style({ opacity: 0 }),
        animate(300, style({ opacity: 1 })),
      ]),
    ]),
  ],
})

export class SignInComponent implements OnInit {
  errorMessage = '';
  loginSuccess = false;
  fieldTextType!: boolean;
  loading = false;

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

  ngOnInit(): void {
        //get session storage of the loged in user
        let user = sessionStorage.getItem("user");
        if (user != null) {
          this.router.navigate(['dashboard']);
        }
  }
  back() {
    window.history.back();
  }

  userLogin() {
    this.errorMessage = '';
    this.loginSuccess = false;
    this.loading = true;
    this.LoginService.login(this.formData).subscribe(
      (response: any) => {
        console.log(response);
          this.loading = false;
          sessionStorage.setItem('token', response.token);
          sessionStorage.setItem('businessName',  response.user.businessName);
          sessionStorage.setItem('user', JSON.stringify(response.user));
          //sessionStorage.setItem('notificationSettings', JSON.stringify(response.user.notificationSettings));
        
          this.loading = false;
          window.location.reload();
          this.router.navigate(['dashboard']);
          this.toastrService.success(
            'You are loggged in'
          );
      },
      (error: any): any => {
        this.loading = false;
        this.toastrService.error(
          'wrong Username/Password',
          'Failled to log in'
        );
        return (this.errorMessage = error.error.message);
      },
    );
  }



  toggleFieldTextType() {
    this.fieldTextType = !this.fieldTextType;
  }
}
