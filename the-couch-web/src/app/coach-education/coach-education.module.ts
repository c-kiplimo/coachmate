import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TrainingsListComponent } from './trainings-list/trainings-list.component';
import { AddTrainingComponent } from './add-training/add-training.component';
import { AppModule } from "../app.module";



@NgModule({
    declarations: [
        TrainingsListComponent,
        AddTrainingComponent
    ],
    imports: [
        CommonModule,
        AppModule,
    
    ]
})
export class CoachEducationModule { }
