import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IInvoiceNumber, InvoiceNumber } from '../invoice-number.model';
import { InvoiceNumberService } from '../service/invoice-number.service';

import { InvoiceNumberRoutingResolveService } from './invoice-number-routing-resolve.service';

describe('InvoiceNumber routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: InvoiceNumberRoutingResolveService;
  let service: InvoiceNumberService;
  let resultInvoiceNumber: IInvoiceNumber | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(InvoiceNumberRoutingResolveService);
    service = TestBed.inject(InvoiceNumberService);
    resultInvoiceNumber = undefined;
  });

  describe('resolve', () => {
    it('should return IInvoiceNumber returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInvoiceNumber = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInvoiceNumber).toEqual({ id: 123 });
    });

    it('should return new IInvoiceNumber if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInvoiceNumber = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultInvoiceNumber).toEqual(new InvoiceNumber());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as InvoiceNumber })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInvoiceNumber = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInvoiceNumber).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
