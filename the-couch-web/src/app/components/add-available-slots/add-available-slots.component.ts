import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/ApiService';

@Component({
  selector: 'app-add-available-slots',
  templateUrl: './add-available-slots.component.html',
  styleUrls: ['./add-available-slots.component.css']
})
export class AddAvailableSlotsComponent implements OnInit {

  slot = {
    sessionDate: '',
    startTime: '',
    endTime: '',
  };

  user: any;
  coachSessionData: any;
  userRole: any;
  orgId: any;
  coachId: any;

  coachSlots: any;
  loading = false;
  
  page: number = 0;
  pageSize: number = 15;
  totalElements: any;

  constructor(
    private apiService: ApiService,
  ) { }

  ngOnInit(): void {
    this.coachSessionData = sessionStorage.getItem('user');
    this.user = JSON.parse(this.coachSessionData);

    this.userRole = this.user.userRole;
    console.log(this.userRole);


    if (this.userRole == 'ORGANIZATION') {
      this.orgId = this.user.id;
      this.getCoachSlots(this.page);

    } else if (this.userRole == 'COACH') {
      this.coachId = this.user.id;
      this.getCoachSlots(this.page);
    }
  }

  addSlot() {
    console.log(this.slot);
    const options = {
      coachId: this.user.id,
    };
    this.apiService.addSlot(this.slot, options).subscribe({
      next: (response) => {
        console.log(response);
        this.getCoachSlots(this.page);
      }
    });

  }

  getCoachSlots(page: number) {
    this.loading = true;
    const options = {
      page: page,
      size: this.pageSize,
      coachId: this.coachId,
      sort: 'id,desc',
    };
    this.apiService.getCoachSlots(options).subscribe({
      next: (response) => {
        this.loading = false;
        this.coachSlots = response.body;
      }
    });
  }

  deleteSlot(slot: any) {
    if(slot.booked === false) {
      this.loading = true;
    const options = {
      coachId: this.user.id,
      id: slot.id,
    };
    this.apiService.deleteSlot(options).subscribe({
      next: (response) => {
        this.loading = false;
        console.log(response);
        this.getCoachSlots(this.page);
      }
    });
  } else {
    alert('Cannot delete booked slot');
  }
}



}
