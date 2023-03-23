import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CoachConfirmComponent } from './coach-confirm.component';

describe('CoachConfirmComponent', () => {
  let component: CoachConfirmComponent;
  let fixture: ComponentFixture<CoachConfirmComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CoachConfirmComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CoachConfirmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
