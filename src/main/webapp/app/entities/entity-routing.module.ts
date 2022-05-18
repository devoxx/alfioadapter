import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'invoice-number',
        data: { pageTitle: 'InvoiceNumbers' },
        loadChildren: () => import('./invoice-number/invoice-number.module').then(m => m.InvoiceNumberModule),
      },
      {
        path: 'recyclable-invoice-number',
        data: { pageTitle: 'RecyclableInvoiceNumbers' },
        loadChildren: () =>
          import('./recyclable-invoice-number/recyclable-invoice-number.module').then(m => m.RecyclableInvoiceNumberModule),
      },
      {
        path: 'invoice-history',
        data: { pageTitle: 'InvoiceHistories' },
        loadChildren: () => import('./invoice-history/invoice-history.module').then(m => m.InvoiceHistoryModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
