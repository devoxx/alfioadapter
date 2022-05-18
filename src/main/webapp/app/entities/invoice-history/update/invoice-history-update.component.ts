import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IInvoiceHistory, InvoiceHistory } from '../invoice-history.model';
import { InvoiceHistoryService } from '../service/invoice-history.service';

@Component({
  selector: 'jhi-invoice-history-update',
  templateUrl: './invoice-history-update.component.html',
})
export class InvoiceHistoryUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    creationDate: [],
    invoiceNumber: [],
    eventId: [],
    status: [],
  });

  constructor(
    protected invoiceHistoryService: InvoiceHistoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoiceHistory }) => {
      if (invoiceHistory.id === undefined) {
        const today = dayjs().startOf('day');
        invoiceHistory.creationDate = today;
      }

      this.updateForm(invoiceHistory);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoiceHistory = this.createFromForm();
    if (invoiceHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.invoiceHistoryService.update(invoiceHistory));
    } else {
      this.subscribeToSaveResponse(this.invoiceHistoryService.create(invoiceHistory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoiceHistory>>): void {
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

  protected updateForm(invoiceHistory: IInvoiceHistory): void {
    this.editForm.patchValue({
      id: invoiceHistory.id,
      creationDate: invoiceHistory.creationDate ? invoiceHistory.creationDate.format(DATE_TIME_FORMAT) : null,
      invoiceNumber: invoiceHistory.invoiceNumber,
      eventId: invoiceHistory.eventId,
      status: invoiceHistory.status,
    });
  }

  protected createFromForm(): IInvoiceHistory {
    return {
      ...new InvoiceHistory(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? dayjs(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      invoiceNumber: this.editForm.get(['invoiceNumber'])!.value,
      eventId: this.editForm.get(['eventId'])!.value,
      status: this.editForm.get(['status'])!.value,
    };
  }
}
