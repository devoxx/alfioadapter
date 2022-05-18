import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InvoiceNumberDetailComponent } from './invoice-number-detail.component';

describe('InvoiceNumber Management Detail Component', () => {
  let comp: InvoiceNumberDetailComponent;
  let fixture: ComponentFixture<InvoiceNumberDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InvoiceNumberDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ invoiceNumber: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InvoiceNumberDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InvoiceNumberDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load invoiceNumber on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.invoiceNumber).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
