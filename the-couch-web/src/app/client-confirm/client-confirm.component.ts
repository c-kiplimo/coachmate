import { Component, OnInit } from '@angular/core';
import {
  faChevronLeft,
  faEye,
  faEyeSlash,
} from '@fortawesome/free-solid-svg-icons';

import { LoginService } from '../services/LoginService';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-client-confirm',
  templateUrl: './client-confirm.component.html',
  styleUrls: ['./client-confirm.component.css']
})
export class ClientConfirmComponent implements OnInit {

  formData = {
    password: '',
    passwordConfirm: '',
  };

  passwordInvalid = false;
  errorMessage = '';
  fieldTextType!: boolean;
  eyeIcon = faEye;
  eyeSlashIcon = faEyeSlash;
  backIcon = faChevronLeft;

  constructor(
    private LoginService: LoginService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) { }

  id: any;
  token: any;
  ngOnInit(): void {
    this.id = this.activatedRoute.snapshot.paramMap.get('id');
    console.log(this.id);
    this.token = this.activatedRoute.snapshot.paramMap.get('token');
    console.log(this.token);
  }

  validatePassword() {
    console.log(this.formData)
    if (this.formData.password === this.formData.passwordConfirm) {
      this.passwordInvalid = false;
      this.errorMessage="";
      this.confirmAndUpdatePassword();
    } else {
      this.passwordInvalid = true;
      this.errorMessage="Passwords do not match";
    }
  }

  toggleFieldTextType() {
    this.fieldTextType = !this.fieldTextType;
  }
  confirmAndUpdatePassword() {
    this.errorMessage = '';
    console.log(this.formData)
    this.passwordInvalid = false;
    this.LoginService.confirmAndUpdateClientPassword(this.formData).subscribe({
      next: (response) => {
        console.log(response);
        console.log('here');
        if (response.body.error) {
          this.errorMessage = response.body.error;
        } else {
         
          this.router.navigate(['/login']);
        }
      },
      error: (error) => {
        console.log(error);
      },
    });
  }


}
