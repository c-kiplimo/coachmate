import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { faChevronLeft } from '@fortawesome/free-solid-svg-icons';
import { ToastrService } from 'ngx-toastr';
import { ClientService } from '../services/ClientService';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  orderTemplateTypes = [
    'NEW_ORDER ',
    'PARTIAL_ORDER_PAYMENT',
    'FULL_ORDER_PAYMENT',
    'CANCEL_ORDER',
    'DELIVER_ORDER',
  ];

  newOrderForm!: any;
  template!: any;
  notificationForm: any;
  accountDetailsform:any;
  saveChanges = true;
  message: string = 'NEW ORDER';
  businessName!: any;
  userNotificationSettings: any;
  bakerPhoneNumber:any;
  bakerEmail:any;
  backIcon = faChevronLeft;
  user:any;

  constructor(
    private formbuilder: FormBuilder,
    private service: ClientService,
    private http: HttpClient,
    private toastrService: ToastrService
  ) {}

  ngOnInit(): void {
    this.user =  JSON.parse(
      sessionStorage.getItem('user') || '{}'
    );
    this.userNotificationSettings = JSON.parse(
      sessionStorage.getItem('notificationSettings') || '{}'
    );
    this.bakerEmail=  this.user.email; 
    this.bakerPhoneNumber= this.userNotificationSettings.msisdn;
    this.notificationForm = this.userNotificationSettings;
    // this.accountDetailsform = this.bakerAccountDetails;

    // console.log(this.notificationForm)
    // this.businessName = sessionStorage.getItem('businessName');
    // this.notificationForm.smsDisplayName = this.businessName;
    // this.notificationForm.emailDisplayName = this.businessName;
    // // console.log(this.businessName);
  }

  viewTemplate(template: any): void {
    this.template = template;
  }
  saveSettings() {
    this.service.saveSettings(this.notificationForm).subscribe({
      next: (res) => {
        const data = res.body;
        console.log('here', res.body);
        this.toastrService.success('Settings updated', 'Success!');
        sessionStorage.removeItem('notificationSettings');
        sessionStorage.setItem('notificationSettings', JSON.stringify(data));

        setTimeout(() => {
          location.reload();
        }, 3);
        console.log(res);
      },
      error: (err) => {
        console.log(err);
        this.toastrService.error('Settings not saved, try again!', 'Failed!');
      },
    });
  }
  back(){
    window.history.back()
  }
}
