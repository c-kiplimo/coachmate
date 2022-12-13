import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ApiService } from '../services/ApiService'; 
@Component({
  selector: 'app-confirmed-view',
  templateUrl: './confirmed-view.component.html',
  styleUrls: ['./confirmed-view.component.css']
})
export class ConfirmedViewComponent implements OnInit {

  token = '';
  errorMessage = '';
  confirmationSuccess = false;

  constructor(
    public router: Router,
    public activatedRoute: ActivatedRoute,
    private service: ApiService
  ) { }

  ngOnInit(): void {
  }
  confirmMobileNumber(): void {
    this.errorMessage = '';
    this.confirmationSuccess = false;
    this.service.confirmMobileNumber(this.token).subscribe(
      (res: any) => {
        console.log(res);
        if (res.body?.error) {
          this.errorMessage = res.body.message;
        } else {
          this.confirmationSuccess = true;
        }
      },
      (err: any) => {
        console.log(err);
      }
    );
  }
  resendOtp() {
    throw new Error('Method not implemented.');
    }
}
