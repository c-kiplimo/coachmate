import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { ClientService } from '../services/ClientService';
import {
  faChevronLeft,
  faChevronRight,
  faEnvelope,

} from '@fortawesome/free-solid-svg-icons';
import { NotificationsService } from '../services/notifications.service';

@Component({
  selector: 'app-support',
  templateUrl: './support.component.html',
  styleUrls: ['./support.component.css']
})

export class SupportComponent implements OnInit {
  contactIcon = faEnvelope;
  rightIcon = faChevronRight;
  leftIcon = faChevronLeft;
  contactForm!: FormGroup;
  panelOpenState = false;


  constructor(
    private formBuilder: FormBuilder,
    private toastrService: ToastrService,
    private notificationService: NotificationsService
  ) {}

  ngOnInit(): void {
    this.contactForm = this.formBuilder.group({
      name: '',
      email: '',
      message: '',
    });
  }


title: any;
back() {
  window.history.back();
}
contactUsMessage() {
  if (this.contactForm.invalid) {
    // If the form is invalid, display an error and return
    this.toastrService.error('Please fill in all required fields', 'Form Error');
    return;
  }

  console.log(this.contactForm.value);

  this.notificationService.contactUsMessage(this.contactForm.value).subscribe({
    next: (response: { error: any; body: { message: any; }; }) => {
      console.log(response);
      if (response.error) {
        console.log(response.body.message);
      } else {
        this.contactForm.reset();
        this.toastrService.success(
          'Thank you for the Message',
          'We will get back to you shortly'
        );
      }
    },
    error: (error: any): any => {
      this.toastrService.error('Please try again', 'Message not sent');
    },
  });
}
}
