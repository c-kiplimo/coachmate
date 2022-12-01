import { Component, OnInit } from '@angular/core';
import { ClientService } from '../services/ClientService';

@Component({
  selector: 'app-sessions',
  templateUrl:'./sessions.component.html',
  styleUrls: ['./sessions.component.css']
})
export class SessionsComponent implements OnInit {
  constructor(private SessionService:ClientService) { }
  
  Sessions: any;

  ngOnInit(): void {
    this.SessionService.getSessions().subscribe(
      (response: any) => {
        console.log(response)
        this.Sessions = response
      }, (error: any) => {
        console.log(error)
      }
    )
  }

}
