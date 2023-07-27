import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import {
  faChevronLeft,
  faChevronRight,
  faEnvelope,

} from '@fortawesome/free-solid-svg-icons';
import { ToastrService } from 'ngx-toastr';
import { ClientService } from '../services/ClientService';
import { NotificationsService } from '../services/notifications.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  contactIcon = faEnvelope;
  rightIcon = faChevronRight;
  leftIcon = faChevronLeft;
  contactForm!: FormGroup;
 
  constructor(
    private formBuilder: FormBuilder,
    private toastrService: ToastrService,
    private notificationService: NotificationsService
  ) {}

  ngOnInit(): void {
    this.clearSessionStorage();
    this.contactForm = this.formBuilder.group({
      name: '',
      email: '',
      message: '',
    });
  }

  clearSessionStorage() {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('businessName');
    sessionStorage.removeItem('user');
  }
  contactUsMessage() {
    console.log(this.contactForm.value);

    this.notificationService.contactUsMessage(this.contactForm.value).subscribe({
      next: (response: { error: any; body: { message: any; }; }) => {
        console.log(response);
        if (response.error) {
          console.log(response.body.message);
        } else {
          this.contactForm.reset();
          this.toastrService.success(
            'Message received, we will get back to you shortly'
          );
        }
      },
      error: (error: any): any => {
        this.toastrService.error('Please try again', 'Message not sent');
      },
    });
  }

}
