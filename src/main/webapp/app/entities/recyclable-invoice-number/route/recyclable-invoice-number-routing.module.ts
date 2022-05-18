import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RecyclableInvoiceNumberComponent } from '../list/recyclable-invoice-number.component';
import { RecyclableInvoiceNumberDetailComponent } from '../detail/recyclable-invoice-number-detail.component';
import { RecyclableInvoiceNumberUpdateComponent } from '../update/recyclable-invoice-number-update.component';
import { RecyclableInvoiceNumberRoutingResolveService } from './recyclable-invoice-number-routing-resolve.service';

const recyclableInvoiceNumberRoute: Routes = [
  {
    path: '',
    component: RecyclableInvoiceNumberComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RecyclableInvoiceNumberDetailComponent,
    resolve: {
      recyclableInvoiceNumber: RecyclableInvoiceNumberRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RecyclableInvoiceNumberUpdateComponent,
    resolve: {
      recyclableInvoiceNumber: RecyclableInvoiceNumberRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RecyclableInvoiceNumberUpdateComponent,
    resolve: {
      recyclableInvoiceNumber: RecyclableInvoiceNumberRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(recyclableInvoiceNumberRoute)],
  exports: [RouterModule],
})
export class RecyclableInvoiceNumberRoutingModule {}
