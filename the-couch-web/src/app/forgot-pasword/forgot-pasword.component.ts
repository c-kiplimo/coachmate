import { 
  Component,
  ElementRef,
  HostListener,
  OnInit,
  ViewChild, } from '@angular/core';
import { 
  faEye, 
  faEyeSlash, 
  faPlusSquare,
  faChevronLeft,
} from '@fortawesome/free-solid-svg-icons';
import { ApiService } from '../services/ApiService';  

import { ToastrService } from 'ngx-toastr';
import { fromEvent, map, debounceTime, distinctUntilChanged } from 'rxjs';


@Component({
  selector: 'app-forgot-pasword',
  templateUrl: './forgot-pasword.component.html',
  styleUrls: ['./forgot-pasword.component.css']
})
export class ForgotPaswordComponent implements OnInit {
  resetForm!: any;
  backIcon = faChevronLeft;
  fieldTextType!: boolean;
  eyeIcon = faEye;
  eyeSlashIcon = faEyeSlash;
  // Get code view
  getCodeView = true;
  errorMessage = '';
  getCodeErrorMessage = '';

  // reset code view
  passwordResetSuccessMessage = '';
  errorResetMessage = '';
  resetCodeDefault = false;
  passwordResetSuccess = false;
  countDownStarted = false;
  passwordErrorMessage = '';

  formData = {
    msisdn: '',
    password: '',
    otp: '',
    confirmPassword: '',
  };
  passwordInvalid = false;
  time: any;
  countDownDuration: any = 2 * 60 * 1000;
  isCountdownOver = false;
  timer: any;
  msisdn!: number;

  @ViewChild('yourElement') yourElement!: ElementRef;

  constructor(
    private apiservice: ApiService, 
    private toastrService: ToastrService
    ) {}

  ngOnInit(): void {

  }
  
  ngAfterViewInit(): void {
    fromEvent(this.yourElement.nativeElement, 'input')
      .pipe(map((event: any) => (event.target as HTMLInputElement).value))
      .pipe(debounceTime(2000))
      .pipe(distinctUntilChanged())
      .subscribe((data) => this.validatePasswordRule());
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
    if (this.formData.password === this.formData.confirmPassword) {
      this.passwordInvalid = false;
      this.passwordReset();
    } else {
      this.passwordInvalid = true;
      this.passwordErrorMessage = "Passwords don't match";
    }
  }
  getCode() {
    const options = {
      msisdn: this.formData.msisdn,
    };

    this.apiservice.getResetCode(options).subscribe({
      next: (response) => {
        if (response.body.error) {
          this.getCodeView = true;
          this.errorMessage = '';
          // this.countDownTimer();
          this.getCodeErrorMessage = response.body.message;
        } else {
          this.getCodeView = false;
          this.resetCodeDefault = true;
          this.countDownTimer();
          this.getCodeErrorMessage = '';
          this.errorMessage = response.body.message;
        }
      },
      error: (error: any) => {
        this.getCodeView = true;
        this.errorMessage = '';
        this.getCodeErrorMessage =
          error.error.message ?? error.error.error.message;
      },
    });
  }

  passwordReset() {
    this.apiservice.setNewPassword(this.formData).subscribe({
      next: (response) => {
        if (response.body.error) {
          this.resetCodeDefault = true;
          this.errorMessage = '';
          this.errorResetMessage = '';
          this.passwordResetSuccessMessage = response.body.message;
        } else {
          this.resetCodeDefault = false;
          this.passwordResetSuccess = true;
          this.errorMessage = '';
          this.passwordResetSuccessMessage = '';
          this.errorResetMessage = response.body.message;
        }
      },
      error: (error: any) => {
        this.resetCodeDefault = true;
        this.passwordResetSuccess = false;
        this.errorMessage = '';
        this.errorResetMessage = '';
        this.passwordResetSuccessMessage =
          error.error.message ?? error.error.error.message;
      },
    });
  }
  toggleFieldTextType() {
    this.fieldTextType = !this.fieldTextType;
  }
  // Update the count down every 1 second
  countDownTimer() {
    this.countDownStarted = true;
    this.isCountdownOver = false;
    let countDownDate = new Date().getTime() + this.countDownDuration;
    this.timer = setInterval(() => {
      let now = new Date().getTime();
      let timeDifference = countDownDate - now;

      // Time calculations for minutes and seconds
      let minutes = Math.floor(
        (timeDifference % (1000 * 60 * 60)) / (1000 * 60)
      );
      let seconds = Math.floor((timeDifference % (1000 * 60)) / 1000);
      // this.time = minutes +" "+  seconds;

      this.time = minutes + 'm ' + seconds + 's ';
      if (timeDifference < 0) {
        clearInterval(this.timer);
        this.time = 'expired';

        this.isCountdownOver = true;
      }
    }, 1000);
  }

  resendOtp() {
    console.log("Resend invoked");
    // this.isCountdownOver = false;
    this.errorMessage = '';
    this.msisdn = parseInt(this.formData.msisdn);
    const options = {
      msisdn: this.msisdn,
    };
    if (this.isCountdownOver) {
      this.apiservice.getResendOtpCode(options).subscribe({
        next: (response) => {
          console.log(response);
          // console.log('here');
          if (response.body.error) {
            this.errorMessage = response.body.message;
            console.log('here');
          } else {
            this.toastrService.success('Success', 'OTP has been sent!');
            this.isCountdownOver = false;
            this.countDownTimer();

            console.log('here');
          }
        },
        error: (error: any) => {
          // console.log(error);
          this.toastrService.error('Please retry', 'OTP not sent!');
        },
      });
    }
  }
}
