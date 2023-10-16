import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddPractisedLanguageComponent } from './add-practised-language.component';

describe('AddPractisedLanguageComponent', () => {
  let component: AddPractisedLanguageComponent;
  let fixture: ComponentFixture<AddPractisedLanguageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddPractisedLanguageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddPractisedLanguageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
