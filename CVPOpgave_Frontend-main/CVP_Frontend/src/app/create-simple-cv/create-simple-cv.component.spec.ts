import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateSimpleCvComponent } from './create-simple-cv.component';

describe('CreateSimpleCvComponent', () => {
  let component: CreateSimpleCvComponent;
  let fixture: ComponentFixture<CreateSimpleCvComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateSimpleCvComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateSimpleCvComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
