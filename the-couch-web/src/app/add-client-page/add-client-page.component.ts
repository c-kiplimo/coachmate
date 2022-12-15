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
  constructor(
    private ClientService: ClientService,
    private router: Router,
    private formbuilder: FormBuilder,
    private toastrService: ToastrService

  ) {}

  ngOnInit(): void {
    this.addClient = this.formbuilder.group({
      firstName:' ',
      lastName:' ',
      clientType:' ',
      msisdn:' ',
      email:' ',
      physicalAddress: ' ',
      profession:' ',
      paymentMode:' ',
      reason:'',
    });
  }

  // Client = {};
  newClient() {
    console.log("add client form=>", this.addClient.value);
    this.ClientService.addClient(this.addClient.value).subscribe(
      (response: any) => {
        console.log(response);
        this.toastrService.success('Client added!', 'Success!');
        this.router.navigate(['/clients']);
      }
    );
  }
}
