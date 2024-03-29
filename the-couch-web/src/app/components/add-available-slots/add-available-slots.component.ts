import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/ApiService';
import { trigger, transition, style, animate } from '@angular/animations';
import { data } from 'jquery';
import { emptySlots } from './emptySlots';
import { ToastrService } from 'ngx-toastr';
import { CalendlyService } from 'src/app/services/calendly.service';
import { LoginService } from 'src/app/services/LoginService';

@Component({
  selector: 'app-add-available-slots',
  templateUrl: './add-available-slots.component.html',
  styleUrls: ['./add-available-slots.component.css'],
  animations: [
    trigger('fadeIn', [
      transition(':enter', [
        // :enter is alias to 'void => *'
        style({ opacity: 0 }),
        animate(600, style({ opacity: 1 })),
      ]),
    ]),
  ],
})
export class AddAvailableSlotsComponent implements OnInit {

  slots: any = [
  ];

  sunday: any = [];
  monday: any = [];
  tuesday: any = [];
  wednesday: any = [];
  thursday: any = [];
  friday: any = [];
  saturday: any = [];

  combinedSlots: any = [];
  dayOfTheWeeks: any;

  oneSlot = {
    sessionDate: '',
    startTime: '',
    endTime: '',
    dayOfTheWeek: {},
  }

  user: any;
  coachSessionData: any;
  userRole: any;
  orgId: any;
  coachId: any;

  coachSlots: any = [];
  loading = true;
  settingCalendlyUsername = true;
  calendlyUsername = '';

  page: number = 0;
  pageSize: number = 15;
  totalElements: any;


  constructor(
    private apiService: ApiService,
    private toastrService: ToastrService,
    private calendlyService: CalendlyService,
    private loginService: LoginService
  ) { }

  ngOnInit(): void {

    this.coachSessionData = sessionStorage.getItem('user');
    this.user = JSON.parse(this.coachSessionData);

    this.userRole = this.user.userRole;

    this.calendlyUsername = this.user?.calendlyUsername;

    if (this.userRole == 'ORGANIZATION') {
      this.orgId = this.user.id;

    } else if (this.userRole == 'COACH') {
      this.coachId = this.user.id;
    }

    this.getDaysOfWeek();
    

  }

  createCalendlyHtml() {
    let calendlyHtml = this.calendlyService.createCalendlyHtml('Preview your', this.calendlyUsername);

    
    // Step 8: Add the HTML snippet to the DOM.
    // Check if the element with id 'calendly' exists in the document
    const calendlyElement = document.getElementById('calendly');

    if (calendlyElement) {
      // The element exists, so set its innerHTML
      calendlyElement.innerHTML = calendlyHtml;
    } else {
      // The element doesn't exist, handle the error or take appropriate action
      console.error("Element with id 'calendly' not found in the document.");
    }

  }


  submitCalendlyUsername() {
    this.settingCalendlyUsername = true;

    this.loginService.setCalendlyUsername(this.calendlyUsername).subscribe({
      next: (response: any) => {
        console.log(response);
        this.user.calendlyUsername = this.calendlyUsername;
        sessionStorage.setItem('user', JSON.stringify(this.user));
        this.settingCalendlyUsername = false;
        this.toastrService.success('Calendly username saved successfully');
        this.createCalendlyHtml();
      }
    });
  }


  addSlot() {
    //Determines the name of the day of the week of the date entered
    let dayOfTheWeek = new Date(this.oneSlot.sessionDate).toLocaleDateString('en-US', { weekday: 'long' });
    //find the day of the week in the array of days of the week return the object
    let dayOfTheWeekObject = this.dayOfTheWeeks.find((day: any) => day.day === dayOfTheWeek.toUpperCase());
    //add the day of the week object to the slot object
    dayOfTheWeekObject.coach = {};

    this.oneSlot.dayOfTheWeek = dayOfTheWeekObject;

    const options = {
      coachId: this.user.id,
    };

    this.apiService.addSlot(this.oneSlot, options).subscribe({
      next: (response) => {
        this.toastrService.success('Slot added successfully');
        //clear the oneSlot object
        this.oneSlot = {
          sessionDate: '',
          startTime: '',
          endTime: '',
          dayOfTheWeek: {},
        }
        this.getCoachSlots(this.page);
      }
    });

  }

  getCoachSlots(page: number) {
    this.combinedSlots = [];
    this.loading = true;
    const options = {
      page: page,
      size: this.pageSize,
      coachId: this.coachId,
      sort: 'id,desc',
    };
    this.apiService.getCoachSlots(options).subscribe({
      next: (response) => {
        this.processData(response.body);
        this.loading = false;
      }, error: (error) => {
        console.log(error);
        this.loading = false;
      }
    });
  }

