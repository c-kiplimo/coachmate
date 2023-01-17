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
  http: any;
  items: any;

  constructor( private router: Router) { }

  ngOnInit(): void {
  }
  deleteItem(item: { id: any; }) {
    this.http.delete(`/api/items/${item.id}`).subscribe(() => {
      this.items = this.items.filter((i: any) => i !== item);
    });
  }

}
