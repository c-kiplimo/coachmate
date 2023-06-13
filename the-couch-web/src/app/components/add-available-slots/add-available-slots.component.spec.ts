import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddAvailableSlotsComponent } from './add-available-slots.component';

describe('AddAvailableSlotsComponent', () => {
  let component: AddAvailableSlotsComponent;
  let fixture: ComponentFixture<AddAvailableSlotsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddAvailableSlotsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddAvailableSlotsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
