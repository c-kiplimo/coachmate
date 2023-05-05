import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormBuilder } from '@angular/forms';
import { ClientService } from '../../services/ClientService';
import { ToastrService } from 'ngx-toastr';
import { Route, Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
@Component({
  selector: 'app-feeback-form',
  templateUrl: './feeback-form.component.html',
  styleUrls: ['./feeback-form.component.css']
})
export class FeebackFormComponent implements OnInit {
  feebackForm: any;
  sessionId: any;
  session: any;
  coachId: any;
  sessions: any;
  loading: any;
  constructor(
    private formbuilder: FormBuilder,
    private clientService: ClientService,
    private toastrService: ToastrService,
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient
  ) { }


  ngOnInit(): void {  
   
    this.sessionId = this.route.snapshot.params['sessionId'];
    console.log(this.sessionId);
  
    
    
      
    
    
    this.feebackForm = this.formbuilder.group({
      understandingScore: [''],
      emotionalIntelligenceScore: [''],
      listeningSkillsScore: [''],
      clarificationScore: [''],
      availabilityScore: [''],
      comments: ['']
    });
  }
  

  submitForm() {
    const params = {
      session_id: this.sessionId,
    };

    this.clientService.addFeedback(this.feebackForm.value, params).subscribe(
      (response) => {
        console.log(response);
        this.toastrService.success('Feedback Added Successfully');
        this.router.navigate(['sessionView', this.sessionId]);
      },
      (error) => {
        console.log(error);
        this.toastrService.error('Error in adding Feedback');
      }
    );
  }


}
