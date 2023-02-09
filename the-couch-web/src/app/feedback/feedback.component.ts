import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-feedback',
  templateUrl: './feedback.component.html',
  styleUrls: ['./feedback.component.css']
})
export class FeedbackComponent implements OnInit {

  loading = false;
  Feedbacks: any;
  coachSessionData: any;
  coachData: any;
  userRole: any;

  OrgData: any;
  orgSession: any;


  constructor() { }
  
  ngOnInit(): void {
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);
    this.userRole = this.coachData.userRole;
    console.log(this.userRole);

    if(this.userRole == 'ORGANIZATION'){
      this.OrgData = sessionStorage.getItem('Organization');
      this.orgSession = JSON.parse(this.OrgData);
      console.log(this.orgSession);

      this.getOrgFeedbacks(this.orgSession.id);
  }
}

  getOrgFeedbacks(orgId: any) {
    this.loading = true;
  }


}
