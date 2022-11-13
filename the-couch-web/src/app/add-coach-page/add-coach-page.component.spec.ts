import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddCoachPageComponent } from './add-coach-page.component';

describe('AddCoachPageComponent', () => {
  let component: AddCoachPageComponent;
  let fixture: ComponentFixture<AddCoachPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddCoachPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddCoachPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
