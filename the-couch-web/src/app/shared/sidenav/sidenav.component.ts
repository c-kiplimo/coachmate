import { animate, keyframes, style, transition, trigger } from '@angular/animations';
import { Component, Output, EventEmitter, OnInit, HostListener, Input } from '@angular/core';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { sideNavData } from './sidenav-data';

interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.css'],
  animations: [
    trigger('fadeIn', [
      transition(':enter', [
        // :enter is alias to 'void => *'
        style({ opacity: 0 }),
        animate(250, style({ opacity: 1 })),
      ]),
    ]),
    trigger('fadeInOut', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('350ms',
          style({ opacity: 1 })
        )
      ]),
      transition(':leave', [
        style({ opacity: 1 }),
        animate('350ms',
          style({ opacity: 0 })
        )
      ])
    ]),
    trigger('rotate', [
      transition(':enter', [
        animate('1000ms',
          keyframes([
            style({ transform: 'rotate(0deg)', offset: '0' }),
            style({ transform: 'rotate(2turn)', offset: '1' })
          ])
        )
      ])
    ])

  ]
})

export class SidnavComponent implements OnInit {
  @Input() sideNavStatus: boolean = false;

  sideNavMenu = sideNavData;

  userData: any;
  user: any;
  menuData: any;

  menuDataForMobile: any;
  excessMenu: any;

  selectedTabIndex!: number;
  currentRouteUrl!: string;

  showMoreMenus = false;

  bottomMenu: any;

  @Output() onToggleSidebar: EventEmitter<SideNavToggle> = new EventEmitter();
  collapsed = false;
  screenWidth = 0;
  navData: any;
  coachSessionData: any;
  coachData: any;
  userRole: any;

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.screenWidth = window.innerWidth;
    if (this.screenWidth <= 768) {
      this.collapsed = false;
      this.onToggleSidebar.emit({ collapsed: this.collapsed, screenWidth: this.screenWidth });
    }
  }

  ngOnInit(): void {
    if (window.innerWidth > 920) {
      this.showMoreMenus = false;
    }

    //get session storage of the loged in user
    this.coachSessionData = sessionStorage.getItem('user');

    if (this.coachSessionData != null) {
      this.coachData = JSON.parse(this.coachSessionData);

      this.userRole = this.coachData.userRole;
      console.log(this.userRole);

      if (this.userRole == 'COACH') {
        this.menuData = this.sideNavMenu.navDataforCoach;
      } else if (this.userRole == 'ORGANIZATION') {
        this.menuData = this.sideNavMenu.navDataforOrg;;
      } else if (this.userRole == 'CLIENT') {
        this.menuData = this.sideNavMenu.navDataforClient;
      }
    }

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.currentRouteUrl = event.url;
        this.sideNavMenu.navDataforOrg.forEach((element) => {
          if (this.currentRouteUrl.includes(element.routeLink)) {
            this.selectedTabIndex = element.number;
          }
        })
      }
    });

  }

  constructor(public router: Router) { }


  handleShowMoreMenus() {
    this.showMoreMenus = !this.showMoreMenus;
    // this.showMoreMenus = true;
  }

  hideMoreMenus() {
    this.showMoreMenus = false
  }


}
