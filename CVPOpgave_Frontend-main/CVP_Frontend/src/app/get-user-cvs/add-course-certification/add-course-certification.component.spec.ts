import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddCourseCertificationComponent } from './add-course-certification.component';

describe('AddCourseCertificationComponent', () => {
  let component: AddCourseCertificationComponent;
  let fixture: ComponentFixture<AddCourseCertificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddCourseCertificationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddCourseCertificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
