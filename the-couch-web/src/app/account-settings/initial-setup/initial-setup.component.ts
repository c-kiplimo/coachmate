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
  counties = [{ "id": 30, "name": "BARINGO" }, { "id": 36, "name": "BOMET" }, { "id": 39, "name": "BUNGOMA" }, { "id": 40, "name": "BUSIA" }, { "id": 28, "name": "ELGEYO/MARAKWET" }, { "id": 14, "name": "EMBU" }, { "id": 7, "name": "GARISSA" }, { "id": 43, "name": "HOMA BAY" }, { "id": 11, "name": "ISIOLO" }, { "id": 34, "name": "KAJIADO" }, { "id": 37, "name": "KAKAMEGA" }, { "id": 35, "name": "KERICHO" }, { "id": 22, "name": "KIAMBU" }, { "id": 3, "name": "KILIFI" }, { "id": 20, "name": "KIRINYAGA" }, { "id": 45, "name": "KISII" }, { "id": 42, "name": "KISUMU" }, { "id": 15, "name": "KITUI" }, { "id": 2, "name": "KWALE" }, { "id": 31, "name": "LAIKIPIA" }, { "id": 5, "name": "LAMU" }, { "id": 16, "name": "MACHAKOS" }, { "id": 17, "name": "MAKUENI" }, { "id": 9, "name": "MANDERA" }, { "id": 10, "name": "MARSABIT" }, { "id": 12, "name": "MERU" }, { "id": 44, "name": "MIGORI" }, { "id": 1, "name": "MOMBASA" }, { "id": 21, "name": "MURANG'A" }, { "id": 47, "name": "NAIROBI" }, { "id": 32, "name": "NAKURU" }, { "id": 29, "name": "NANDI" }, { "id": 33, "name": "NAROK" }, { "id": 46, "name": "NYAMIRA" }, { "id": 18, "name": "NYANDARUA" }, { "id": 19, "name": "NYERI" }, { "id": 25, "name": "SAMBURU" }, { "id": 41, "name": "SIAYA" }, { "id": 6, "name": "TAITA TAVETA" }, { "id": 4, "name": "TANA RIVER" }, { "id": 13, "name": "THARAKA-NITHI" }, { "id": 26, "name": "TRANS NZOIA" }, { "id": 23, "name": "TURKANA" }, { "id": 27, "name": "UASIN GISHU" }, { "id": 38, "name": "VIHIGA" }, { "id": 8, "name": "WAJIR" }, { "id": 24, "name": "WEST POKOT" }];
  settingsObject: any;
  bakerObject: any;
  submittingDetails = false;
  submitSuccess = false;
  submitFail = false;
  optionFilled = false;

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
    // const textArea = document.getElementById("id") as HTMLElement;
    //
    // // will get the value of the text area
    // let x = $('#text1').val();
    //
    // // setting the updated value in the text area
    // $('#text1').val(x.slice(0, curPos) + text_to_insert + x.slice(curPos));
  }

  getNotificationTemplates(): void {
    this.settingsObject = JSON.parse(sessionStorage.getItem('notificationSettings') || '{}');
    this.setupDetails.newOrderTemplate = this.settingsObject.newOrderTemplate;
    this.setupDetails.partialOrderPaymentTemplate = this.settingsObject.partialOrderPaymentTemplate;
    this.setupDetails.fullOrderPaymentTemplate = this.settingsObject.fullOrderPaymentTemplate;
    this.setupDetails.deliverOrderTemplate = this.settingsObject.deliverOrderTemplate;
    this.setupDetails.cancelOrderTemplate = this.settingsObject.cancelOrderTemplate;
    this.setupDetails.paymentReminderTemplate = this.settingsObject.paymentReminderTemplate;
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
