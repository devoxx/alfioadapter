import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RecyclableInvoiceNumberDetailComponent } from './recyclable-invoice-number-detail.component';

describe('RecyclableInvoiceNumber Management Detail Component', () => {
  let comp: RecyclableInvoiceNumberDetailComponent;
  let fixture: ComponentFixture<RecyclableInvoiceNumberDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RecyclableInvoiceNumberDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ recyclableInvoiceNumber: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RecyclableInvoiceNumberDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RecyclableInvoiceNumberDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load recyclableInvoiceNumber on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.recyclableInvoiceNumber).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
