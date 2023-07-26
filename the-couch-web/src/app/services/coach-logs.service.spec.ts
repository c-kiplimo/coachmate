import { TestBed } from '@angular/core/testing';

import { CoachLogsService } from './coach-logs.service';

describe('CoachLogsService', () => {
  let service: CoachLogsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CoachLogsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
