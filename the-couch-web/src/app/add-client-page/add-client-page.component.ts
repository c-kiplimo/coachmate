import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { ClientService } from '../services/ClientService';

@Component({
  selector: 'app-add-client-page',
  templateUrl: './add-client-page.component.html',
  styleUrls: ['./add-client-page.component.css'],
})
export class AddClientPageComponent implements OnInit {
  addClient!: FormGroup;
  firstName: any;
  lastName: any;
  constructor(
    private ClientService: ClientService,
    private router: Router,
    private formbuilder: FormBuilder
  ) {}

  ngOnInit(): void {
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
  }

  // Client = {};
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
