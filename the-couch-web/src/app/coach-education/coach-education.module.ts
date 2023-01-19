import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TrainingsListComponent } from './trainings-list/trainings-list.component';
import { AddTrainingComponent } from './add-training/add-training.component';
import { SharedModule } from 'src/shared/shared.module';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';




@NgModule({
    declarations: [
        TrainingsListComponent,
  
    
    ],
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        RouterModule.forChild([
            { path: 'trainings', component: TrainingsListComponent },
            { path: 'AddTraining', component: AddTrainingComponent },
          ]),
        SharedModule

    ]
})
export class CoachEducationModule { }
