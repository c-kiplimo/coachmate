import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ClientService } from '../services/ClientService';
import { Router } from '@angular/router';
@Component({
  selector: 'app-add-objective',
  templateUrl: './contract.component.html',
  styleUrls: ['./contract.component.css']
})
export class contractComponent implements OnInit{
  contractForm!: FormGroup;
  // ClientService: any; 
 
  clients: any;
  sessions:any;
  contracts:any;
  numberOfClients!: number;
  numberOfSessions!:number;
  numberOfContracts!:number;
  rightIcon: any;
  ApiService: any;

  constructor(private clientService : ClientService,
    private formBuilder:FormBuilder, private router:Router) { }

  ngOnInit(): void {
    this.contractForm = this.formBuilder.group(
      {
        coachingCategory:'',
        coachingTopic:'',
        startDate:'',
        endDate:'',
        feesPerPerson:'',
        groupFeesPerson:'',
        individualFeesPerPerson:'',
        noOfSessions:'',
      }
    );
    this.getClients();
    this.getNoOfSessions();
    this.getNoOfContracts();
    this.addContract();
  }
  getClients(){
    this.clientService.getClient().subscribe(
      (response: any) => {
        console.log(response)
        this.clients = response
        this.numberOfClients = this.clients.length;
        console.log(this.numberOfClients)
      }, (error: any) => {
        console.log(error)
      }
    )
  

  }
  getNoOfSessions(){
    this.clientService.getSessions().subscribe(
      (response:any) =>{
        console.log(response)
        this.sessions = response
        this.numberOfSessions = this.sessions.length;
        console.log(this.numberOfSessions)
        
      },
      (error: any) => {
        console.log(error)
      }
    )
  }
  getNoOfContracts(){
    this.clientService.getContracts().subscribe(
      (response:any) =>{
        console.log(response)
        this.contracts = response
        this.numberOfContracts = this.contracts.length;
        console.log(this.numberOfContracts)
        
      },
      (error: any) => {
        console.log(error)
      }
    )
  }
  addContract(){
    console.log('here');
    console.log(this.contractForm.value);
    // this.ApiService.addNewContract(this.contractForm.value).subscribe(
    //   (response: any) => {
        
    //     this.router.navigate(['/contracts']);
    //   }, (error: any) => {
    //     console.log(error)
    //   }
    // )
  }

}
