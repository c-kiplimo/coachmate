import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClientService } from 'src/app/services/ClientService';
@Component({
  selector: 'app-record-payment',
  templateUrl: './record-payment.component.html',
  styleUrls: ['./record-payment.component.css']
})
export class RecordPaymentComponent implements OnInit {
  paymentForm!: FormGroup;
  clients!: '';
  modesOfPayment ='';
  successMessage!: string;

  constructor(
    private formBuilder: FormBuilder,
    private clientService:ClientService
  
  ) {}

  ngOnInit() {
    this.paymentForm = this.formBuilder.group({
      client: ['', Validators.required],
      paymentRef: ['', Validators.required],
      extPaymentRef: [''],
      modeOfPayment: ['', Validators.required],
      amount: ['', [Validators.required, Validators.min(0)]],
    });
  
  }
  }


