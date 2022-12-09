import { Component, OnInit } from '@angular/core';
import {SessionsService }  from '../services/SessionsService';
import { Router } from '@angular/router';
import { ClientService } from '../services/ClientService';
@Component({
  selector: 'app-add-session',
  templateUrl: './add-session.component.html',
  styleUrls: ['./add-session.component.css']
})
export class AddSessionComponent implements OnInit {
addSessionForm:any={
sessionType:"INDIVIDUAL",
sessionVenue:"VIRTUAL"
};

  constructor(private sessionService: ClientService,  private router: Router) { }

  ngOnInit(): void {
  }

  addSession () {
    this.sessionService.addSession(this.addSessionForm).subscribe({
      
      next: (response: any) => {
        console.log("no error")
        this.router.navigate(['/sessions']);
        console.log(this.addSessionForm);
      }, 
      error: (error: any) => {
        console.log("error")
      }
    }
      
    )
    
  }

}

// {
//   "id": 1,
//   "session_no": 1,
//   "name": "restfulToolkitX",
//   "type": "INDIVIDUAL",
//   "details": "restfulToolkitX",
//   "notes": "restfulToolkitX",
//   "amount_paid": "restfulToolkitX",
//   "session_date": "restfulToolkitX",
//   "delivery_date": "restfulToolkitX",
//   "delivered_at": "restfulToolkitX",
//   "status": "NEW",
//   "reason": "restfulToolkitX",
//   "session_venue": "restfulToolkitX",
//   "attachments": "restfulToolkitX",
//   "client_id": "restfulToolkitX",
//   "coach_id": "restfulToolkitX",
//   "goals": "restfulToolkitX",
//   "feedback": "restfulToolkitX",
//   "createdBy": "restfulToolkitX",
//   "lastUpdatedBy": "restfulToolkitX"
// }
