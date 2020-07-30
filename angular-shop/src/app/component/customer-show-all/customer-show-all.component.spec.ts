import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerShowAllComponent } from './customer-show-all.component';

describe('CustomerShowAllComponent', () => {
  let component: CustomerShowAllComponent;
  let fixture: ComponentFixture<CustomerShowAllComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CustomerShowAllComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomerShowAllComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
