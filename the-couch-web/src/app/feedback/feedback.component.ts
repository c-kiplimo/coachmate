import { Component, OnInit } from '@angular/core';
import { ClientService } from '../services/ClientService';
import { style, animate, transition, trigger } from '@angular/animations';

@Component({
  selector: 'app-feedback',
  templateUrl: './feedback.component.html',
  styleUrls: ['./feedback.component.css']
})
export class FeedbackComponent implements OnInit {

  loading = false;
  feedbacks: any;
  coachSessionData: any;
  coachData: any;
  userRole: any;

  OrgData: any;
  orgSession: any;


  constructor(
    private apiService: ClientService,
  ) { }
  
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

    
  } else if (this.userRole == 'COACH') {
    this.getCoachFeedbacks(this.coachData.coach.id);
  }
}



  getCoachFeedbacks(coachId: any) {
    this.loading = true;
    this.apiService.getCoachFeedbacks(coachId).subscribe(
      (response: any) => {
        console.log(response);
        this.feedbacks = response;
        console.log(this.feedbacks);
        this.loading = false;
      }
    );

  }
  // get organization feedbacks
  getOrgFeedbacks(orgId: any) {
    this.loading = true;
    this.apiService.getOrgFeedbacks(orgId).subscribe(
      (response: any) => {
        console.log(response);
        this.feedbacks = response;
        console.log(this.feedbacks);
        this.loading = false;
      }
    );

  }


}
