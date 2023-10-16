import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SortingSearchForCvsComponent } from './sorting-search-for-cvs.component';

describe('SortingSearchForCvsComponent', () => {
  let component: SortingSearchForCvsComponent;
  let fixture: ComponentFixture<SortingSearchForCvsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SortingSearchForCvsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SortingSearchForCvsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
