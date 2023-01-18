import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
@Component({
  selector: 'app-feeback-form',
  templateUrl: './feeback-form.component.html',
  styleUrls: ['./feeback-form.component.css']
})
export class FeebackFormComponent implements OnInit {
  feedbackForm = new FormGroup({
    goalSetting: new FormControl(''),
    emotionManagement: new FormControl(''),
    activeListening: new FormControl(''),
    clarification: new FormControl(''),
    agenda: new FormControl(''),
    presence: new FormControl(''),
    additionalComments: new FormControl(''),
  });

  submitForm() {
    // send data to backend for future use
  }

  constructor() { }

  ngOnInit(): void {
  }

}
