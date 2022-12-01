import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  constructor() { }

  User: any
  ngOnInit(): void {
 
    this.User = JSON.parse(sessionStorage.getItem('userDetails') as any);
      
    console.log(this.User.user.fullName)
  }
   

}
