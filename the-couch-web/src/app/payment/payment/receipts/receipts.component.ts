import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-receipts',
  templateUrl: './receipts.component.html',
  styleUrls: ['./receipts.component.css']
})
export class ReceiptsComponent implements OnInit {
payment: any;
loadingpayment: any;
savePdf() {
throw new Error('Method not implemented.');
}

  constructor() { }

  ngOnInit(): void {
  }

}
