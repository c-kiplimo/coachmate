import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmedViewComponent } from './confirmed-view.component';

describe('ConfirmedViewComponent', () => {
  let component: ConfirmedViewComponent;
  let fixture: ComponentFixture<ConfirmedViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfirmedViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmedViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
