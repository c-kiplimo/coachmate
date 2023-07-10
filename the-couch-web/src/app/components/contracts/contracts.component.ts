import { Component, OnInit } from '@angular/core';
import { ClientService } from '../../services/ClientService';
import { style, animate, transition, trigger } from '@angular/animations';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { ContractsService } from 'src/app/services/contracts.service';
import { options } from '@mobiscroll/angular';

@Component({
  selector: 'app-contract',
  templateUrl: './contracts.component.html',
  styleUrls: ['./contracts.component.css'],
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
export class contractComponent implements OnInit {

  loading = false;
  contracts!: any;
  contractId: any;
  userRole: any;
  contract:any;
  user: any;
  coachSessionData: any;

  coachId: any;
  orgId: any;
  clientId: any;

  filters: any = {
    status: '',
    searchItem: '',
  };

  page: number = 0;
  pageSize: number = 15;
  totalElements: any;
  

  constructor(private clientService: ClientService,
    private router: Router,
    private route: ActivatedRoute,
    private contractsService: ContractsService) {}

  ngOnInit(): void {
    this.coachSessionData = sessionStorage.getItem('user');
    this.user = JSON.parse(this.coachSessionData);

    this.userRole = this.user.userRole;
    console.log(this.userRole);


    if (this.userRole == 'ORGANIZATION') {
      this.orgId = this.user.id;
      this.getAllContracts(this.page);

    } else if (this.userRole == 'COACH') {
      this.coachId = this.user.id;
      this.getAllContracts(this.page);

    } else if (this.userRole == 'CLIENT') {
      this.clientId = this.user.id;
      this.getAllContracts(this.page);
    }
  }

  navigateToTerms(id: any) {
    console.log("contractId on navigate",id);
    this.contractId = id;
    if(this.userRole == 'COACH'){

    this.router.navigate(['/contractDetail', id]);
    } else if (this.userRole == 'CLIENT'&& id.contractStatus==null) {
      this.router.navigate(['/terms', id]);
    }else(this.userRole == 'CLIENT'&& id.contractStatus=="SIGNED")
    this.router.navigate(['/contractDetail', id]);


  }

  getAllContracts(page: any) {

    this.loading = true;
    this.page = page;
    //if page is 0, don't subtract 1
    if (page === 0 || page < 0) {
      page = 0;
    } else {
      page = page - 1;
    }

    const options: any = {
      page: page,
      size: this.pageSize,
      sessionStatus: this.filters.status,
      search: this.filters.searchItem,
      sort: 'id,desc',
    };

    if(this.userRole == 'COACH'){
      options.coachId = this.coachId;
    }else if(this.userRole == 'CLIENT'){
      options.clientId = this.clientId;
    }else if(this.userRole == 'ORGANIZATION'){
      //options.orgId = this.orgId;
      options.coachId = this.coachId;
    }

    this.contractsService.getContracts(options).subscribe(
      (res: any) => {
        console.log("res",res);
        this.contracts = res.body;
        this.totalElements = +res.headers.get('X-Total-Count');
        this.loading = false;
      }, (err: any) => {
        console.log(err);
        this.loading = false;
      }
    );

  }

  onTableDataChange(event: any) {
    this.page = event;
    this.getAllContracts(this.page);
  }

}
