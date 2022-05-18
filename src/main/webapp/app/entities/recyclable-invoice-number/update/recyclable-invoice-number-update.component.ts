import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IRecyclableInvoiceNumber, RecyclableInvoiceNumber } from '../recyclable-invoice-number.model';
import { RecyclableInvoiceNumberService } from '../service/recyclable-invoice-number.service';

@Component({
  selector: 'jhi-recyclable-invoice-number-update',
  templateUrl: './recyclable-invoice-number-update.component.html',
})
export class RecyclableInvoiceNumberUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    creationDate: [],
    invoiceNumber: [],
    eventId: [],
  });

  constructor(
    protected recyclableInvoiceNumberService: RecyclableInvoiceNumberService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recyclableInvoiceNumber }) => {
      if (recyclableInvoiceNumber.id === undefined) {
        const today = dayjs().startOf('day');
        recyclableInvoiceNumber.creationDate = today;
      }

      this.updateForm(recyclableInvoiceNumber);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const recyclableInvoiceNumber = this.createFromForm();
    if (recyclableInvoiceNumber.id !== undefined) {
      this.subscribeToSaveResponse(this.recyclableInvoiceNumberService.update(recyclableInvoiceNumber));
    } else {
      this.subscribeToSaveResponse(this.recyclableInvoiceNumberService.create(recyclableInvoiceNumber));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRecyclableInvoiceNumber>>): void {
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

  protected updateForm(recyclableInvoiceNumber: IRecyclableInvoiceNumber): void {
    this.editForm.patchValue({
      id: recyclableInvoiceNumber.id,
      creationDate: recyclableInvoiceNumber.creationDate ? recyclableInvoiceNumber.creationDate.format(DATE_TIME_FORMAT) : null,
      invoiceNumber: recyclableInvoiceNumber.invoiceNumber,
      eventId: recyclableInvoiceNumber.eventId,
    });
  }

  protected createFromForm(): IRecyclableInvoiceNumber {
    return {
      ...new RecyclableInvoiceNumber(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? dayjs(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      invoiceNumber: this.editForm.get(['invoiceNumber'])!.value,
      eventId: this.editForm.get(['eventId'])!.value,
    };
  }
}
