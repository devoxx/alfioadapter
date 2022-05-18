import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRecyclableInvoiceNumber, RecyclableInvoiceNumber } from '../recyclable-invoice-number.model';

import { RecyclableInvoiceNumberService } from './recyclable-invoice-number.service';

describe('RecyclableInvoiceNumber Service', () => {
  let service: RecyclableInvoiceNumberService;
  let httpMock: HttpTestingController;
  let elemDefault: IRecyclableInvoiceNumber;
  let expectedResult: IRecyclableInvoiceNumber | IRecyclableInvoiceNumber[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RecyclableInvoiceNumberService);
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

    it('should create a RecyclableInvoiceNumber', () => {
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

      service.create(new RecyclableInvoiceNumber()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RecyclableInvoiceNumber', () => {
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

    it('should partial update a RecyclableInvoiceNumber', () => {
      const patchObject = Object.assign(
        {
          creationDate: currentDate.format(DATE_TIME_FORMAT),
          eventId: 'BBBBBB',
        },
        new RecyclableInvoiceNumber()
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

    it('should return a list of RecyclableInvoiceNumber', () => {
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

    it('should delete a RecyclableInvoiceNumber', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRecyclableInvoiceNumberToCollectionIfMissing', () => {
      it('should add a RecyclableInvoiceNumber to an empty array', () => {
        const recyclableInvoiceNumber: IRecyclableInvoiceNumber = { id: 123 };
        expectedResult = service.addRecyclableInvoiceNumberToCollectionIfMissing([], recyclableInvoiceNumber);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(recyclableInvoiceNumber);
      });

      it('should not add a RecyclableInvoiceNumber to an array that contains it', () => {
        const recyclableInvoiceNumber: IRecyclableInvoiceNumber = { id: 123 };
        const recyclableInvoiceNumberCollection: IRecyclableInvoiceNumber[] = [
          {
            ...recyclableInvoiceNumber,
          },
          { id: 456 },
        ];
        expectedResult = service.addRecyclableInvoiceNumberToCollectionIfMissing(
          recyclableInvoiceNumberCollection,
          recyclableInvoiceNumber
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RecyclableInvoiceNumber to an array that doesn't contain it", () => {
        const recyclableInvoiceNumber: IRecyclableInvoiceNumber = { id: 123 };
        const recyclableInvoiceNumberCollection: IRecyclableInvoiceNumber[] = [{ id: 456 }];
        expectedResult = service.addRecyclableInvoiceNumberToCollectionIfMissing(
          recyclableInvoiceNumberCollection,
          recyclableInvoiceNumber
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(recyclableInvoiceNumber);
      });

      it('should add only unique RecyclableInvoiceNumber to an array', () => {
        const recyclableInvoiceNumberArray: IRecyclableInvoiceNumber[] = [{ id: 123 }, { id: 456 }, { id: 44640 }];
        const recyclableInvoiceNumberCollection: IRecyclableInvoiceNumber[] = [{ id: 123 }];
        expectedResult = service.addRecyclableInvoiceNumberToCollectionIfMissing(
          recyclableInvoiceNumberCollection,
          ...recyclableInvoiceNumberArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const recyclableInvoiceNumber: IRecyclableInvoiceNumber = { id: 123 };
        const recyclableInvoiceNumber2: IRecyclableInvoiceNumber = { id: 456 };
        expectedResult = service.addRecyclableInvoiceNumberToCollectionIfMissing([], recyclableInvoiceNumber, recyclableInvoiceNumber2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(recyclableInvoiceNumber);
        expect(expectedResult).toContain(recyclableInvoiceNumber2);
      });

      it('should accept null and undefined values', () => {
        const recyclableInvoiceNumber: IRecyclableInvoiceNumber = { id: 123 };
        expectedResult = service.addRecyclableInvoiceNumberToCollectionIfMissing([], null, recyclableInvoiceNumber, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(recyclableInvoiceNumber);
      });

      it('should return initial array if no RecyclableInvoiceNumber is added', () => {
        const recyclableInvoiceNumberCollection: IRecyclableInvoiceNumber[] = [{ id: 123 }];
        expectedResult = service.addRecyclableInvoiceNumberToCollectionIfMissing(recyclableInvoiceNumberCollection, undefined, null);
        expect(expectedResult).toEqual(recyclableInvoiceNumberCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
