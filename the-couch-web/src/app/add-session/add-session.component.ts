import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-add-session',
  templateUrl: './add-session.component.html',
  styleUrls: ['./add-session.component.css']
})
export class AddSessionComponent implements OnInit {


  constructor() { }

  ngOnInit(): void {
  }

  AddSession (sessionForm: any) {

    console.log(sessionForm.value)
    
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
