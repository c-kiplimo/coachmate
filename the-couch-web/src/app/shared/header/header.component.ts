import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { faSign, faUser, faUsers } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  @Output() sideNavToggle = new EventEmitter<boolean>();
  menuStatus: boolean = false;

  logoutIcon = faSign;
  showDropdown = false;
  loginIcon = faSign;
  userIcon = faUser;
  customersIcon = faUsers;
  businessName: any;
  backIcon: any;
  user: any

  constructor(
    public router : Router
  ) {}


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

  SideNavToggle() {
    this.menuStatus = !this.menuStatus;
    this.sideNavToggle.emit(this.menuStatus);
  }

  logOut() {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('businessName');
    sessionStorage.removeItem('user');
    this.businessName = '';
    this.router.navigate(['signin']);

  }

}
