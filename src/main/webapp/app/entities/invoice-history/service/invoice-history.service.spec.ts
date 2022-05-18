import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInvoiceHistory, InvoiceHistory } from '../invoice-history.model';

import { InvoiceHistoryService } from './invoice-history.service';

describe('InvoiceHistory Service', () => {
  let service: InvoiceHistoryService;
  let httpMock: HttpTestingController;
  let elemDefault: IInvoiceHistory;
  let expectedResult: IInvoiceHistory | IInvoiceHistory[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InvoiceHistoryService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      creationDate: currentDate,
      invoiceNumber: 0,
      eventId: 'AAAAAAA',
      status: 'AAAAAAA',
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

    it('should create a InvoiceHistory', () => {
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

      service.create(new InvoiceHistory()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InvoiceHistory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          creationDate: currentDate.format(DATE_TIME_FORMAT),
          invoiceNumber: 1,
          eventId: 'BBBBBB',
          status: 'BBBBBB',
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

    it('should partial update a InvoiceHistory', () => {
      const patchObject = Object.assign(
        {
          status: 'BBBBBB',
        },
        new InvoiceHistory()
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

    it('should return a list of InvoiceHistory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          creationDate: currentDate.format(DATE_TIME_FORMAT),
          invoiceNumber: 1,
          eventId: 'BBBBBB',
          status: 'BBBBBB',
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

    it('should delete a InvoiceHistory', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInvoiceHistoryToCollectionIfMissing', () => {
      it('should add a InvoiceHistory to an empty array', () => {
        const invoiceHistory: IInvoiceHistory = { id: 123 };
        expectedResult = service.addInvoiceHistoryToCollectionIfMissing([], invoiceHistory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(invoiceHistory);
      });

      it('should not add a InvoiceHistory to an array that contains it', () => {
        const invoiceHistory: IInvoiceHistory = { id: 123 };
        const invoiceHistoryCollection: IInvoiceHistory[] = [
          {
            ...invoiceHistory,
          },
          { id: 456 },
        ];
        expectedResult = service.addInvoiceHistoryToCollectionIfMissing(invoiceHistoryCollection, invoiceHistory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InvoiceHistory to an array that doesn't contain it", () => {
        const invoiceHistory: IInvoiceHistory = { id: 123 };
        const invoiceHistoryCollection: IInvoiceHistory[] = [{ id: 456 }];
        expectedResult = service.addInvoiceHistoryToCollectionIfMissing(invoiceHistoryCollection, invoiceHistory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(invoiceHistory);
      });

      it('should add only unique InvoiceHistory to an array', () => {
        const invoiceHistoryArray: IInvoiceHistory[] = [{ id: 123 }, { id: 456 }, { id: 32693 }];
        const invoiceHistoryCollection: IInvoiceHistory[] = [{ id: 123 }];
        expectedResult = service.addInvoiceHistoryToCollectionIfMissing(invoiceHistoryCollection, ...invoiceHistoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const invoiceHistory: IInvoiceHistory = { id: 123 };
        const invoiceHistory2: IInvoiceHistory = { id: 456 };
        expectedResult = service.addInvoiceHistoryToCollectionIfMissing([], invoiceHistory, invoiceHistory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(invoiceHistory);
        expect(expectedResult).toContain(invoiceHistory2);
      });

      it('should accept null and undefined values', () => {
        const invoiceHistory: IInvoiceHistory = { id: 123 };
        expectedResult = service.addInvoiceHistoryToCollectionIfMissing([], null, invoiceHistory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(invoiceHistory);
      });

      it('should return initial array if no InvoiceHistory is added', () => {
        const invoiceHistoryCollection: IInvoiceHistory[] = [{ id: 123 }];
        expectedResult = service.addInvoiceHistoryToCollectionIfMissing(invoiceHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(invoiceHistoryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
