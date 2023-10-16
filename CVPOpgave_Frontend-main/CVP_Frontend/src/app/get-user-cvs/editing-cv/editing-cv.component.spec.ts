import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditingCVComponent } from './editing-cv.component';

describe('EditingCVComponent', () => {
  let component: EditingCVComponent;
  let fixture: ComponentFixture<EditingCVComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditingCVComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditingCVComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
