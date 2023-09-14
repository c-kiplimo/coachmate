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
  user: any;
  clientId!: number;
  topics = [
    "SESSIONS",
    "NOTIFICATIONS",
    "CONTRACTS",
    "SETTINGS",
    "OTHER"
  ]


  constructor(
    private formBuilder: FormBuilder,
    private toastrService: ToastrService,
    private notificationService: NotificationsService
  ) {}

  ngOnInit(): void {
    this.user = JSON.parse(sessionStorage.getItem('user')!);
    this.clientId = this.user.id; 
    this.contactForm = this.formBuilder.group({
      topic: '',
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

  let options = {
    clientId: this.clientId,
  }

  this.notificationService.contactUsMessage(this.contactForm.value, options).subscribe({
    next: (response: { error: any; body: { message: any; }; }) => {
      if (response.error) {
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
