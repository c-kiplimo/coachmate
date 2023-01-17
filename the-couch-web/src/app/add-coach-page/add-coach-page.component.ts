import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
// import { CoachService } from '../services/CoachService';


@Component({
  selector: 'app-add-Coach-page',
  templateUrl: './add-coach-page.component.html',
  styleUrls: ['./add-coach-page.component.css']
})
export class AddCoachPageComponent implements OnInit {

  // constructor(private CoachService: CoachService,  private router: Router ) { }

  ngOnInit(): void {
  }

  // Coach = {}
  // AddCoach(AddCoachForm: any) {
  //   console.log(AddCoachForm.value);
  //   this.CoachService.addCoach(AddCoachForm.value).subscribe(
  //     (response) => {
  //       console.log(response)
  //       this.router.navigate(['/Coach']);
  //     }, (error) => {
  //       console.log(error)
  //     }
  //   )
  // }

}
