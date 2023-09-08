import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { faChevronLeft } from '@fortawesome/free-solid-svg-icons';
import { ClientService } from '../services/ClientService';
import { NotificationsService } from '../services/notifications.service';



@Component({
  selector: 'app-contact-us',
  templateUrl: './contact-us.component.html',
  styleUrls: ['./contact-us.component.scss']
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

    this.notificationsService.contactUsMessage(this.feedbackForm.value).subscribe({
      next: (response) => {
        console.log(response);
        if (response.error) {
          console.log(response.body.message);
        } else {
          this.toastrService.success(
            'Thank you for the feedback',
            'Message sent successfully'
          );
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
