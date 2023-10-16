import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Directive } from '@angular/core';
import { UsersProfileStartsiteComponent } from './users-profile-startsite.component';

describe('UsersProfileStartsiteComponent', () => {
  let component: UsersProfileStartsiteComponent;
  let fixture: ComponentFixture<UsersProfileStartsiteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UsersProfileStartsiteComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UsersProfileStartsiteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
