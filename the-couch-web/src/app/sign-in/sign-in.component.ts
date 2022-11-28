
import { Router } from '@angular/router';
import { LoginService } from '../services/LoginService';
import { Component, OnInit } from '@angular/core';


@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})


export class SignInComponent implements OnInit {

  constructor(private LoginService: LoginService, private router: Router) { }

  ngOnInit(): void {
  }

  login(loginData: any) {
    console.log(loginData.value)
    this.LoginService.login(loginData.value).subscribe({
      next: (response) => {
        console.log(response.token)
        sessionStorage.setItem('userDetails', JSON.stringify(response));
        this.router.navigate(['']);
      },
      error: (error:any) => {
        console.log(error);
      },
});
}
}
