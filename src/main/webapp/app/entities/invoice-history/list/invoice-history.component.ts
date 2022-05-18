import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInvoiceHistory } from '../invoice-history.model';
import { InvoiceHistoryService } from '../service/invoice-history.service';
import { InvoiceHistoryDeleteDialogComponent } from '../delete/invoice-history-delete-dialog.component';

@Component({
  selector: 'jhi-invoice-history',
  templateUrl: './invoice-history.component.html',
})
export class InvoiceHistoryComponent implements OnInit {
  invoiceHistories?: IInvoiceHistory[];
  isLoading = false;

  constructor(protected invoiceHistoryService: InvoiceHistoryService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.invoiceHistoryService.query().subscribe({
      next: (res: HttpResponse<IInvoiceHistory[]>) => {
        this.isLoading = false;
        this.invoiceHistories = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IInvoiceHistory): number {
    return item.id!;
  }

  delete(invoiceHistory: IInvoiceHistory): void {
    const modalRef = this.modalService.open(InvoiceHistoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.invoiceHistory = invoiceHistory;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
