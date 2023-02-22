import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-support',
  templateUrl: './support.component.html',
  styleUrls: ['./support.component.css']
})
export class SupportComponent implements OnInit {
title: any;
openChat() {
throw new Error('Method not implemented.');
}
  contactForm!: FormGroup<any>;
onSubmit() {
throw new Error('Method not implemented.');
}

  constructor() { }

  ngOnInit(): void {
  }

}
