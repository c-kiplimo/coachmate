import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CoachEducationService } from '../../services/CoachEducationService';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-training',
  templateUrl: './add-training.component.html',
  styleUrls: ['./add-training.component.css']
})
export class AddTrainingComponent implements OnInit {
formData: any;
addTrainingForm!: FormGroup;
coachSessionData: any;
coachData: any;

  constructor(
    private formbuilder: FormBuilder,
    private CoachEducationService: CoachEducationService,
    private toastrService: ToastrService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);

    this.addTrainingForm = this.formbuilder.group({
      courseName: ['', Validators.required],
      provider: ['', Validators.required],
      dateIssued: ['', Validators.required],
      validTill: ['', Validators.required],
      trainingHours: ['', Validators.required],
      certificate:'',
      certificateUrl: ''
    });
    
  }
  back() {
    window.history.back();
  }
  addTraining() {
    var addCourse = this.addTrainingForm.value;
   
    addCourse.createdBy = this.coachData.fullName;
    addCourse.coachId = this.coachData.id;
    console.log(addCourse);
    this.CoachEducationService.addCoachEducation(addCourse).subscribe(
      (response: any) => {
        console.log(response);
        this.formData = response;
        this.toastrService.success('Training added!', 'Success!');
        this.router.navigate(['/education']);
      }
    )
  }

}
