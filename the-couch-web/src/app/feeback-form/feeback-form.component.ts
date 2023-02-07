import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormBuilder } from '@angular/forms';
import { ClientService } from '../services/ClientService';
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
  constructor(
    private formbuilder: FormBuilder,
    private ClientService: ClientService,
    private toastrService: ToastrService,
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient
  ) { }


  ngOnInit(): void {
    this.sessionId = this.route.snapshot.params['sessionId'];
    this.http.get(`/api/sessions/${this.sessionId}`)
    .subscribe(session => this.session = session);
    
    
    this.feebackForm = this.formbuilder.group({
      goalSetting: [''],
      emotionManagement: [''],
      activeListening: [''],
      clarification: [''],
      agenda: [''],
      presence: [''],
      additionalComments: ['']
    });
  }

  submitForm() {
    console.log(this.feebackForm.value);
    this.feebackForm.value.sessionId = this.sessionId;
    this.feebackForm.value.coachId = this.coachId;

    this.ClientService.addFeedback(this.feebackForm.value, this.sessionId, this.coachId).subscribe(
      (response) => {
        console.log(response);
        this.toastrService.success('Feedback Added Successfully');
        this.router.navigate(['/sessions']);
      },
      (error) => {
        console.log(error);
        this.toastrService.error('Error in adding Feedback');
      }
    );
  }


}
