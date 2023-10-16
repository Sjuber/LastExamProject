import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GetUserCVsComponent } from './get-user-cvs.component';

describe('GetUserCVsComponent', () => {
  let component: GetUserCVsComponent;
  let fixture: ComponentFixture<GetUserCVsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GetUserCVsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GetUserCVsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
