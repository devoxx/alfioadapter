import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { RecyclableInvoiceNumberService } from '../service/recyclable-invoice-number.service';

import { RecyclableInvoiceNumberComponent } from './recyclable-invoice-number.component';

describe('RecyclableInvoiceNumber Management Component', () => {
  let comp: RecyclableInvoiceNumberComponent;
  let fixture: ComponentFixture<RecyclableInvoiceNumberComponent>;
  let service: RecyclableInvoiceNumberService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RecyclableInvoiceNumberComponent],
    })
      .overrideTemplate(RecyclableInvoiceNumberComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RecyclableInvoiceNumberComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RecyclableInvoiceNumberService);

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
    expect(comp.recyclableInvoiceNumbers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
