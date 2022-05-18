import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInvoiceNumber } from '../invoice-number.model';

@Component({
  selector: 'jhi-invoice-number-detail',
  templateUrl: './invoice-number-detail.component.html',
})
export class InvoiceNumberDetailComponent implements OnInit {
  invoiceNumber: IInvoiceNumber | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoiceNumber }) => {
      this.invoiceNumber = invoiceNumber;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
