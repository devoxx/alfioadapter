import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRecyclableInvoiceNumber } from '../recyclable-invoice-number.model';
import { RecyclableInvoiceNumberService } from '../service/recyclable-invoice-number.service';

@Component({
  templateUrl: './recyclable-invoice-number-delete-dialog.component.html',
})
export class RecyclableInvoiceNumberDeleteDialogComponent {
  recyclableInvoiceNumber?: IRecyclableInvoiceNumber;

  constructor(protected recyclableInvoiceNumberService: RecyclableInvoiceNumberService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.recyclableInvoiceNumberService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
