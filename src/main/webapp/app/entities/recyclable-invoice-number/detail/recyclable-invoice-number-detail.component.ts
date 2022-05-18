import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRecyclableInvoiceNumber } from '../recyclable-invoice-number.model';

@Component({
  selector: 'jhi-recyclable-invoice-number-detail',
  templateUrl: './recyclable-invoice-number-detail.component.html',
})
export class RecyclableInvoiceNumberDetailComponent implements OnInit {
  recyclableInvoiceNumber: IRecyclableInvoiceNumber | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recyclableInvoiceNumber }) => {
      this.recyclableInvoiceNumber = recyclableInvoiceNumber;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
