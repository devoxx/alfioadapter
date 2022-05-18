import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InvoiceNumberService } from '../service/invoice-number.service';
import { IInvoiceNumber, InvoiceNumber } from '../invoice-number.model';

import { InvoiceNumberUpdateComponent } from './invoice-number-update.component';

describe('InvoiceNumber Management Update Component', () => {
  let comp: InvoiceNumberUpdateComponent;
  let fixture: ComponentFixture<InvoiceNumberUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let invoiceNumberService: InvoiceNumberService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InvoiceNumberUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(InvoiceNumberUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InvoiceNumberUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    invoiceNumberService = TestBed.inject(InvoiceNumberService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const invoiceNumber: IInvoiceNumber = { id: 456 };

      activatedRoute.data = of({ invoiceNumber });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(invoiceNumber));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InvoiceNumber>>();
      const invoiceNumber = { id: 123 };
      jest.spyOn(invoiceNumberService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceNumber });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: invoiceNumber }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(invoiceNumberService.update).toHaveBeenCalledWith(invoiceNumber);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InvoiceNumber>>();
      const invoiceNumber = new InvoiceNumber();
      jest.spyOn(invoiceNumberService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceNumber });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: invoiceNumber }));
      saveSubject.complete();

      // THEN
      expect(invoiceNumberService.create).toHaveBeenCalledWith(invoiceNumber);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InvoiceNumber>>();
      const invoiceNumber = { id: 123 };
      jest.spyOn(invoiceNumberService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceNumber });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(invoiceNumberService.update).toHaveBeenCalledWith(invoiceNumber);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
