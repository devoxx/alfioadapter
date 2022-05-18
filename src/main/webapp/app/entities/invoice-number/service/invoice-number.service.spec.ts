import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInvoiceNumber, InvoiceNumber } from '../invoice-number.model';

import { InvoiceNumberService } from './invoice-number.service';

describe('InvoiceNumber Service', () => {
  let service: InvoiceNumberService;
  let httpMock: HttpTestingController;
  let elemDefault: IInvoiceNumber;
  let expectedResult: IInvoiceNumber | IInvoiceNumber[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InvoiceNumberService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      creationDate: currentDate,
      invoiceNumber: 0,
      eventId: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          creationDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a InvoiceNumber', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          creationDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          creationDate: currentDate,
        },
        returnedFromService
      );

      service.create(new InvoiceNumber()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InvoiceNumber', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          creationDate: currentDate.format(DATE_TIME_FORMAT),
          invoiceNumber: 1,
          eventId: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          creationDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InvoiceNumber', () => {
      const patchObject = Object.assign(
        {
          creationDate: currentDate.format(DATE_TIME_FORMAT),
          eventId: 'BBBBBB',
        },
        new InvoiceNumber()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          creationDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InvoiceNumber', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          creationDate: currentDate.format(DATE_TIME_FORMAT),
          invoiceNumber: 1,
          eventId: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          creationDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a InvoiceNumber', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInvoiceNumberToCollectionIfMissing', () => {
      it('should add a InvoiceNumber to an empty array', () => {
        const invoiceNumber: IInvoiceNumber = { id: 123 };
        expectedResult = service.addInvoiceNumberToCollectionIfMissing([], invoiceNumber);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(invoiceNumber);
      });

      it('should not add a InvoiceNumber to an array that contains it', () => {
        const invoiceNumber: IInvoiceNumber = { id: 123 };
        const invoiceNumberCollection: IInvoiceNumber[] = [
          {
            ...invoiceNumber,
          },
          { id: 456 },
        ];
        expectedResult = service.addInvoiceNumberToCollectionIfMissing(invoiceNumberCollection, invoiceNumber);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InvoiceNumber to an array that doesn't contain it", () => {
        const invoiceNumber: IInvoiceNumber = { id: 123 };
        const invoiceNumberCollection: IInvoiceNumber[] = [{ id: 456 }];
        expectedResult = service.addInvoiceNumberToCollectionIfMissing(invoiceNumberCollection, invoiceNumber);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(invoiceNumber);
      });

      it('should add only unique InvoiceNumber to an array', () => {
        const invoiceNumberArray: IInvoiceNumber[] = [{ id: 123 }, { id: 456 }, { id: 5736 }];
        const invoiceNumberCollection: IInvoiceNumber[] = [{ id: 123 }];
        expectedResult = service.addInvoiceNumberToCollectionIfMissing(invoiceNumberCollection, ...invoiceNumberArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const invoiceNumber: IInvoiceNumber = { id: 123 };
        const invoiceNumber2: IInvoiceNumber = { id: 456 };
        expectedResult = service.addInvoiceNumberToCollectionIfMissing([], invoiceNumber, invoiceNumber2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(invoiceNumber);
        expect(expectedResult).toContain(invoiceNumber2);
      });

      it('should accept null and undefined values', () => {
        const invoiceNumber: IInvoiceNumber = { id: 123 };
        expectedResult = service.addInvoiceNumberToCollectionIfMissing([], null, invoiceNumber, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(invoiceNumber);
      });

      it('should return initial array if no InvoiceNumber is added', () => {
        const invoiceNumberCollection: IInvoiceNumber[] = [{ id: 123 }];
        expectedResult = service.addInvoiceNumberToCollectionIfMissing(invoiceNumberCollection, undefined, null);
        expect(expectedResult).toEqual(invoiceNumberCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
