import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'CoachMatePRO';
  sideNavStatus: boolean = false;
  hideHeaderAndSideNav: boolean = false;
  user: any;

  constructor(public router: Router) { }

  ngOnInit(): void {
    // Subscribe to the NavigationEnd event to update the layout visibility on route changes
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        this.updateLayoutVisibility();
      });

    // Get session storage of the logged-in user
    this.user = sessionStorage.getItem("user");
    console.log(this.user);

  
    this.hideHeaderAndSideNav = this.user != null;
  }

  shouldShowLayout(): boolean {

    return !this.router.url.includes('');
  }

  private updateLayoutVisibility(): void {
    this.user = sessionStorage.getItem("user");
    this.hideHeaderAndSideNav = this.user != null;
  }
}
