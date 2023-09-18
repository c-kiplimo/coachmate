import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { faChevronLeft } from '@fortawesome/free-solid-svg-icons';
import { ClientService } from '../services/ClientService';
import { NotificationsService } from '../services/notifications.service';



@Component({
  selector: 'app-contact-us',
  templateUrl: './contact-us.component.html',
  styleUrls: ['./contact-us.component.css']
})
export class ContactUsComponent implements OnInit {
  feedbackForm!: FormGroup;
  field: any;
  feedbackField = [
    "SESSIONS",
    "NOTIFICATIONS",
    "CONTRACTS",
    "SETTINGS",
    "OTHER"
  ];
  backIcon = faChevronLeft;

  constructor(
    private formBuilder: FormBuilder,
    private notificationsService: NotificationsService,
    private toastrService: ToastrService
  ) { }

  ngOnInit(): void {
    this.feedbackForm = this.formBuilder.group({
      feedbackField: '',
      message: ''
    })
  }

  sendFeedback() {
    console.log(this.feedbackForm.value)

    let options = null;
    this.notificationsService.contactUsMessage(this.feedbackForm.value, options).subscribe({
      next: (response) => {
        if (response.error) {
        } else {
          this.toastrService.success(
            'Thank you for the feedback',
            'Message sent successfully'
          );
          this.back();
        }
      }, error: (error: any): any => {
        this.toastrService.error(
          'Please try again',
          'Message not sent'
        );
      },
    });
  }
  back() {
    window.history.back()
  }
}
