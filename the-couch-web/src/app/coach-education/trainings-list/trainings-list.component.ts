import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
@Component({
  selector: 'app-trainings-list',
  templateUrl: './trainings-list.component.html',
  styleUrls: ['./trainings-list.component.css']
})
export class TrainingsListComponent implements OnInit {
numberOfHours: any;
numberOfMinutes: any;

  constructor( private router: Router) { }

  ngOnInit(): void {
  }

}