  processData(allSlots: any) {

    for (let i = 0; i < allSlots.length; i++) {
      //group the slots by day of the week and add them to the slots array

      let slot = {
        id: allSlots[i].id,
        sessionDate: allSlots[i].sessionDate,
        recurring: allSlots[i].recurring,
        booked: allSlots[i].booked,
        dayOfTheWeek: allSlots[i].dayOfTheWeek,
        time: [
          {
            id: allSlots[i].id,
            booked: allSlots[i].booked,
            startTime: allSlots[i].startTime,
            endTime: allSlots[i].endTime,
          }
        ]
      };

      if (allSlots[i].dayOfTheWeek.day === "SUNDAY") {

        if (this.sunday.length >= 1) {
          this.sunday[0].time.push(slot.time[0]);
        } else {
          this.sunday.push(slot);
        }
      }
      else if (allSlots[i].dayOfTheWeek.day === "MONDAY") {
        if (this.monday.length >= 1) {
          this.monday[0].time.push(slot.time[0]);
        } else {
          this.monday.push(slot);
        }
      }
      else if (allSlots[i].dayOfTheWeek.day === "TUESDAY") {
        if (this.tuesday.length >= 1) {
          this.tuesday[0].time.push(slot.time[0]);
        } else {
          this.tuesday.push(slot);
        }
      }
      else if (allSlots[i].dayOfTheWeek.day === "WEDNESDAY") {
        if (this.wednesday.length >= 1) {
          this.wednesday[0].time.push(slot.time[0]);
        }
        else {
          this.wednesday.push(slot);
        }
      }
      else if (allSlots[i].dayOfTheWeek.day === "THURSDAY") {
        if (this.thursday.length >= 1) {
          this.thursday[0].time.push(slot.time[0]);
        } else {
          this.thursday.push(slot);
        }
      }
      else if (allSlots[i].dayOfTheWeek.day === "FRIDAY") {
        if (this.friday.length >= 1) {
          this.friday[0].time.push(slot.time[0]);
        } else {
          this.friday.push(slot);
        }
      }
      else if (allSlots[i].dayOfTheWeek.day === "SATURDAY") {
        if (this.saturday.length >= 1) {
          this.saturday[0].time.push(slot.time[0]);
        } else {
          this.saturday.push(slot);
        }
      }

    }

    //update the empty slots with the slots that have been booked
    emptySlots.sunday[0].dayOfTheWeek = this.dayOfTheWeeks[0];
    emptySlots.monday[0].dayOfTheWeek = this.dayOfTheWeeks[1];
    emptySlots.tuesday[0].dayOfTheWeek = this.dayOfTheWeeks[2];
    emptySlots.wednesday[0].dayOfTheWeek = this.dayOfTheWeeks[3];
    emptySlots.thursday[0].dayOfTheWeek = this.dayOfTheWeeks[4];
    emptySlots.friday[0].dayOfTheWeek = this.dayOfTheWeeks[5];
    emptySlots.saturday[0].dayOfTheWeek = this.dayOfTheWeeks[6];


    this.combinedSlots.push(this.sunday.length != 0 ? this.sunday : emptySlots.sunday);
    this.combinedSlots.push(this.monday.length != 0 ? this.monday : emptySlots.monday);
    this.combinedSlots.push(this.tuesday.length != 0 ? this.tuesday : emptySlots.tuesday);
    this.combinedSlots.push(this.wednesday.length != 0 ? this.wednesday : emptySlots.wednesday);
    this.combinedSlots.push(this.thursday.length != 0 ? this.thursday : emptySlots.thursday);
    this.combinedSlots.push(this.friday.length != 0 ? this.friday : emptySlots.friday);
    this.combinedSlots.push(this.saturday.length != 0 ? this.saturday : emptySlots.saturday);
    console.log(this.combinedSlots);
    //clear empty slots
    this.sunday = [];
    this.monday = [];
    this.tuesday = [];
    this.wednesday = [];
    this.thursday = [];
    this.friday = [];
    this.saturday = [];
  }

  deleteSlot(slot: any, slotTimes: any, j: number) {
    //if slot is empty, delete it
    if (slot.id == null || slot.id == undefined || slot.id == '') {
      slotTimes.splice(j, 1);
      return;
    }
    if (slot.booked === false) {
      this.loading = true;
      const options = {
        coachId: this.user.id,
        id: slot.id,
      };
      this.apiService.deleteSlot(options).subscribe({
        next: (response) => {
          this.loading = false;
          delete slot.id;
          delete slot.booked;
          delete slot.dayOfTheWeek;
          slot.startTime = '';
          slot.endTime = '';

          //remove index j from slotTimes array
          slotTimes.splice(j, 1);

          this.toastrService.success('Slot deleted successfully');
        }
      });
    } else {
      this.toastrService.error('Cannot delete a booked slot');
    }
  }

  //get days of the week
  getDaysOfWeek() {
    const options = {
      coachId: this.user.id,
    }
    this.apiService.getDaysOfWeek(options).subscribe({
      next: (response: any) => {
        this.loading = false;
        this.dayOfTheWeeks = response.body;
        this.getCoachSlots(this.page);
      }
    });
  }

  saveSlot(slot: any, dayOfTheWeek: any) {
    console.log(slot);
    if (slot.id != null) {
      this.updateSlot(slot, dayOfTheWeek);
    } else {
      dayOfTheWeek.coach = {};
      slot.dayOfTheWeek = dayOfTheWeek;
      const options = {
        coachId: this.user.id,
      };

      this.apiService.addSlot(slot, options).subscribe({
        next: (response) => {
          console.log(response);
          slot.id = response.body.id;
          slot.booked = response.body.booked;
          slot.dayOfTheWeek = response.body.dayOfTheWeek;
          slot.startTime = response.body.startTime;
          slot.endTime = response.body.endTime;

          this.toastrService.success('Slot added successfully');
        }
      });
    }
  }

  updateSlot(slot: any, dayOfTheWeek: any) {
    console.log(slot);
    dayOfTheWeek.coach = {};
    slot.dayOfTheWeek = dayOfTheWeek;

    this.apiService.updateSlot(slot).subscribe({
      next: (response) => {
        console.log(response);
        this.toastrService.success('Changes saved successfully');
      }
    });
  }


  updateDayOfTheWeek(dayOfTheWeek: any) {
    console.log(dayOfTheWeek);
    dayOfTheWeek.coach = {};
    this.apiService.updateDayOfTheWeek(dayOfTheWeek).subscribe({
      next: (response: any) => {
        console.log(response);
        this.toastrService.success('Day of the week updated successfully');
      }
    });
  }

  addDaySlot(slotTimes: any) {
    console.log(slotTimes);
    slotTimes.push({
      booked: false,
      startTime: '',
      endTime: '',
    });
  }

}