import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TrainingsListComponent } from './trainings-list/trainings-list.component';
import { AddTrainingComponent } from './add-training/add-training.component';
import { SharedModule } from 'src/shared/shared.module';
import { RouterModule } from '@angular/router';






@NgModule({
    declarations: [
        TrainingsListComponent,
        AddTrainingComponent, 
    
    ],
    imports: [
        CommonModule,
        RouterModule.forChild([
            { path: 'trainings', component: TrainingsListComponent },
            { path: 'AddTraining', component: AddTrainingComponent },
          ]),
        SharedModule

    ]
})
export class CoachEducationModule { }
