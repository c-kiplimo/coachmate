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

  notificationTemplateTypes = [
    'NEW_CONTRACT ',
    'PARTIAL_BILL_PAYMENT',
    'FULL_BILL_PAYMENT',
    'CANCELLED_SESSION',
    'CONDUCTED_SESSION',
    'RESCHEDULED_SESSION'
  ];
  template!: any;
  notificationForm: any;
  accountDetailsform:any;
  saveChanges = true;
  message: string = 'NEW CONTRACT';
  businessName!: any;
  fullNames!: any;
  NotificationSettings: any;
  coachPhoneNumber:any;
  coachEmail:any;
  backIcon = faChevronLeft;
  user:any;
  coachSessionData:any;
  coachData: any;
  userRole: any;
  OrgData: any;
  orgSession: any;
  User: any;
  orgId: any;

  constructor(
    private formbuilder: FormBuilder,
    private service: ClientService,
    private http: HttpClient,
    private toastrService: ToastrService
  ) {}
  ngOnInit(): void {
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);
    this.userRole = this.coachData.userRole;
    console.log(this.userRole);



    this.coachEmail=  this.coachData.email;
    this.coachPhoneNumber= this.coachData.msisdn;
    this.notificationForm = this.NotificationSettings;
  
    if(this.userRole == 'COACH'){
    this.getUser();
    this.fullNames= this.coachData.fullName;
    } else if(this.userRole == 'ORGANIZATION'){
      this.OrgData = sessionStorage.getItem('Organization');
      this.orgId = this.coachData.organization.id;
      console.log("organization id",this.orgId);
      this.fullNames= this.coachData.organization.fullName;
     
    }else if(this.userRole == 'CLIENT') {
      console.log('not coach');
      this.getUser();
      this.fullNames = this.coachData.fullName;
      
    }
    this.notificationForm = this.formbuilder.group({
      notificationMode: [''],
      notificationEnable: [''],
    });



    
  }
  getUser() {
    this.User = JSON.parse(sessionStorage.getItem('user') as any);
    console.log(this.User);
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