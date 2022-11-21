import { Component, OnInit } from '@angular/core';
import { ClientService } from '../services/ClientService';


@Component({
  selector: 'app-add-client-page',
  templateUrl: './add-client-page.component.html',
  styleUrls: ['./add-client-page.component.css']
})
export class AddClientPageComponent implements OnInit {

  constructor(private ClientService: ClientService) { }

  ngOnInit(): void {
  }

  Client = {}
  AddClient(AddClientForm: any) {
    console.log(AddClientForm.value);
    this.ClientService.addClient(AddClientForm.value).subscribe(
      (response) => {
        console.log(response)
      }, (error) => {
        console.log(error)
      }
    )
  }

}
