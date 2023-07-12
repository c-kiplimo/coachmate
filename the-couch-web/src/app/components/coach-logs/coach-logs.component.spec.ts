import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CoachLogsComponent } from './coach-logs.component';

describe('CoachLogsComponent', () => {
  let component: CoachLogsComponent;
  let fixture: ComponentFixture<CoachLogsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CoachLogsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CoachLogsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
