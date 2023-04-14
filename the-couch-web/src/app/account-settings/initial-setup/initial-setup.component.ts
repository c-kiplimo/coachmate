import { Component, OnInit } from '@angular/core';
import { style, animate, transition, trigger } from '@angular/animations';
import { Router, ActivatedRoute } from '@angular/router';

import { ClientService } from 'src/app/services/ClientService';
import { CoachService } from 'src/app/services/CoachService';

@Component({
  selector: 'app-initial-setup',
  templateUrl: './initial-setup.component.html',
  styleUrls: ['./initial-setup.component.scss', ],
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
export class InitialSetupComponent implements OnInit {


  errorMessage = '';
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
  step = 1;
  paymentOptions = [
    {
      name: 'Buy goods / Till number',
      value: 'BUY_GOODS'
    },
    {
      name: 'Paybill no.',
      value: 'PAY_BILL'
    },
    {
      name: 'Mobile money',
      value: 'SEND_MONEY'
    },
    {
      name: 'Pochi la biashara',
      value: 'POCHI_LA_BIASHARA'
    }
  ];
  optionLabel = '';
  setupDetails: any = {
    county: 'NAIROBI',
    depositPercentage: '50',
    paymentType: 'MPESA',
    mpesaPaymentType: 'BUY_GOODS'
  };
  notificationTemplates: any = [];
  bakerObject: any;
  submittingDetails = false;
  submitSuccess = false;
  submitFail = false;
  optionFilled = false;
  settingsObject: any;

  constructor(
    private notificationsService: ClientService,
    private coachService: CoachService,
    private router: Router
  ) { }

  ngOnInit(): void {
    window.scroll(0,0);
    this.getNotificationTemplates();
    this.bakerObject = JSON.parse(sessionStorage.getItem('user') || '{}');
    this.setupDetails.bakerId = this.bakerObject.baker.id;
  }

  navigateWizardSteps(movement: string): void {
    this.step = movement === 'back' ? (this.step -= 1) : this.step += 1;

    // if (this.step === 2 && this.currentThumbnailUpload && !this.fileTooLarge) {
    //   this.submitThumbnail();
    // }
    window.scroll(0,0);

    if (this.step === 3) {
      this.submitSuccess = false;
      this.submitFail = false;
    }

    if ((!this.setupDetails.depositPercentage || +this.setupDetails.depositPercentage === 0) && this.step === 4) {
      this.setupDetails.depositPercentage = '50';
      // console.log(this.setupDetails.depositPercentage);
    }
  }

  toDashboard(): void {
    this.router.navigate(['/dashboard']);
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

      if (this.currentThumbnailUpload.size > 5000000) {
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
          this.setupDetails.filename = res.body.filename;
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

  selectPaymentOption(): void {
    this.setupDetails.tillNumber = '';
    this.setupDetails.businessNumber = '';
    this.setupDetails.msisdn = '';
    this.setupDetails.accountNumber = '';
    this.optionFilled = false;
  }

  // checkIfMpesaFieldsAreEmpty():void{
  //   if ((this.mpesaPaymentType === 'BUY_GOODS' && !this.setupDetails.tillNumber) || this.mpesaPaymentType === '' ) {
  //
  //   }
  // }

  replaceUnderScoreSymbols(paymentOption: string): string {
    return paymentOption.replace(/_/g, '-');
  }

  percentageValidation(percent: any): void {
    setTimeout(() => {
      this.setupDetails.depositPercentage = (percent === 0 || percent > 100) ? '50' : percent;
    }, 1);
  }

  insertTextForTemplates(text: string): void {
  }

  getNotificationTemplates(): void {
    this.settingsObject = JSON.parse(sessionStorage.getItem('notificationSettings') || '{}');
    this.setupDetails.paymentReminderTemplate = this.settingsObject.paymentReminderTemplate;
    this.setupDetails.paymentConfirmationTemplate = this.settingsObject.paymentConfirmationTemplate;
    this.setupDetails.fullBillPaymentTemplate = this.settingsObject.fullBillPaymentTemplate;
    this.setupDetails.partialBillPaymentTemplate = this.settingsObject.partialBillPaymentTemplate;
    this.setupDetails.cancelSessionTemplate = this.settingsObject.cancelSessionTemplate;
    this.setupDetails.sessionReminderTemplate = this.settingsObject.sessionReminderTemplate;
    this.setupDetails.sessionConfirmationTemplate = this.settingsObject.sessionConfirmationTemplate;

  }

  resetTemplate(template: string): void {
    this.setupDetails[template] = this.settingsObject[template];
  }

  submitBakerDetails(): void {
    this.submittingDetails = true;
    this.submitSuccess = false;
    this.submitFail = false;

    this.coachService.onboardBaker(this.setupDetails).subscribe(
      (res: any) => {
        console.log(res);

        this.submittingDetails = false;
        this.submitSuccess = true;
        this.step = 4;

      },
      (error: any) => {
        this.submittingDetails = false;
        this.submitFail = true;
        this.step = 4;

        console.log(error);

      }
    )
  }

}
