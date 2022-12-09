import { ComponentFixture, TestBed } from '@angular/core/testing';

import { contractComponent } from './contract.component';

describe('ContractComponent', () => {
  let component: contractComponent;
  let fixture: ComponentFixture<contractComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ contractComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(contractComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
