import { ClientService } from '../services/ClientService';
import { ApiService } from '../services/ApiService';
import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import {
  faCaretDown,
  faChevronRight,
  faPlus,
  // faRefresh,
} from '@fortawesome/free-solid-svg-icons';
import { style, animate, transition, trigger } from '@angular/animations';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-client-view',
  templateUrl: './client-view.component.html',
  styleUrls: ['./client-view.component.css',
                ],
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

export class ClientViewComponent implements OnInit {
contracts:any;
sessions:any;
clients:any;
clientId:any;
  console: any;
  itemsPerPage = 20;
  filters: any = {
    status: '',
    searchItem: '',
  };


  constructor(
    private clientService:ClientService,
    private restApiService:ApiService,
    private router:Router,
    private activatedRoute: ActivatedRoute)
   { }

  notificationOptions = [false, true];
  notificationType = ['sms', 'email'];
  loadingClient = false;
  totalLength: any;

  // page: number = 1;
  // itemsPerPage = 20;
  // ApiService: any;


  ngOnInit(): void {
    this.clientId = this.activatedRoute.snapshot.params['id'];
   
    this.getClient();

  }

  getClient() {
    console.log("Get Client");
    this.loadingClient = true;
    
    this.clientService.getOneClient(this.clientId).subscribe((data) => {
      this.loadingClient = false;
      this.clients = data.body;
      console.log(this.clients);
    });
  }


}
