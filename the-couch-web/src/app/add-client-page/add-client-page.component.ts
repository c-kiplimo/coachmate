import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { ClientService } from '../services/ClientService';

@Component({
  selector: 'app-add-client-page',
  templateUrl: './add-client-page.component.html',
  styleUrls: ['./add-client-page.component.css']
})
export class AddClientPageComponent implements OnInit {
addClient!: FormGroup;
firstName: any;
lastName: any;
  constructor(private ClientService: ClientService,  private router: Router,
    private formbuilder: FormBuilder, ) { }

  ngOnInit(): void {
    this.addClient= this.formbuilder.group({
      firstName: '',
      lastName: '',
      msisdn: '',
      email: '',
      physicalAddress: '',
    });
    this.newClient();
  }

  Client = {}
  newClient() {
    console.log(this.addClient.value);
    this.addClient;
      (this.addClient.value)
      .subscribe((response: any) => {
        console.log(response);
        this.router.navigate(['/clients']);
      });
  }

}


