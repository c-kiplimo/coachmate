import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RecordPaymentComponent } from './payment/record-payment/record-payment.component';
import { AccountStatementComponent } from './payment/account-statement/account-statement.component';
import { AccountSummaryComponent } from './payment/account-summary/account-summary.component';
import { ReceiptsComponent } from './payment/receipts/receipts.component';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from 'src/shared/shared.module';
import { NgxPaginationModule } from 'ngx-pagination';




@NgModule({
  declarations: [
    RecordPaymentComponent,
    AccountStatementComponent,
    AccountSummaryComponent,
    ReceiptsComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    NgxPaginationModule,
    ReactiveFormsModule,
    RouterModule.forChild([
        { path: 'receipts', component: ReceiptsComponent },
        { path: 'record-payment', component:  RecordPaymentComponent, },
        { path: 'statement', component: AccountStatementComponent },
        { path: 'summary', component:  AccountSummaryComponent, },
      ]),
    SharedModule
  ]
})
export class PaymentModule { }
