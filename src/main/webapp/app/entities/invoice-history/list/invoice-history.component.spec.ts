import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { InvoiceHistoryService } from '../service/invoice-history.service';

import { InvoiceHistoryComponent } from './invoice-history.component';

describe('InvoiceHistory Management Component', () => {
  let comp: InvoiceHistoryComponent;
  let fixture: ComponentFixture<InvoiceHistoryComponent>;
  let service: InvoiceHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [InvoiceHistoryComponent],
    })
      .overrideTemplate(InvoiceHistoryComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InvoiceHistoryComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(InvoiceHistoryService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.invoiceHistories?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
