import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { style, animate, transition, trigger } from '@angular/animations';
import { ToastrService } from 'ngx-toastr';
import { faChevronLeft } from '@fortawesome/free-solid-svg-icons';
import { CoachService } from 'src/app/services/CoachService';
import { LoginService } from 'src/app/services/LoginService';
import { ClientService } from 'src/app/services/ClientService';
import { ApiService } from 'src/app/services/ApiService';

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
  coachSessionData: any;
  userRole: any;
  coachStatus:any;
  editingSettings = false;
  disableButton = false;
  logoDetails: any = {};
  imageSrc: any;
  logo: any;
  saveSuccess = false;
  currentThumbnailUpload: any;
  isSaving = false;
  wrongFileFormat = false;
  message: any;
  uploadSuccess = false;
  uploadFail = false;
  uploadErrorText: any;
  fileTooLarge = false;
  selectedFile!: any;
  thumbnailName: any;
  bannerPresent = false;

  constructor(
    private toastrService: ToastrService,
    private coachService: CoachService,
    private apiService: ApiService
  ) { }

  ngOnInit(): void {
    this.coachSessionData = sessionStorage.getItem('user');
    this.user = JSON.parse(this.coachSessionData);

    this.userRole = this.user.userRole;
    this.coachStatus = this.user.coachStatus;
    console.log(this.userRole);
   console.log(this.coachStatus);
  

  }
  toggleEditingSettings(): void {
    this.editingSettings = !this.editingSettings;
    this.disableButton = true;
    this.setFields();

    setTimeout(() => {
      this.disableButton = false;
    }, 2000);
  }
setFields(): void{
  this.user = JSON.parse(JSON.stringify(this.user))
  delete this.user.userSettings;
  this.logoDetails =JSON.parse(JSON.stringify(this.user.userSettings))
  
  this.imageSrc = this.logo;
  if(!this.logoDetails){
    this.logoDetails={};
  } else {
    this.imageSrc = this.logoDetails?.logoUrl;
  }
}
back(): void {
  window.history.back()
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

  this.apiService.uploadLogo(this.currentThumbnailUpload).subscribe(
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
saveProfileSettings(): void {
  this.isSaving = true;
  window.scroll(0, 0);

  this.apiService.editUserProfile(this.user).subscribe(
    (res: any) => {
      console.log('saveProfileSettings', res);
      // this.saveLocationDetails();
    },
    (error: any) => {
      console.log(error);

    }
  )
}

}
