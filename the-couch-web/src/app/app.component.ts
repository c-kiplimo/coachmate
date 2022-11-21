import { Component } from '@angular/core';
import {LocationStrategy} from '@angular/common';


interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
  
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'The Couch Dashboard';
  notlogged = false;
  

  constructor(private url:LocationStrategy) { }

  ngOnInit(): void {
    console.log(this.url.path());
    if(this.url.path()==='/signin'){

     this.notlogged=true; 
     }
  }
  
  isSideNavCollapsed = false;
  screenWidth = 0;

  
  onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }

 
 
}
