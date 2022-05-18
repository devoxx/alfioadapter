import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInvoiceHistory, getInvoiceHistoryIdentifier } from '../invoice-history.model';

export type EntityResponseType = HttpResponse<IInvoiceHistory>;
export type EntityArrayResponseType = HttpResponse<IInvoiceHistory[]>;

@Injectable({ providedIn: 'root' })
export class InvoiceHistoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/invoice-histories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(invoiceHistory: IInvoiceHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoiceHistory);
    return this.http
      .post<IInvoiceHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(invoiceHistory: IInvoiceHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoiceHistory);
    return this.http
      .put<IInvoiceHistory>(`${this.resourceUrl}/${getInvoiceHistoryIdentifier(invoiceHistory) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(invoiceHistory: IInvoiceHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(invoiceHistory);
    return this.http
      .patch<IInvoiceHistory>(`${this.resourceUrl}/${getInvoiceHistoryIdentifier(invoiceHistory) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInvoiceHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInvoiceHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInvoiceHistoryToCollectionIfMissing(
    invoiceHistoryCollection: IInvoiceHistory[],
    ...invoiceHistoriesToCheck: (IInvoiceHistory | null | undefined)[]
  ): IInvoiceHistory[] {
    const invoiceHistories: IInvoiceHistory[] = invoiceHistoriesToCheck.filter(isPresent);
    if (invoiceHistories.length > 0) {
      const invoiceHistoryCollectionIdentifiers = invoiceHistoryCollection.map(
        invoiceHistoryItem => getInvoiceHistoryIdentifier(invoiceHistoryItem)!
      );
      const invoiceHistoriesToAdd = invoiceHistories.filter(invoiceHistoryItem => {
        const invoiceHistoryIdentifier = getInvoiceHistoryIdentifier(invoiceHistoryItem);
        if (invoiceHistoryIdentifier == null || invoiceHistoryCollectionIdentifiers.includes(invoiceHistoryIdentifier)) {
          return false;
        }
        invoiceHistoryCollectionIdentifiers.push(invoiceHistoryIdentifier);
        return true;
      });
      return [...invoiceHistoriesToAdd, ...invoiceHistoryCollection];
    }
    return invoiceHistoryCollection;
  }

  protected convertDateFromClient(invoiceHistory: IInvoiceHistory): IInvoiceHistory {
    return Object.assign({}, invoiceHistory, {
      creationDate: invoiceHistory.creationDate?.isValid() ? invoiceHistory.creationDate.toJSON() : undefined,
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
      res.body.forEach((invoiceHistory: IInvoiceHistory) => {
        invoiceHistory.creationDate = invoiceHistory.creationDate ? dayjs(invoiceHistory.creationDate) : undefined;
      });
    }
    return res;
  }
}
