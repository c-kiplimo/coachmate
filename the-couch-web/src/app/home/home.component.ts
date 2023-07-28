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

  securityContent = [
    {
      title: 'Our track record',
      description: 'CoachMatePRO is a product by Meliora Technologies LTD, a software engineering company with a proven track record of delivering high-quality enterprise solutions for banks and Telcos over the past 8 years. You can be assured that CoachMatePRO upholds the same level of data security standards.'
    },
    {
      title: 'Best in class security',
      description: 'We utilize multiple layers of encryption to ensure the utmost protection of your, & your client’s, critical information, including phone numbers, email addresses, and more. Rest assured that only you have access to this data – not even our team of engineers can view it.'
    },
    {
      title: 'As a matter of policy',
      description: 'We are committed to protecting your data and will not share it with any third party. Your data will only be used to provide and improve our services to you. Our privacy policy provides guidelines on how to use and protect your data.'
    },
    {
      title: 'We have a lot to lose',
      description: 'The future of this product relies on the secure and protected storage of your private data. We are dedicated to continuously improving our methods to safeguard your business\'s data.'
    }
  ];

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
