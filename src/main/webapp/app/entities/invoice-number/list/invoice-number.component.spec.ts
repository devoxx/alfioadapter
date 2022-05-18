import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { InvoiceNumberService } from '../service/invoice-number.service';

import { InvoiceNumberComponent } from './invoice-number.component';

describe('InvoiceNumber Management Component', () => {
  let comp: InvoiceNumberComponent;
  let fixture: ComponentFixture<InvoiceNumberComponent>;
  let service: InvoiceNumberService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [InvoiceNumberComponent],
    })
      .overrideTemplate(InvoiceNumberComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InvoiceNumberComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(InvoiceNumberService);

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
    expect(comp.invoiceNumbers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
