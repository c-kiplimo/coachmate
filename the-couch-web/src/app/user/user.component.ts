import { Component, OnInit } from '@angular/core';
import {
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
  logoutIcon = faSign;
  showDropdown = false;
  loginIcon = faSign;
  userIcon = faUser;
  customersIcon = faUsers;
  businessName: any;
  backIcon: any;

  constructor(public router: Router) { }

  user: any
  ngOnInit(): void {
    this.user = JSON.parse(sessionStorage.getItem('user') as any);
    if (this.user.userRole === 'COACH') {
      this.businessName = this.user.businessName;
    }
    if (this.user.userRole !== 'COACH') {
      this.businessName = this.user.fullName.substring(0, 8);
    }
    else {
      this.businessName = this.user.fullName.substring(0, 8);
    }
  }

  logOut() {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('businessName');
    sessionStorage.removeItem('user');
    this.businessName = '';
    this.router.navigate(['signin']);

  }
  back() {
    window.history.back();
  }

}
