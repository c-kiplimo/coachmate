import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FeebackFormComponent } from './feeback-form.component';

describe('FeebackFormComponent', () => {
  let component: FeebackFormComponent;
  let fixture: ComponentFixture<FeebackFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FeebackFormComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FeebackFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
