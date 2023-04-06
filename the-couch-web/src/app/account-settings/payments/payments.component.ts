import { Component, OnInit } from '@angular/core';
import { style, animate, transition, trigger } from '@angular/animations';
import { ClientService } from 'src/app/services/ClientService';
import { CoachService } from 'src/app/services/CoachService';
import { LoginService } from 'src/app/services/LoginService';


@Component({
  selector: 'app-payments',
  templateUrl: './payments.component.html',
  styleUrls: ['./payments.component.scss', '../settings/settings.component.scss', '../profile/profile.component.scss'],
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
export class PaymentsComponent implements OnInit {
  user: any = {};
  paymentDetails: any = {};
  editingSettings = false;
  isSaving = false;
  disableButton = false;
  saveSuccess = false;
  optionFilled = false;
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

  constructor(
  private login: LoginService,
  private coachService: CoachService,
  private notificationsService: ClientService,

  ) { }

  ngOnInit(): void {
    window.scroll(0, 0);
    this.user = JSON.parse(
      sessionStorage.getItem('user') || '{}'
    );
    this.setFields();
  }

  setFields(): void {
    this.paymentDetails = JSON.parse(JSON.stringify(this.user.baker.defaultBakerPaymentDetails));
    if (!this.paymentDetails) {
      this.paymentDetails = {};
      this.paymentDetails.paymentType = 'CASH';
      this.paymentDetails.mpesaPaymentType = 'BUY_GOODS';
      this.paymentDetails.depositPercentage = 50;
    }
  }

  savePaymentSettings(): void {
    this.isSaving = true;
    this.coachService.editPaymentDetails(this.paymentDetails).subscribe(
      (res: any) => {
        // console.log('saveProfileSettings', res);
        this.isSaving = false;
        this.saveSuccess = true;
        this.editingSettings = false;
        window.scroll(0, 0);

        this.getAccount();

        setTimeout(() => {
          this.saveSuccess = false;
        }, 2000);
      },
      (error: any) => {
        console.log(error);

      }
    )
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

  toggleEditingSettings(): void {
    this.editingSettings = !this.editingSettings;
    this.disableButton = true;
    this.setFields();

    setTimeout(() => {
      this.disableButton = false;
    }, 2000);
  }

  percentageValidation(percent: any): void {
    setTimeout(() => {
      this.paymentDetails.depositPercentage = (percent === 0 || percent > 100) ? '50' : percent;
    }, 1);
  }

  selectPaymentOption(): void {
    this.paymentDetails.tillNumber = '';
    this.paymentDetails.businessNumber = '';
    this.paymentDetails.msisdn = '';
    this.paymentDetails.accountNumber = '';
    this.optionFilled = false;
  }

  replaceUnderScoreSymbols(paymentOption: string): string {
    return paymentOption.replace(/_/g, '-');
  }

  back(): void {
    window.history.back()
  }

}
