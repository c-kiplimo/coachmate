import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ClientService } from '../services/ClientService';


@Component({
  selector: 'app-add-client-page',
  templateUrl: './add-client-page.component.html',
  styleUrls: ['./add-client-page.component.css']
})
export class AddClientPageComponent implements OnInit {

  constructor(private ClientService: ClientService,  private router: Router ) { }

  ngOnInit(): void {
  }

  Client = {}
  AddClient(AddClientForm: any) {
    console.log(AddClientForm.value);
    this.ClientService.addClient(AddClientForm.value).subscribe(
      (response) => {
        console.log(response)
        this.router.navigate(['/clients']);
      }, (error) => {
        console.log(error)
      }
    )
  }

}


