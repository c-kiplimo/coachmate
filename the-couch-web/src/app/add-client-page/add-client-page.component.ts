import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { ClientService } from '../services/ClientService';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-add-client-page',
  templateUrl: './add-client-page.component.html',
  styleUrls: ['./add-client-page.component.css'],
})
export class AddClientPageComponent implements OnInit {
  addClient!: FormGroup;
  firstName: any;
  lastName: any;
  coachData: any;
  couchSessionData: any;
  constructor(
    private ClientService: ClientService,
    private router: Router,
    private formbuilder: FormBuilder,
    private toastrService: ToastrService
  ) {}

  ngOnInit(): void {
     this.couchSessionData = sessionStorage.getItem('user');
     this.coachData = JSON.parse(this.couchSessionData)
    console.log(this.coachData);
    this.addClient = this.formbuilder.group({
      firstName: ' ',
      lastName: ' ',
      clientType: ' ',
      msisdn: ' ',
      email: ' ',
      physicalAddress: ' ',
      profession: ' ',
      paymentMode: ' ',
      reason: '',

    });
  }

  // Client = {};
  newClient() {
    var details = this.addClient.value;
    details.createdBy = this.coachData.fullName;
    details.coach_id = this.coachData.id;
    details.status = 'NEW';
    details.password = '12345678';
    
    console.log(details);

    console.log('add client form=>', details);
    this.ClientService.addClient(details).subscribe(
      (response: any) => {
        console.log(response);
        this.toastrService.success('Client added!', 'Success!');
        this.router.navigate(['/clients']);
      }
    );
  }
}
