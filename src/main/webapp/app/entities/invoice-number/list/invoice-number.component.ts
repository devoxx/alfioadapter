import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInvoiceNumber } from '../invoice-number.model';
import { InvoiceNumberService } from '../service/invoice-number.service';
import { InvoiceNumberDeleteDialogComponent } from '../delete/invoice-number-delete-dialog.component';

@Component({
  selector: 'jhi-invoice-number',
  templateUrl: './invoice-number.component.html',
})
export class InvoiceNumberComponent implements OnInit {
  invoiceNumbers?: IInvoiceNumber[];
  isLoading = false;

  constructor(protected invoiceNumberService: InvoiceNumberService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.invoiceNumberService.query().subscribe({
      next: (res: HttpResponse<IInvoiceNumber[]>) => {
        this.isLoading = false;
        this.invoiceNumbers = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IInvoiceNumber): number {
    return item.id!;
  }

  delete(invoiceNumber: IInvoiceNumber): void {
    const modalRef = this.modalService.open(InvoiceNumberDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.invoiceNumber = invoiceNumber;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
