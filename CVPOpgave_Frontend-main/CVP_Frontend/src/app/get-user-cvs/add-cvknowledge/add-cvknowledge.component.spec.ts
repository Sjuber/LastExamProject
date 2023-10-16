import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddCVKnowledgeComponent } from './add-cvknowledge.component';

describe('AddCVKnowledgeComponent', () => {
  let component: AddCVKnowledgeComponent;
  let fixture: ComponentFixture<AddCVKnowledgeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddCVKnowledgeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddCVKnowledgeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
