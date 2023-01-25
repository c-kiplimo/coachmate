import { Component, OnInit } from '@angular/core';
import { ClientService } from '../services/ClientService';
import { SessionsService } from '../services/SessionsService';
import { style, animate, transition, trigger } from '@angular/animations';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import {
  faCaretDown,
  faChevronRight,
  faPlus,
  faRedo,
  // faRefresh,
} from '@fortawesome/free-solid-svg-icons';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-session-view',
  templateUrl: './session-view.component.html',
  styleUrls: ['./session-view.component.css'],
  
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
export class sessionViewComponent implements OnInit {
  paymentForm!: FormGroup;
  
  caretDown = faCaretDown;
  addIcon = faPlus;
  rightIcon = faChevronRight;
  refreshIcon =faRedo;
  payments!: any;
  notifications!: any;
  notification!: any;
  attachments!: any;
 
  payment!: any;
  order!: any;
  response_data!: any;
  searching = false;
  currentTab = 'payments';
  loading = false;

  service: any;
  sessions:any;
  session:any;

  itemsPerPage = 20;
  filters: any = {
    status: '',
    searchItem: '',
  };
  sessionId!: any;



  constructor(
    private restApiService:ClientService,
    private http: HttpClient,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private formbuilder: FormBuilder,
    private sessionService:SessionsService 
  ) {}

  ngOnInit() {
   this.activatedRoute.params.subscribe((params) => {
      this.sessionId = params['id'];
    });
    this.getSession();
  //  this.getAllSessions();

    this.paymentForm = this.formbuilder.group({
      extPaymentRef: '',
      amount: '',
      narration: '',
    
      sendNotification: true,
    });
  
  
  }

  toggleTab(tab: string): void {
    this.currentTab = tab;
  }
  // getAllSessions(){

  //   const options = {
  //     page: 1,
  //     per_page: this.itemsPerPage,
  //     status: this.filters.status,
  //     search: this.filters.searchItem,
  //   };
  //   this.restApiService.getSessions(options).subscribe(
  //     (response: any) => {
  //       console.log(response)
      
  //     }, (error: any) => {
  //       console.log(error)
  //     }
  //   )
  // }
  getSession() {
    this.loading = true;

    this.sessionService.getOneSession(this.sessionId).subscribe((res: any) => {
      
      this.loading = false;
      this.sessions = res.body;
      console.log(this.sessions);
   
    });
  }


}
