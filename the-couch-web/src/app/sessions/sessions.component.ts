import { Component, OnInit } from '@angular/core';
import { ClientService } from '../services/ClientService';
import { Router, ActivatedRoute } from '@angular/router';
@Component({
  selector: 'app-sessions',
  templateUrl:'./sessions.component.html',
  styleUrls: ['./sessions.component.css']
})
export class SessionsComponent implements OnInit {
  constructor(private restApiService:ClientService,private router: Router) { }
  
  sessions: any;

  ngOnInit(): void {
    this.getAllSessions();
  }
  getAllSessions(){
    this.restApiService.getSessions().subscribe(
      (response: any) => {
        console.log(response)
        this.sessions = response
      }, (error: any) => {
        console.log(error)
      }
    )
  }
  navigateToSessionView(id: any) {
    console.log(id)
    this.router.navigate(['sessionView', id]);

}

}
