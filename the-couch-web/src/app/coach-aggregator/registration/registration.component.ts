import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { faChevronLeft, faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';
import { ToastrService } from 'ngx-toastr';
import { LoginService } from '../../services/LoginService';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {
  fieldTextType!: boolean;
  eyeIcon = faEye;
  eyeSlashIcon = faEyeSlash;
  backIcon = faChevronLeft;

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
  registrationSuccess = false;
  emailInvalid = false;
  passwordInvalid = false;
  errorMessage = '';

  constructor(  private http: HttpClient,
    private router: Router,
    private service: LoginService,
    private toastrService: ToastrService) { }

  ngOnInit(): void {
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
    console.log(this.formData);
    this.errorMessage = '';
    this.registrationSuccess = false;
    this.service.signUp(this.formData).subscribe({
      next: (response: { body: { error: any; message: string; }; }) => {
        console.log(response);
        console.log('here');
        if (response.body.error) {
          this.errorMessage = response.body.message;
          console.log('here');
        } else {
          this.registrationSuccess = true;
          this.router.navigate(['/registration/confirm']);
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
        this.toastrService.error('Please retry', 'Registration failled!');
      },
    });
  }
  toggleFieldTextType() {
    this.fieldTextType = !this.fieldTextType;
  }
}
//

