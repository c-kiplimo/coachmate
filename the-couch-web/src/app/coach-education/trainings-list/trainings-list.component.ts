import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';

import { CoachEducationService } from '../../services/CoachEducationService';
@Component({
  selector: 'app-trainings-list',
  templateUrl: './trainings-list.component.html',
  styleUrls: ['./trainings-list.component.css']
})
export class TrainingsListComponent implements OnInit {
  loading = false;
  itemsPerPage = 20;
  filters: any = {
    status: '',
    searchItem: '',
  };

editCoachEducationForm!: FormGroup;

numberOfHours: any;

numberOfMinutes: any;
  http: any;
  items: any;
  coachSessionData: any;
  coachData: any;

  currentCoachEducation: any;

  coachEducationData: any;
  constructor(
     private router: Router,
     private CoachEducationService: CoachEducationService,
     private formbuilder: FormBuilder,
    ) { }

  ngOnInit(): void {
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);

    this.getCoachEducation(this.coachData.id);

    this.editCoachEducationForm = this.formbuilder.group({
      courseName: ' ',
      provider: ' ',
      dateIssued: ' ',
      validTill: ' ',
      trainingHours: ' ',
      certificateUrl: ' ',

    })

  }

  getCoachEducation(id: any) {
    const options = {
      coachId: id,
    }

    this.loading = true;
    this.CoachEducationService.getCoachEducation(options).subscribe(
      (response: any) => {
        this.loading = false;
        console.log(response);
        this.coachEducationData = response;
        this.calculateNumberOfHours(this.coachEducationData);
      }, (error: any) => {
        console.log(error);
      }
    )

  }

  calculateNumberOfHours(data: any) {
    //calculate total number of hours
    let totalHours = 0;
    data.forEach((element: any) => {
      totalHours += parseInt(element.trainingHours);
    });
    this.numberOfHours = Math.floor(totalHours);
  }

deleteItem(coachEducation: any) {
  console.log(coachEducation);
  this.currentCoachEducation = coachEducation;
}
deleteCoachEducation() {
  this.loading = true;
  this.CoachEducationService.deleteCoachEducation(this.currentCoachEducation.id).subscribe(
    (response: any) => {
      this.loading = false;
      console.log(response);
      this.getCoachEducation(this.coachData.id);
    }, (error: any) => {
      console.log(error);
    }
  )
}

setCurrectCoachEducation(coachEducation: any) {
  this.currentCoachEducation = coachEducation;
  console.log(this.currentCoachEducation);

  this.editCoachEducationForm = this.formbuilder.group({
    courseName: this.currentCoachEducation.courseName,
    provider: this.currentCoachEducation.provider,
    dateIssued: this.currentCoachEducation.dateIssued,
    validTill: this.currentCoachEducation.validTill,
    trainingHours: this.currentCoachEducation.trainingHours,
    certificateUrl: this.currentCoachEducation.certificateUrl,
    
  });
}

editCoachEducation() {
  console.log(this.editCoachEducationForm.value);
  this.loading = true;
  var data = this.editCoachEducationForm.value;
  data.id = this.currentCoachEducation.id;
  data.lastUpdatedBy = this.coachData.fullName;
  console.log(data);
  this.CoachEducationService.updateCoachEducation(data).subscribe(
    (response: any) => {
      this.loading = false;
      console.log(response);
      this.getCoachEducation(this.coachData.id);
    }, (error: any) => {
      console.log(error);
    }
  )
}


}
