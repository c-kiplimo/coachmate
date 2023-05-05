import { Component } from '@angular/core';
import {LocationStrategy} from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'CoachMatePRO';
  sideNavStatus: boolean = false;
  hideHeaderAndSideNav: boolean = false;
  user: any;

  constructor(private url:LocationStrategy, public router: Router) { }

  ngOnInit(): void {
    //get session storage of the loged in user
    this.user = sessionStorage.getItem("user");
    console.log(this.user);

    if (this.user != null) {
      this.hideHeaderAndSideNav = true;
    } else {
      this.hideHeaderAndSideNav = false;

    }
  }
 
}
