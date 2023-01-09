import { Component, OnInit } from '@angular/core';
import {
  faRightFromBracket,
  faSign,
  faUser,
  faUsers,
} from '@fortawesome/free-solid-svg-icons';
import { Router, ActivatedRoute } from '@angular/router';
@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  logoutIcon = faRightFromBracket;
  loginIcon = faSign;
  userIcon = faUser;
  customersIcon = faUsers;
  businessName: any;

  constructor(public router: Router) { }

  User: any
  ngOnInit(): void {
 
    this.User = JSON.parse(sessionStorage.getItem('user') as any);
      
    console.log(this.User.coach.businessName) 
  }
  logOut() {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('businessName');
    sessionStorage.removeItem('user');
    this.businessName = '';

  }
  
}
