import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InvoiceNumberComponent } from '../list/invoice-number.component';
import { InvoiceNumberDetailComponent } from '../detail/invoice-number-detail.component';
import { InvoiceNumberUpdateComponent } from '../update/invoice-number-update.component';
import { InvoiceNumberRoutingResolveService } from './invoice-number-routing-resolve.service';

const invoiceNumberRoute: Routes = [
  {
    path: '',
    component: InvoiceNumberComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InvoiceNumberDetailComponent,
    resolve: {
      invoiceNumber: InvoiceNumberRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InvoiceNumberUpdateComponent,
    resolve: {
      invoiceNumber: InvoiceNumberRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InvoiceNumberUpdateComponent,
    resolve: {
      invoiceNumber: InvoiceNumberRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(invoiceNumberRoute)],
  exports: [RouterModule],
})
export class InvoiceNumberRoutingModule {}
