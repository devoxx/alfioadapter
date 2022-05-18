import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RecyclableInvoiceNumberComponent } from './list/recyclable-invoice-number.component';
import { RecyclableInvoiceNumberDetailComponent } from './detail/recyclable-invoice-number-detail.component';
import { RecyclableInvoiceNumberUpdateComponent } from './update/recyclable-invoice-number-update.component';
import { RecyclableInvoiceNumberDeleteDialogComponent } from './delete/recyclable-invoice-number-delete-dialog.component';
import { RecyclableInvoiceNumberRoutingModule } from './route/recyclable-invoice-number-routing.module';

@NgModule({
  imports: [SharedModule, RecyclableInvoiceNumberRoutingModule],
  declarations: [
    RecyclableInvoiceNumberComponent,
    RecyclableInvoiceNumberDetailComponent,
    RecyclableInvoiceNumberUpdateComponent,
    RecyclableInvoiceNumberDeleteDialogComponent,
  ],
  entryComponents: [RecyclableInvoiceNumberDeleteDialogComponent],
})
export class RecyclableInvoiceNumberModule {}
