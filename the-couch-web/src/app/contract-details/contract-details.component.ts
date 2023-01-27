import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ClientService } from '../services/ClientService';

@Component({
  selector: 'app-contract-details',
  templateUrl: './contract-details.component.html',
  styleUrls: ['./contract-details.component.css']
})
export class ContractDetailsComponent implements OnInit {
sessions: any;

editSession(_t44: any) {
throw new Error('Method not implemented.');
}
deleteSession(_t44: any) {
throw new Error('Method not implemented.');
}
addSession() {
throw new Error('Method not implemented.');
}
modalTitle: any;
sessionDate: any;
sessionTime: any;
sessionGoals: any;
session: any;
id:any;
contract:any;
contractId!: number;
closeModal() {
throw new Error('Method not implemented.');
}

  constructor(private clientService: ClientService,
    private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const id = params['id'];
    this.contract = this.clientService.getContract(id).subscribe((data: any) => {
      this.contract = data.body;
      console.log(this.contract);
      console.log("contract details");
  }
  )}
    )}
  }

