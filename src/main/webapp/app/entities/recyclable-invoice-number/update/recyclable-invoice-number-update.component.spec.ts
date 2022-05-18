import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RecyclableInvoiceNumberService } from '../service/recyclable-invoice-number.service';
import { IRecyclableInvoiceNumber, RecyclableInvoiceNumber } from '../recyclable-invoice-number.model';

import { RecyclableInvoiceNumberUpdateComponent } from './recyclable-invoice-number-update.component';

describe('RecyclableInvoiceNumber Management Update Component', () => {
  let comp: RecyclableInvoiceNumberUpdateComponent;
  let fixture: ComponentFixture<RecyclableInvoiceNumberUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let recyclableInvoiceNumberService: RecyclableInvoiceNumberService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RecyclableInvoiceNumberUpdateComponent],
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
      .overrideTemplate(RecyclableInvoiceNumberUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RecyclableInvoiceNumberUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    recyclableInvoiceNumberService = TestBed.inject(RecyclableInvoiceNumberService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const recyclableInvoiceNumber: IRecyclableInvoiceNumber = { id: 456 };

      activatedRoute.data = of({ recyclableInvoiceNumber });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(recyclableInvoiceNumber));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RecyclableInvoiceNumber>>();
      const recyclableInvoiceNumber = { id: 123 };
      jest.spyOn(recyclableInvoiceNumberService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recyclableInvoiceNumber });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recyclableInvoiceNumber }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(recyclableInvoiceNumberService.update).toHaveBeenCalledWith(recyclableInvoiceNumber);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RecyclableInvoiceNumber>>();
      const recyclableInvoiceNumber = new RecyclableInvoiceNumber();
      jest.spyOn(recyclableInvoiceNumberService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recyclableInvoiceNumber });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recyclableInvoiceNumber }));
      saveSubject.complete();

      // THEN
      expect(recyclableInvoiceNumberService.create).toHaveBeenCalledWith(recyclableInvoiceNumber);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RecyclableInvoiceNumber>>();
      const recyclableInvoiceNumber = { id: 123 };
      jest.spyOn(recyclableInvoiceNumberService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recyclableInvoiceNumber });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(recyclableInvoiceNumberService.update).toHaveBeenCalledWith(recyclableInvoiceNumber);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
