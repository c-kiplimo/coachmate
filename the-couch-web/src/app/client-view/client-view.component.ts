import { ClientService } from '../services/ClientService';
import { ApiService } from '../services/ApiService';
import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';


import { style, animate, transition, trigger } from '@angular/animations';
import { id } from 'date-fns/locale';
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
  status: any;

  updateClient!: FormGroup;

  constructor(
    private ClientService:ClientService,
    private restApiService:ApiService,
    private router:Router,
    private formbuilder: FormBuilder,
    private activatedRoute: ActivatedRoute)
   { }

  notificationOptions = [false, true];
  notificationType = ['sms', 'email'];
  loadingClient = false;
  totalLength: any;

  
  // page: number = 1;
  // itemsPerPage = 20;
  // ApiService: any;

  clientToBeUpdated!: any;
  ngOnInit(): void {
    this.clientId = this.activatedRoute.snapshot.params['id'];
   
    this.getClientData(this.clientId);

    this.updateClient = this.formbuilder.group({
    
      firstName: ' ',
      lastName: ' ',
      type: ' ',
      msisdn: ' ',
      email_address: ' ',
      physical_address: ' ',
      profession: ' ',
      payment_mode: ' ',
      reason: '',
  
      });

  }

  getClientData(id: any) {
    console.log("Get Client");
    this.loadingClient = true;
    
    this.ClientService.getOneClient(id).subscribe((data) => {
      this.loadingClient = false;
      this.clients = data.body;
      console.log(this.clients);

      this.updateClient = this.formbuilder.group({
       
        firstName: this.clients.firstName,
        lastName: this.clients.lastName,
        type: this.clients.type,
        msisdn: this.clients.msisdn,
        email_address: this.clients.email_address,
        physical_address: this.clients.physical_address,
        profession: this.clients.profession,
        payment_mode: this.clients.payment_mode,
        reason: this.clients.reason,
      });

    });
  }



  updateClientDetails(){
  
    console.log(this.updateClient.value)
    
    
    console.log(this.updateClient.value)
   
    this.ClientService.editClient(this.clientId, this.updateClient.value).subscribe(
      (response) => {
        this.router.navigate(['/clients']);
      }, (error) => {
        console.log(error)
      }
    );
  }

  showStatus: any;

  statusState (currentstatus: any) {
    console.log(currentstatus);
    if (currentstatus === 'ACTIVE') {
      this.showStatus = "ACTIVATE";
      this.status = "ACTIVE";
    } else if (currentstatus === 'SUSPENDED') {
      this.showStatus = "SUSPEND";
      this.status = "SUSPENDED";
    } else if (currentstatus === 'CLOSED') {
      this.showStatus = "CLOSE";
      this.status = "CLOSED";
    }

  }
  changeClientStatus(){
    console.log(this.status);
    if(this.status === "ACTIVE") {
      this.ClientService.changeClientStatus(this.clientId, "ACTIVE").subscribe(
        (response) => {
          this.router.navigate(['/clients']);
        }, (error) => {
          console.log(error)
        }
      );
    }

    if(this.status === "SUSPENDED") {
      this.ClientService.changeClientStatus(this.clientId, "SUSPENDED").subscribe(
        (response) => {
          this.router.navigate(['/clients']);
        }, (error) => {
          console.log(error)
        }
      );
    }

    if(this.status === "CLOSED") {
      this.ClientService.changeClientStatus(this.clientId, "CLOSED").subscribe(
        (response) => {
          this.router.navigate(['/clients']);
        }, (error) => {
          console.log(error)
        }
      );
    }
  }

}
