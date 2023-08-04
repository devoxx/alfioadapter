import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInvoiceNumber } from '../invoice-number.model';
import { InvoiceNumberService } from '../service/invoice-number.service';

@Component({
  templateUrl: './invoice-number-delete-dialog.component.html',
})
export class InvoiceNumberDeleteDialogComponent {
  eventInvoiceNumber?: IInvoiceNumber;

  constructor(protected invoiceNumberService: InvoiceNumberService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.invoiceNumberService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
