import {
  Component,
  ElementRef,
  HostListener,
  OnInit,
  ViewChild,
} from '@angular/core';
// import { ToastrService } from 'ngx-toastr';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';
import { ClientService } from '../services/ClientService';
import { ApiService } from '../services/ApiService'; 
import {SessionsService }  from '../services/SessionsService';
import { style, animate, transition, trigger } from '@angular/animations';
import { FormBuilder, FormGroup } from '@angular/forms';
import { fromEvent, map, debounceTime, distinctUntilChanged } from 'rxjs';
@Component({
  selector: 'app-add-session',
  templateUrl: './add-session.component.html',
  styleUrls: ['./add-session.component.css'],
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
export class AddSessionComponent implements OnInit {
  addsessionForm!:FormGroup;
  addClient!: FormGroup;
  addSessionForm:any={
    sessionType:"INDIVIDUAL",
    sessionVenue:"VIRTUAL",
  }; 
  firstName: any;
  lastName: any;
  user: any;
  newOrderMessage: any;
  addclientForm!: FormGroup;
  clientId: any;
  client: any;
  searchTerm = '';
  eventType = '';
  addNewClient:any;
  sessionDate = '';
  sessionStartTime = '';
  sessionDuration = '';
  searching = false;
  open = false;
  showHideMessage = true;
  sessionType = [
    
  ];
  cakeSize = [
    'ONE_KG',
    'ONE_HALF_KG',
    'TWO_KG',
    'TWO_HALF_KG',
    'THREE_KG',
    'THREE_HALF_KG',
    'FOUR_KG',
    'FOUR_HALF_KG',
    'FIVE_KG',
  ];
  deliveryOption = ['DELIVERED', 'PICKUP'];
  @ViewChild('yourElement') yourElement!: ElementRef;
  createdclient: any;
  @HostListener('document:click', ['$event']) onClick(event: any) {
    // console.log(event.target.attributes.id.nodeValue);

    if (event.target.attributes && event.target.attributes.id) {
      if (event.target.attributes.id.nodeValue === 'open') {
        this.open = true;
      }
    } else {
      this.open = false;
    }
  }
    constructor(
    private service: ApiService  ,
    private http: HttpClient,
    private ClientService: ClientService,
    private formbuilder: FormBuilder,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private sessionService: ClientService,
    // private toastrService: ToastrService
  ) {}
  ngOnInit(): void {
    this.user = JSON.parse(sessionStorage.getItem('user') || '{}');
    this.newOrderMessage = this.user.bakerNotificationSettings.newOrderTemplate;
    console.log(this.newOrderMessage);

    this.addClient = this.formbuilder.group({
      name: ' ',
      type: ' ',
      msisdn: ' ',
      email_address: ' ',
      physical_address: ' ',
      profession: ' ',
      payment_mode: ' ',
      status: 'ACTIVE',
      reason: '',
      createdBy: ' Amos',
      lastUpdatedBy: ' Amos',
    });
      this.addsessionForm = this.formbuilder.group({
      name: '',
      sessionType: '',
      orderAmount: '',
      amountPaid: '0',
      details: '',
      notes: '',
      deliveryAddress: '',
      dueDate: '',
      clientId: '',
      sendNotification:'',
      attachment: '',
      deliveryOption: '',
      cakeSize: '',
      messageOnCake: '',
    });
  }
 
  ngAfterViewInit(): void {
    fromEvent(this.yourElement.nativeElement, 'input')
      .pipe(map((event: any) => (event.target as HTMLInputElement).value))
      .pipe(debounceTime(1000))
      .pipe(distinctUntilChanged())
      .subscribe((data) => this.getSearchItem());
  }
  addSession () {
    this.sessionService.addSession(this.addSessionForm).subscribe({
      
      next: (response: any) => {
        console.log("no error")
        this.router.navigate(['/sessions']);
        console.log(this.addSessionForm);
      }, 
      error: (error: any) => {
        console.log("error")
      }
    }
      
    )
    
  }
  newSession(addSessionForm: any) {
    const names = this.searchTerm.split(' ');

    this.addSessionForm.patchValue({
      name: names[0] + " " + this.addSessionForm.value.orderType ,
      dueDate: this.orderDueDate + 'T' + this.orderDueTime,
      sendNotification: this.showHideMessage
    });
    console.log('time is', this.orderDueTime);

    this.service
      .addSession(this.addSessionForm.value)
      .subscribe({
        next:(response: any) => {
          this.addSessionForm.reset();
          // this.toastrService.success('Order created!', 'Success!');
          this.router.navigate(['/orders']);
          ;},
        error: (err: any) => {
          console.log(err);
          // this.toastrService.error(
          //   'Order not created, try again!',
          //   'Failed!'
          // );
        },
      });
  }
  getSearchItem(): void {
    this.searching = true;
    console.log('searching');
    const options = {
      page: 1,
      per_page: 10,
      name: this.searchTerm,
    };
    this.service.getClients().subscribe((res: any) => {
      // console.log('clients ni', res.body.data);
      this.client = [];
      this.client = res.body.data;
      this.searching = false;
      console.log('here are client', this.client);
    });
  }

  //add new client
  newclient() {
    console.log(this.addClient.value);
    // this.addclientForm.patchValue({
    //   firstName: this.firstName,
    //   lastName: this.lastName,
    // });
    // console.log(this.firstName, this.lastName);
    this.addClient
      this.addNewClient(this.addClient.value)
      .subscribe((response: any) => {
        this.createdclient = response.body;
        this.searchTerm = this.createdclient.firstName + " " + this.createdclient.lastName;
        // this.toastrService.success('client created!', 'Success!');
        this.addSessionForm.patchValue({
          name: this.createdclient.firstName,
          clientId: this.createdclient.id,
        });
        this.clientId = response.id;
        // alert('client created succesfully');
        this.getSearchItem();
      });
  }
  selectclient(client: any): void {
    // this.client = client.firstName;
    console.log("clicked client", this.client)
    this.addSessionForm.patchValue({
      clientId: client.id,
    });
    this.client = client;
    this.searchTerm = client.firstName + " " + client.lastName;
  }
  displayOrderMessage(client: any) {
    console.log(client);
  }
  clientTyped() {
    const names = this.searchTerm.split(' ');
    this.addClient.patchValue({
      firstName: names[0],
      lastName: names[names.length - 1],
    });
  }
  toggleNotifMessage():void{
    this.showHideMessage = !this.showHideMessage;
  }
  newClient() {
    console.log("add client form=>", this.addClient.value);
    this.ClientService.addClient(this.addClient.value).subscribe(
      (response: any) => {
        console.log(response);
        this.router.navigate(['/clients']);
      }
    );
  }
}

