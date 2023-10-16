import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowSingleCVComponent } from './show-single-cv.component';

describe('ShowSingleCVComponent', () => {
  let component: ShowSingleCVComponent;
  let fixture: ComponentFixture<ShowSingleCVComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowSingleCVComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowSingleCVComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
