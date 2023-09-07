import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss'],
})
export class SettingsComponent implements OnInit {
  businessName: any;

  constructor(public router : Router) { }

  ngOnInit(): void {
    
  }
  logOut() {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('businessName');
    sessionStorage.removeItem('user');
    this.businessName = '';
    this.router.navigate(['signin']);

  }
  back() {
    window.history.back()
  }
}

