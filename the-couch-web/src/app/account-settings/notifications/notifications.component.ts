import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { NotificationsService } from "../../../services/notifications.service";
import { BakersService } from "../../../services/bakers.service";
import { ToastrService } from 'ngx-toastr';
import { faChevronLeft } from '@fortawesome/free-solid-svg-icons';


@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.scss', '../settings/settings.component.scss', '../profile/profile.component.scss']
})
export class NotificationsComponent implements OnInit {

  saveChanges = true;
  user: any;
  userNotificationSettings: any;
  notificationDetails: any = {};
  backIcon = faChevronLeft;
  selectedOption = '';

  constructor(
    private notificationsService: NotificationsService,
    private bakersService: BakersService,
    private http: HttpClient,
    private toastrService: ToastrService
  ) { }

  ngOnInit(): void {
    window.scroll(0, 0);

    this.userNotificationSettings = JSON.parse(
      sessionStorage.getItem('notificationSettings') || '{}'
    );

    this.notificationDetails = JSON.parse(JSON.stringify(this.userNotificationSettings));
    this.selectedOption = this.notificationDetails.bakerNotificationMode;

    // console.log(this.notificationDetails);

  }

  saveSettings(): void {
    // console.log(this.notificationDetails.bakerNotificationMode);
    window.scroll(0, 0);

    this.bakersService.saveSettings(this.notificationDetails).subscribe({
      next: (res: any) => {
        // console.log('here', res.body);
        const data = res.body;
        this.toastrService.success('Settings saved!', 'Success!');

        sessionStorage.setItem('notificationSettings', JSON.stringify(data));

        setTimeout(() => {
          this.userNotificationSettings = JSON.parse(sessionStorage.getItem('notificationSettings') || '{}');
        }, 10);
      },
      error: (err: any) => {
        // console.log(err);
        this.toastrService.error('Settings not saved, try again!', 'Failed!');
      },
    });
  }

  toggleEnableSettings():void{
    this.notificationDetails.notificationEnable = !this.notificationDetails.notificationEnable;
    this.saveSettings();
  }

  selectNotificationMode(option: string): void {
    if (this.notificationDetails.notificationEnable) {
      this.notificationDetails.bakerNotificationMode = option;
      this.selectedOption = option;
      this.saveSettings();
    }
  }

  back(): void {
    window.history.back()
  }

}
