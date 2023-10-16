import { TestBed } from '@angular/core/testing';

import { ParsingDataAroundService } from './parsing-data-around.service';

describe('ParsingDataAroundService', () => {
  let service: ParsingDataAroundService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ParsingDataAroundService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
