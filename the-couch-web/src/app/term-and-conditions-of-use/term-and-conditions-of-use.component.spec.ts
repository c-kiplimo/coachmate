import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TermAndConditionsOfUseComponent } from './term-and-conditions-of-use.component';

describe('TermAndConditionsOfUseComponent', () => {
  let component: TermAndConditionsOfUseComponent;
  let fixture: ComponentFixture<TermAndConditionsOfUseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TermAndConditionsOfUseComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TermAndConditionsOfUseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
