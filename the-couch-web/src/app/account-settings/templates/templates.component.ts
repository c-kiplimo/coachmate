import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { NotificationsService } from "../../../services/notifications.service";
import { style, animate, transition, trigger } from '@angular/animations';

import { BakersService } from "../../../services/bakers.service";
import { ToastrService } from 'ngx-toastr';
import { faChevronLeft } from '@fortawesome/free-solid-svg-icons';
import { AuthService } from "../../../services/auth.service";

@Component({
  selector: 'app-templates',
  templateUrl: './templates.component.html',
  styleUrls: ['./templates.component.scss', '../settings/settings.component.scss' , '../profile/profile.component.scss'],
  animations: [
    trigger('fadeIn', [
      transition(':enter', [
        // :enter is alias to 'void => *'
        style({ opacity: 0 }),
        animate(600, style({ opacity: 1 })),
      ]),
    ]),
  ],
})
export class TemplatesComponent implements OnInit {

  user: any;
  userNotificationSettings: any;
  notificationDetails: any;
  backIcon = faChevronLeft;
  orderTemplateTypes = [
    'NEW_ORDER ',
    'PARTIAL_ORDER_PAYMENT',
    'FULL_ORDER_PAYMENT',
    'CANCEL_ORDER',
    'DELIVER_ORDER',
  ];

  editingSettings = false;
  isSaving = false;
  disableButton = false;
  saveSuccess = false;

  constructor(
    private notificationsService: NotificationsService,
    private bakersService: BakersService,
    private http: HttpClient,
    private toastrService: ToastrService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {

    window.scroll(0, 0);

    this.user = JSON.parse(
      sessionStorage.getItem('user') || '{}'
    );

    this.setFields();

  }

  setFields(): void {
    this.notificationDetails = JSON.parse(JSON.stringify(this.user.baker.bakerNotificationSettings));
  }

  saveSettings(): void {
    this.isSaving = true;

    this.bakersService.saveSettings(this.notificationDetails).subscribe({
      next: (res: any) => {
        // console.log('here', res.body);
        this.isSaving = false;
        this.saveSuccess = true;
        this.editingSettings = false;
        window.scroll(0, 0);

        this.getAccount();

        setTimeout(() => {
          this.saveSuccess = false;
        }, 2000);
        // this.toastrService.success('Settings updated', 'Success!');
      },
      error: (err: any) => {
        // console.log(err);
        this.toastrService.error('Settings not saved, try again!', 'Failed!');
      },
    });
  }

  resetTemplate(template: string): void {
    this.notificationDetails[template] = this.user.baker.bakerNotificationSettings[template];
  }

  getAccount(): void {
    this.authService.getAccount().subscribe(
      (response: any) => {
        // console.log(response);
        // console.log('here');

        sessionStorage.setItem('businessName', response.body.firstName);
        sessionStorage.setItem('bakerStatus', response.body.baker.status);
        sessionStorage.setItem('user', JSON.stringify(response.body));
        sessionStorage.setItem('notificationSettings', JSON.stringify(response.body.bakerNotificationSettings))

        setTimeout(() => {
          location.reload();
        }, 10);

      },
      (error: any) => {
        // console.log(error);

      }
    )
  }

  toggleEditingSettings(): void {
    this.editingSettings = !this.editingSettings;
    this.disableButton = true;
    this.setFields();

    setTimeout(() => {
      this.disableButton = false;
    }, 2000);
  }

  back(): void {
    window.history.back()
  }

}
