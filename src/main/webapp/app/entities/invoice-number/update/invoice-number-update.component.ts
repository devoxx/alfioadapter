import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IInvoiceNumber, InvoiceNumber } from '../invoice-number.model';
import { InvoiceNumberService } from '../service/invoice-number.service';

@Component({
  selector: 'jhi-invoice-number-update',
  templateUrl: './invoice-number-update.component.html',
})
export class InvoiceNumberUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    creationDate: [],
    invoiceNumber: [],
    eventId: [],
  });

  constructor(protected invoiceNumberService: InvoiceNumberService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoiceNumber }) => {
      if (invoiceNumber.id === undefined) {
        const today = dayjs().startOf('day');
        invoiceNumber.creationDate = today;
      }

      this.updateForm(invoiceNumber);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoiceNumber = this.createFromForm();
    if (invoiceNumber.id !== undefined) {
      this.subscribeToSaveResponse(this.invoiceNumberService.update(invoiceNumber));
    } else {
      this.subscribeToSaveResponse(this.invoiceNumberService.create(invoiceNumber));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoiceNumber>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(invoiceNumber: IInvoiceNumber): void {
    this.editForm.patchValue({
      id: invoiceNumber.id,
      creationDate: invoiceNumber.creationDate ? invoiceNumber.creationDate.format(DATE_TIME_FORMAT) : null,
      invoiceNumber: invoiceNumber.invoiceNumber,
      eventId: invoiceNumber.eventId,
    });
  }

  protected createFromForm(): IInvoiceNumber {
    return {
      ...new InvoiceNumber(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? dayjs(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      invoiceNumber: this.editForm.get(['invoiceNumber'])!.value,
      eventId: this.editForm.get(['eventId'])!.value,
    };
  }
}
