import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInvoiceNumber, getInvoiceNumberIdentifier } from '../invoice-number.model';

export type EntityResponseType = HttpResponse<IInvoiceNumber>;
export type EntityArrayResponseType = HttpResponse<IInvoiceNumber[]>;

@Injectable({ providedIn: 'root' })
export class InvoiceNumberService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/invoice-numbers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(invoiceNumber: IInvoiceNumber): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoiceNumber);
    return this.http
      .post<IInvoiceNumber>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(invoiceNumber: IInvoiceNumber): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoiceNumber);
    return this.http
      .put<IInvoiceNumber>(`${this.resourceUrl}/${getInvoiceNumberIdentifier(invoiceNumber) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(invoiceNumber: IInvoiceNumber): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoiceNumber);
    return this.http
      .patch<IInvoiceNumber>(`${this.resourceUrl}/${getInvoiceNumberIdentifier(invoiceNumber) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInvoiceNumber>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInvoiceNumber[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInvoiceNumberToCollectionIfMissing(
    invoiceNumberCollection: IInvoiceNumber[],
    ...invoiceNumbersToCheck: (IInvoiceNumber | null | undefined)[]
  ): IInvoiceNumber[] {
    const invoiceNumbers: IInvoiceNumber[] = invoiceNumbersToCheck.filter(isPresent);
    if (invoiceNumbers.length > 0) {
      const invoiceNumberCollectionIdentifiers = invoiceNumberCollection.map(
        invoiceNumberItem => getInvoiceNumberIdentifier(invoiceNumberItem)!
      );
      const invoiceNumbersToAdd = invoiceNumbers.filter(invoiceNumberItem => {
        const invoiceNumberIdentifier = getInvoiceNumberIdentifier(invoiceNumberItem);
        if (invoiceNumberIdentifier == null || invoiceNumberCollectionIdentifiers.includes(invoiceNumberIdentifier)) {
          return false;
        }
        invoiceNumberCollectionIdentifiers.push(invoiceNumberIdentifier);
        return true;
      });
      return [...invoiceNumbersToAdd, ...invoiceNumberCollection];
    }
    return invoiceNumberCollection;
  }

  protected convertDateFromClient(invoiceNumber: IInvoiceNumber): IInvoiceNumber {
    return Object.assign({}, invoiceNumber, {
      creationDate: invoiceNumber.creationDate?.isValid() ? invoiceNumber.creationDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creationDate = res.body.creationDate ? dayjs(res.body.creationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((invoiceNumber: IInvoiceNumber) => {
        invoiceNumber.creationDate = invoiceNumber.creationDate ? dayjs(invoiceNumber.creationDate) : undefined;
      });
    }
    return res;
  }
}
