import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRecyclableInvoiceNumber } from '../recyclable-invoice-number.model';
import { RecyclableInvoiceNumberService } from '../service/recyclable-invoice-number.service';
import { RecyclableInvoiceNumberDeleteDialogComponent } from '../delete/recyclable-invoice-number-delete-dialog.component';

@Component({
  selector: 'jhi-recyclable-invoice-number',
  templateUrl: './recyclable-invoice-number.component.html',
})
export class RecyclableInvoiceNumberComponent implements OnInit {
  recyclableInvoiceNumbers?: IRecyclableInvoiceNumber[];
  isLoading = false;

  constructor(protected recyclableInvoiceNumberService: RecyclableInvoiceNumberService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.recyclableInvoiceNumberService.query().subscribe({
      next: (res: HttpResponse<IRecyclableInvoiceNumber[]>) => {
        this.isLoading = false;
        this.recyclableInvoiceNumbers = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IRecyclableInvoiceNumber): number {
    return item.id!;
  }

  delete(recyclableInvoiceNumber: IRecyclableInvoiceNumber): void {
    const modalRef = this.modalService.open(RecyclableInvoiceNumberDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.recyclableInvoiceNumber = recyclableInvoiceNumber;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
