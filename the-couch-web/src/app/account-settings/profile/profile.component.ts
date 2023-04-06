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
  bakerProfile: any = {};
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

  counties = [{ "id": 30, "name": "BARINGO" }, { "id": 36, "name": "BOMET" }, { "id": 39, "name": "BUNGOMA" }, { "id": 40, "name": "BUSIA" }, { "id": 28, "name": "ELGEYO/MARAKWET" }, { "id": 14, "name": "EMBU" }, { "id": 7, "name": "GARISSA" }, { "id": 43, "name": "HOMA BAY" }, { "id": 11, "name": "ISIOLO" }, { "id": 34, "name": "KAJIADO" }, { "id": 37, "name": "KAKAMEGA" }, { "id": 35, "name": "KERICHO" }, { "id": 22, "name": "KIAMBU" }, { "id": 3, "name": "KILIFI" }, { "id": 20, "name": "KIRINYAGA" }, { "id": 45, "name": "KISII" }, { "id": 42, "name": "KISUMU" }, { "id": 15, "name": "KITUI" }, { "id": 2, "name": "KWALE" }, { "id": 31, "name": "LAIKIPIA" }, { "id": 5, "name": "LAMU" }, { "id": 16, "name": "MACHAKOS" }, { "id": 17, "name": "MAKUENI" }, { "id": 9, "name": "MANDERA" }, { "id": 10, "name": "MARSABIT" }, { "id": 12, "name": "MERU" }, { "id": 44, "name": "MIGORI" }, { "id": 1, "name": "MOMBASA" }, { "id": 21, "name": "MURANG'A" }, { "id": 47, "name": "NAIROBI" }, { "id": 32, "name": "NAKURU" }, { "id": 29, "name": "NANDI" }, { "id": 33, "name": "NAROK" }, { "id": 46, "name": "NYAMIRA" }, { "id": 18, "name": "NYANDARUA" }, { "id": 19, "name": "NYERI" }, { "id": 25, "name": "SAMBURU" }, { "id": 41, "name": "SIAYA" }, { "id": 6, "name": "TAITA TAVETA" }, { "id": 4, "name": "TANA RIVER" }, { "id": 13, "name": "THARAKA-NITHI" }, { "id": 26, "name": "TRANS NZOIA" }, { "id": 23, "name": "TURKANA" }, { "id": 27, "name": "UASIN GISHU" }, { "id": 38, "name": "VIHIGA" }, { "id": 8, "name": "WAJIR" }, { "id": 24, "name": "WEST POKOT" }];



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

    if (this.user.baker ?.bakerSettings ?.logo) {
      this.getLogo();
    }

    // this.notificationForm = JSON.parse(
    //   sessionStorage.getItem('notificationSettings') || '{}'
    // );
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
    this.bakerProfile = JSON.parse(JSON.stringify(this.user.baker));
    delete this.bakerProfile.bakerNotificationSettings;
    delete this.bakerProfile.bakerSettings;
    delete this.bakerProfile.defaultBakerLocation;
    delete this.bakerProfile.defaultBakerPaymentDetails;
    delete this.bakerProfile.subscription;
    delete this.bakerProfile.wallet;

    this.locationDetails = JSON.parse(JSON.stringify(this.user.baker.defaultBakerLocation));
    if (!this.locationDetails) {
      this.locationDetails = {};
      this.locationDetails.county = 'NAIROBI';
    }
    this.logoDetails = JSON.parse(JSON.stringify(this.user.baker.bakerSettings));
    if(!this.logoDetails){
      this.logoDetails={};
    }
    this.imageSrc = this.logo;
  }

  saveProfileSettings(): void {
    this.isSaving = true;
    window.scroll(0, 0);

    this.coachService.editBakerProfile(this.bakerProfile).subscribe(
      (res: any) => {
        // console.log('saveProfileSettings', res);
        this.saveLocationDetails();
      },
      (error: any) => {
        console.log(error);

      }
    )
  }

  saveLocationDetails(): void {
    this.coachService.editLocationDetails(this.locationDetails).subscribe(
      (res: any) => {
        // console.log('saveLocationDetails', res);
        if (this.logoDetails.logo) {
          this.saveLogoDetails();
        }else{
          this.isSaving = false;
          this.saveSuccess = true;
          this.editingSettings = false;

          this.getAccount();

          setTimeout(() => {
            this.saveSuccess = false;
          }, 2000);
        }
      },
      (error: any) => {
        console.log(error);

      }
    )
  }

  saveLogoDetails(): void {

    this.coachService.editBakerLogo(this.logoDetails).subscribe(
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

}
