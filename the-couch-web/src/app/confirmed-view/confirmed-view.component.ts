import { ToastrService } from 'ngx-toastr';
import { Component, Input, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ApiService } from '../services/ApiService'; 
@Component({
  selector: 'app-confirmed-view',
  templateUrl: './confirmed-view.component.html',
  styleUrls: ['./confirmed-view.component.css']
})
export class ConfirmedViewComponent implements OnInit {
  @Input() formData: any;
  token = '';
  errorMessage = '';
  confirmationSuccess = false;
  time:any;
  countDownDuration:any = 3 * 60 * 1000;
  // countDownDate = new Date().getTime() + this.countDownDuration;
  isCountdownOver = false;
  signUpformData:any = {};
  msisdn!:number;

  timer:any;
  constructor(
    public router: Router,
    public activatedRoute: ActivatedRoute,
    private apiservice: ApiService,
    private toastrService: ToastrService
  ) {}

  ngOnInit(): void {
  this.signUpformData = JSON.parse(sessionStorage.getItem('formData') || '{}')
  this.msisdn = this.signUpformData.msisdn;
  console.log(this.msisdn);
  console.log('form data',this.signUpformData)
  this.countDownTimer()
   
  }
  confirmMobileNumber(): void {
    this.errorMessage = '';
    this.confirmationSuccess = false;
    this.apiservice.confirmMobileNumber(this.token).subscribe(
      (res: any) => {
        console.log(res);
        if (res.body?.error) {
          this.errorMessage = res.body.message;
        } else {
          this.confirmationSuccess = true;
          setTimeout(() => {
            this.router.navigate(['login']);
          }, 5);
         sessionStorage.removeItem('formData');
        }
      },
      (err: any) => {
        console.log(err);
      }
    );
  }
// Update the count down every 1 second
countDownTimer(){
  this.isCountdownOver = false;
  let countDownDate = new Date().getTime() + this.countDownDuration;
  this.timer = setInterval(() => {
  let now = new Date().getTime();
  let timeDifference = countDownDate - now;

  // Time calculations for minutes and seconds
  let minutes = Math.floor((timeDifference % (1000 * 60 * 60)) / (1000 * 60));
  let seconds = Math.floor((timeDifference % (1000 * 60)) / 1000);
  // this.time = minutes +" "+  seconds;

 this.time = minutes + "m " + seconds + "s ";
  if (timeDifference < 0) {
    clearInterval(this.timer);
    this.time = "expired"
    this.isCountdownOver = true;
  }
}, 1000);
}
resendOtp(){
  // this.isCountdownOver = false;

  const options ={
    msisdn: this.msisdn,
  }
  if (this.isCountdownOver) {
    this.apiservice.getResendOtpCode(options).subscribe({
      next: (response) => {
        console.log(response);
        // console.log('here');
        if (response.body.error) {
          this.errorMessage = response.body.message;
          console.log('here');
        } else {
          
          this.toastrService.success(
            'Success',
            'OTP has been sent!'
          );
          this.isCountdownOver = false;
          this.countDownTimer();

          // console.log('here');
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


