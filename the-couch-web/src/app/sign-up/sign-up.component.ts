import { Component, OnInit } from '@angular/core';
import { LoginService } from '../services/LoginService';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {

  constructor(private LoginService: LoginService, private router: Router) { }

  ngOnInit(): void {
  }

  signUp(signUpData: any) {
    this.LoginService.signUp(signUpData.value).subscribe({
      next: (response) => {
        console.log(response.data.value);
        console.log("response");
        this.router.navigate(['/signin']);
      },
      error: (error:any) => {
        console.log("error");
        this.router.navigate(['/signin']);
      },
      
    })
  }

}
