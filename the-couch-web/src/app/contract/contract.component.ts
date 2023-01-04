import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { ApiService } from '../services/ApiService';

@Component({
  selector: 'app-contract',
  templateUrl: './contract.component.html',
  styleUrls: ['./contract.component.css']
})
export class ContractComponent implements OnInit {


  addContractForm!: FormGroup;
  createdContract!:any;
  searching = false;
  contracts!: any;

  constructor(
    private activatedRoute: ActivatedRoute,
    private formbuilder: FormBuilder,
    private restApiService:ApiService) { }

  ngOnInit(): void {
    this.addContractForm = this.formbuilder.group({
      coachingTopic:"",
      coachingCategory:"",
      startDate:"",
      endDate:"",
      feesPerPerson:0,
      individualFeesPerSession:0,
      groupFeesPerSession:0,
      noOfSessions:0,
      clientId:0,
      objectives:[],
      sessions:[
          {
              sessionDuration:"",
              name:"",
              sessionType:"",
              notes:"",
              sessionStartTime:"2022-12-12T10:20:30",
              sessionEndTime:"",
              sessionDate:"2022-12-12",
              sessionVenue:"",
              paymentCurrency:"",
              amountPaid:""
          }
      ]
  });
  }

  newContract() {
    console.log(this.addContractForm.value);
    this.restApiService
      .addNewContract(this.addContractForm.value)
      .subscribe((response: any) => {
        this.createdContract = response.body;
      });
  }

  // get payments for specific order
  getContracts(navigate?: boolean): void {
    this.searching = true;

    this.restApiService.getContracts().subscribe((res: any) => {
      this.contracts = res.body.data;
      console.log('Contracts', this.contracts);
      this.searching = false;
    });
  }



}
