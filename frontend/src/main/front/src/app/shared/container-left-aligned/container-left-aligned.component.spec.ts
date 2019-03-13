import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContainerLeftAlignedComponent } from './container-left-aligned.component';

describe('ContainerLeftAlignedComponent', () => {
  let component: ContainerLeftAlignedComponent;
  let fixture: ComponentFixture<ContainerLeftAlignedComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContainerLeftAlignedComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContainerLeftAlignedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
