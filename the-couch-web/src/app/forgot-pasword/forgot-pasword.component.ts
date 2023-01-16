import { Component, OnInit } from '@angular/core';
import { faEye, faEyeSlash, faPlusSquare } from '@fortawesome/free-solid-svg-icons';
import { ApiService } from '../services/ApiService';  


@Component({
  selector: 'app-forgot-pasword',
  templateUrl: './forgot-pasword.component.html',
  styleUrls: ['./forgot-pasword.component.css']
})
export class ForgotPaswordComponent implements OnInit {
  eyeIcon = faEye;
  eyeSlashIcon = faEyeSlash;
  resetForm!:any;
  fieldTextType!: boolean;
  // Get code view
  getCodeView=true;
  errorMessage='';
  getCodeErrorMessage='';

  // reset code view
  passwordResetSuccessMessage='';
  errorResetMessage = '';
  resetCodeDefault=false;
  passwordResetSuccess=false;

  formData = {
    msisdn:'',
    password:'',
    otp:'',
    confirmPassword:''
  }
  passwordInvalid=false;


  constructor(private service: ApiService) { }

  ngOnInit(): void {
  }
  validatePassword() {
    if (this.formData.password === this.formData.confirmPassword) {
      this.passwordInvalid = false;
      this.passwordReset();
    } else {
      this.passwordInvalid = true;
    }
  }
  getCode(){
    const options ={
      msisdn: this.formData.msisdn,
    }
    this.service.getResetCode(options).subscribe({
      next: (response:any) => {
        if (response.body.error) {
          this.getCodeView = true;
          this.errorMessage = '';
          this.getCodeErrorMessage = response.body.message;
         
        } else {
          this.getCodeView = false;
          this.resetCodeDefault = true;
          this.getCodeErrorMessage = '';
          this.errorMessage = response.body.message;

        }
      },
      error: (error: any) => {
        this.getCodeView = true;
        this.errorMessage = '';
        this.getCodeErrorMessage = error.error.message ?? error.error.error.message;
      },
    });

  }


passwordReset(){
  this.service.setNewPassword(this.formData).subscribe({
    next: (response:any) => {
      if (response.body.error) {
        this.resetCodeDefault=true;
        this.errorMessage='';
        this.errorResetMessage = '';
        this.passwordResetSuccessMessage = response.body.message;
       
      } else {
        this.resetCodeDefault=false;
        this.passwordResetSuccess = true;
        this.errorMessage='';
        this.passwordResetSuccessMessage = '';
        this.errorResetMessage = response.body.message;
      }
    },
    error: (error: any) => {
      this.resetCodeDefault=true;
      this.passwordResetSuccess = false;
      this.errorMessage='';
      this.errorResetMessage = '';
      this.passwordResetSuccessMessage = error.error.message ?? error.error.error.message;
    },
  });
}
toggleFieldTextType() {
  this.fieldTextType = !this.fieldTextType;
}

}
