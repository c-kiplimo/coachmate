import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { faEnvelope } from '@fortawesome/free-solid-svg-icons';
import {
  faChevronLeft,
  faChevronRight,
} from '@fortawesome/free-solid-svg-icons';
import { ToastrService } from 'ngx-toastr';

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
  service: any;
  featuresIcon!: IconProp;

  constructor(
    private formBuilder: FormBuilder,
    private toastrService: ToastrService
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

    this.service.contactUsMessage(this.contactForm.value).subscribe({
      next: (response: { error: any; body: { message: any; }; }) => {
        console.log(response);
        if (response.error) {
          console.log(response.body.message);
        } else {
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
