import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormBuilder } from '@angular/forms';
import { ClientService } from '../services/ClientService';
import { ToastrService } from 'ngx-toastr';
import { Route, Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
@Component({
  selector: 'app-feeback-form',
  templateUrl: './feeback-form.component.html',
  styleUrls: ['./feeback-form.component.css']
})
export class FeebackFormComponent implements OnInit {
  feebackForm: any;
  session_id: any;
  sessionId: any;

  
  submitForm() {
    var feeback = this.feebackForm.value;
    feeback.sessionId = this.formData.sessionId;
    console.log(feeback);
    console.log("session id",this.sessionId);
    this.ClientService.addFeedback(feeback).subscribe(
      (response: any) => {
        console.log("session id",this.sessionId);
        console.log(response);
        this.formData = response;
        this.toastrService.success('Feedback submitted!', 'Success!');
        this.router.navigate(['/sessionView']);
      }
    )
  }
  

  formData: any;
  constructor(
    private formbuilder: FormBuilder,
    private ClientService: ClientService,
    private toastrService: ToastrService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) { }


  ngOnInit(): void {
      this.activatedRoute.queryParams.subscribe(params => {
        this.formData = { sessionId: params['sessionId'] };
        console.log("here",this.sessionId)
      });
    
    
    this.feebackForm = this.formbuilder.group({
      goalSetting: [''],
      emotionManagement: [''],
      activeListening: [''],
      clarification: [''],
      agenda: [''],
      presence: [''],
      additionalComments: ['']
    });
    // { sessionId: params.sessionId };
  }

}
