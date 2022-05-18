import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRecyclableInvoiceNumber, RecyclableInvoiceNumber } from '../recyclable-invoice-number.model';
import { RecyclableInvoiceNumberService } from '../service/recyclable-invoice-number.service';

@Injectable({ providedIn: 'root' })
export class RecyclableInvoiceNumberRoutingResolveService implements Resolve<IRecyclableInvoiceNumber> {
  constructor(protected service: RecyclableInvoiceNumberService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRecyclableInvoiceNumber> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((recyclableInvoiceNumber: HttpResponse<RecyclableInvoiceNumber>) => {
          if (recyclableInvoiceNumber.body) {
            return of(recyclableInvoiceNumber.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RecyclableInvoiceNumber());
  }
}
