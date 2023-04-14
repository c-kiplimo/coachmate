import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { style, animate, transition, trigger } from '@angular/animations';
import { ToastrService } from 'ngx-toastr';
import { faChevronLeft } from '@fortawesome/free-solid-svg-icons';
import { CoachService } from 'src/app/services/CoachService';
import { LoginService } from 'src/app/services/LoginService';
import { ClientService } from 'src/app/services/ClientService';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss', '../settings/settings.component.scss'],
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
export class ProfileComponent implements OnInit {

  user: any;
  userNotificationSettings: any;
  notificationForm: any;
  backIcon = faChevronLeft;
  coachProfile: any = {};
  filename = '';
  locationDetails: any = {};
  logoDetails: any = {};
  // logo
  selectedFile!: any;
  thumbnailName: any;
  currentThumbnailUpload: any;
  wrongFileFormat = false;
  message: any;
  imageSrc: any;
  uploadSuccess = false;
  uploadFail = false;
  uploadErrorText: any;
  fileTooLarge = false;

  editingSettings = false;
  isSaving = false;
  disableButton = false;
  saveSuccess = false;
  logo: any;

  constructor(
    private notificationsService: ClientService,
    private login: LoginService,
    private http: HttpClient,
    private toastrService: ToastrService,
    private coachService: CoachService
  ) { }

  ngOnInit(): void {
    window.scroll(0, 0);


    this.user = JSON.parse(
      sessionStorage.getItem('user') || '{}'
    );

    this.setFields();

    if (this.user.coach ?.notificationSettings ?.logo) {
      this.getLogo();
    }
  }

  getLogo(): void {
    this.coachService.getLogo().subscribe(
      (res: any) => {
        // console.log(res);
        const reader = new FileReader();
        reader.onload = e => this.logo = reader.result;

        reader.readAsDataURL(res.body);
      }
    )
  }

  setFields(): void {
    this.coachProfile = JSON.parse(JSON.stringify(this.user.coach));
    delete this.coachProfile.coachNotificationSettings;
    delete this.coachProfile.coachSettings;
    delete this.coachProfile.defaultcoachLocation;
    delete this.coachProfile.defaultcoachPaymentDetails;
    delete this.coachProfile.subscription;
    delete this.coachProfile.wallet;
    this.locationDetails = JSON.parse(JSON.stringify(this.user.coach.defaultcoachLocation));
    this.logoDetails = JSON.parse(JSON.stringify(this.user.coach.coachSettings));
    if(!this.logoDetails){
      this.logoDetails={};
    }
    this.imageSrc = this.logo;
  }

  saveProfileSettings(): void {
    this.isSaving = true;
    window.scroll(0, 0);

    this.coachService.editcoachProfile(this.coachProfile).subscribe(
      (res: any) => {

      },
      (error: any) => {
        console.log(error);

      }
    )
  }
  saveLogoDetails(): void {

    this.coachService.editcoachLogo(this.logoDetails).subscribe(
      (res: any) => {
        // console.log('saveLogoDetails', res);
        this.isSaving = false;
        this.saveSuccess = true;
        this.editingSettings = false;

        this.getAccount();

        setTimeout(() => {
          this.saveSuccess = false;
        }, 2000);
      },
      (error: any) => {
        console.log(error);
        this.isSaving = false;

      }
    )
  }

  onThumbnailChange(event: any) {

    this.currentThumbnailUpload = '';
    this.selectedFile = '';
    this.thumbnailName = '';
    this.message = '';
    this.imageSrc = '';
    this.wrongFileFormat = false;


    this.selectedFile = event.target.files;
    this.thumbnailName = this.selectedFile[0] ?.name;
    // console.log(this.thumbnailName);
    this.currentThumbnailUpload = this.selectedFile[0];
    // console.log(this.currentThumbnailUpload);


    if (this.currentThumbnailUpload ?.type.includes('image') ) {


      const reader = new FileReader();
      reader.onload = e => this.imageSrc = reader.result;

      reader.readAsDataURL(this.currentThumbnailUpload);

      this.wrongFileFormat = false;
      this.message = '';

      if (this.currentThumbnailUpload.size > 2000000) {
        this.fileTooLarge = true;
        this.message = 'File is too large to upload! <br> <small><i>Pick another image to upload</i></small>';

      } else {
        this.fileTooLarge = false;
        this.message = '';
        this.submitThumbnail();

      }

    } else {

      if (this.currentThumbnailUpload) {
        this.message = 'You have uploaded a wrong file format !';
        this.wrongFileFormat = true;
      }
    }

  }

  submitThumbnail(): void {
    this.uploadFail = false;
    this.uploadSuccess = false;

    this.coachService.uploadLogo(this.currentThumbnailUpload).subscribe(
      (res: any) => {
        // console.log(res);
        if (res.body ?.status === 'SUCCESS') {
          this.uploadSuccess = true;
          this.uploadFail = false;
          if (res.body) {
            this.logoDetails.logo = res.body.filename;
          }
          // console.log(this.logoDetails);

        } else {
          this.uploadFail = true;
        }

      },
      (error: any) => {
        // console.log(error);
        this.uploadFail = true;

        // setTimeout(() => {
        //   this.uploadFail = false;
        // }, 4000);
      }
    );
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

  getAccount(): void {
    this.login.getAccount().subscribe(
      (response: any) => {
        // console.log(response);
        // console.log('here');

        sessionStorage.setItem('businessName', response.body.firstName);
        sessionStorage.setItem('coachStatus', response.body.coach.status);
        sessionStorage.setItem('user', JSON.stringify(response.body));
        sessionStorage.setItem('notificationSettings', JSON.stringify(response.body.coachNotificationSettings))

        setTimeout(() => {
          location.reload();
        }, 10);

      },
      (error: any) => {

      }
    )
  }

}
