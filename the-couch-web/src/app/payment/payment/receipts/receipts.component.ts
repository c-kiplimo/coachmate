import { Component, OnInit } from '@angular/core';
import { ClientService } from 'src/app/services/ClientService';
import { Router } from '@angular/router';
import { style, animate, transition, trigger } from '@angular/animations';

@Component({
  selector: 'app-receipts',
  templateUrl: './receipts.component.html',
  styleUrls: ['./receipts.component.css'],
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

export class ReceiptsComponent implements OnInit {
payment: any;
payments: any;
loadingpayment: any;
loading = false;
itemsPerPage = 20;
filters: any = {
  status: '',
  searchItem: '',
};
coachSessionData: any;
coachData: any;
userRole: any;


savePdf() {
throw new Error('Method not implemented.');
}

  constructor(
    private ClientService: ClientService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);
    this.userRole = this.coachData.userRole;
    console.log(this.userRole);

    if(this.userRole == 'COACH'){
      this.getPaymentsByCoachId();
    }
  }

  getPaymentsByCoachId(){
    const options = {
      page: 1,
      per_page: this.itemsPerPage,
      status: this.filters.status,
      search: this.filters.searchItem,
      coachId: this.coachData.coach.id,
    };

    this.loading = true;
    this.ClientService.getPaymentsByCoachId(options).subscribe(
      (response) => {
        this.loading = false;
        this.payments = response.body;
        console.log('payments', this.payments);
      }, (error) => {
        console.log(error);
      }
    )



  }

}
