import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInvoiceNumber, InvoiceNumber } from '../invoice-number.model';
import { InvoiceNumberService } from '../service/invoice-number.service';

@Injectable({ providedIn: 'root' })
export class InvoiceNumberRoutingResolveService implements Resolve<IInvoiceNumber> {
  constructor(protected service: InvoiceNumberService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInvoiceNumber> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((invoiceNumber: HttpResponse<InvoiceNumber>) => {
          if (invoiceNumber.body) {
            return of(invoiceNumber.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InvoiceNumber());
  }
}
