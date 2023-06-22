import { Component, OnInit } from '@angular/core';
import { ClientService } from '../../services/ClientService';
import { style, animate, transition, trigger } from '@angular/animations';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { error } from 'jquery';

@Component({
  selector: 'app-feedback',
  templateUrl: './feedback.component.html',
  styleUrls: ['./feedback.component.css'],
  animations: [
    trigger('fadeIn', [
      transition(':enter', [
        // :enter is alias to 'void => *'
        style({ opacity: 0 }),
        animate(600, style({ opacity: 1 })),
      ]),
    ]),
  ],
})
export class FeedbackComponent implements OnInit {

  loading = false;
  feedbacks?: any;
  coachSessionData: any;
  coachData: any;
  userRole: any;

  OrgData: any;
  orgSession: any;
  feedback: any;
  coachId: any;
  page: number = 0;


  constructor(
    private router: Router,
    private route: ActivatedRoute,
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
      this.getOrgFeedbacks(this.OrgData.id);
  } else if (this.userRole == 'COACH') {
    this.coachId = this.coachData.id;
    this.getCoachFeedbacks(this.coachId);
  }
  
  
}

  getCoachFeedbacks(coachId: any) {
    this.loading = true;
    this.apiService.getCoachFeedbacks(coachId).subscribe(
      (response: any) => {
        console.log(response);
        this.feedbacks = response.body;
        console.log(this.feedbacks);
        this.loading = false;
      }, error => {
        console.log(error);
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
        this.feedbacks = response.body;
        console.log(this.feedbacks);
        this.loading = false;
      }
    );

  }
  viewComment(feedback: any): void {
    this.feedback = feedback;
    console.log(this.feedback);
  }

}
