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
  coachSessionData: any;
  coachData: any;

  coachSlots: any;

  constructor(
    private apiService: ApiService,
  ) { }

  ngOnInit(): void {
    //get user session
    this.coachSessionData = sessionStorage.getItem('user'); 
    this.coachData = JSON.parse(this.coachSessionData);
    console.log(this.coachData);

    this.getCoachSlots();

  }

  addSlot() {
    console.log(this.slot);
    const options = {
      coachId: this.coachData.coach.id,
    };
    this.apiService.addSlot(this.slot, options).subscribe({
      next: (response) => {
        console.log(response);
        this.getCoachSlots();
      }
    });

  }

  getCoachSlots() {
    const coachId = this.coachData.coach.id;
    const options = {
    };
    this.apiService.getCoachSlots(coachId, options).subscribe({
      next: (response) => {
        this.coachSlots = response.body;
      }
    });
  }

  deleteSlot(slot: any) {
    if(slot.booked === false) {
    const options = {
      coachId: this.coachData.coach.id,
      id: slot.id,
    };
    this.apiService.deleteSlot(options).subscribe({
      next: (response) => {
        console.log(response);
        this.getCoachSlots();
      }
    });
  } else {
    alert('Cannot delete booked slot');
  }
}



}
