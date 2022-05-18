import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InvoiceNumberComponent } from './list/invoice-number.component';
import { InvoiceNumberDetailComponent } from './detail/invoice-number-detail.component';
import { InvoiceNumberUpdateComponent } from './update/invoice-number-update.component';
import { InvoiceNumberDeleteDialogComponent } from './delete/invoice-number-delete-dialog.component';
import { InvoiceNumberRoutingModule } from './route/invoice-number-routing.module';

@NgModule({
  imports: [SharedModule, InvoiceNumberRoutingModule],
  declarations: [InvoiceNumberComponent, InvoiceNumberDetailComponent, InvoiceNumberUpdateComponent, InvoiceNumberDeleteDialogComponent],
  entryComponents: [InvoiceNumberDeleteDialogComponent],
})
export class InvoiceNumberModule {}
