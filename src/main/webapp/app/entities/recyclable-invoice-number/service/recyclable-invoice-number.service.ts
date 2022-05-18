import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRecyclableInvoiceNumber, getRecyclableInvoiceNumberIdentifier } from '../recyclable-invoice-number.model';

export type EntityResponseType = HttpResponse<IRecyclableInvoiceNumber>;
export type EntityArrayResponseType = HttpResponse<IRecyclableInvoiceNumber[]>;

@Injectable({ providedIn: 'root' })
export class RecyclableInvoiceNumberService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/recyclable-invoice-numbers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(recyclableInvoiceNumber: IRecyclableInvoiceNumber): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(recyclableInvoiceNumber);
    return this.http
      .post<IRecyclableInvoiceNumber>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(recyclableInvoiceNumber: IRecyclableInvoiceNumber): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(recyclableInvoiceNumber);
    return this.http
      .put<IRecyclableInvoiceNumber>(
        `${this.resourceUrl}/${getRecyclableInvoiceNumberIdentifier(recyclableInvoiceNumber) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(recyclableInvoiceNumber: IRecyclableInvoiceNumber): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(recyclableInvoiceNumber);
    return this.http
      .patch<IRecyclableInvoiceNumber>(
        `${this.resourceUrl}/${getRecyclableInvoiceNumberIdentifier(recyclableInvoiceNumber) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRecyclableInvoiceNumber>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRecyclableInvoiceNumber[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRecyclableInvoiceNumberToCollectionIfMissing(
    recyclableInvoiceNumberCollection: IRecyclableInvoiceNumber[],
    ...recyclableInvoiceNumbersToCheck: (IRecyclableInvoiceNumber | null | undefined)[]
  ): IRecyclableInvoiceNumber[] {
    const recyclableInvoiceNumbers: IRecyclableInvoiceNumber[] = recyclableInvoiceNumbersToCheck.filter(isPresent);
    if (recyclableInvoiceNumbers.length > 0) {
      const recyclableInvoiceNumberCollectionIdentifiers = recyclableInvoiceNumberCollection.map(
        recyclableInvoiceNumberItem => getRecyclableInvoiceNumberIdentifier(recyclableInvoiceNumberItem)!
      );
      const recyclableInvoiceNumbersToAdd = recyclableInvoiceNumbers.filter(recyclableInvoiceNumberItem => {
        const recyclableInvoiceNumberIdentifier = getRecyclableInvoiceNumberIdentifier(recyclableInvoiceNumberItem);
        if (
          recyclableInvoiceNumberIdentifier == null ||
          recyclableInvoiceNumberCollectionIdentifiers.includes(recyclableInvoiceNumberIdentifier)
        ) {
          return false;
        }
        recyclableInvoiceNumberCollectionIdentifiers.push(recyclableInvoiceNumberIdentifier);
        return true;
      });
      return [...recyclableInvoiceNumbersToAdd, ...recyclableInvoiceNumberCollection];
    }
    return recyclableInvoiceNumberCollection;
  }

  protected convertDateFromClient(recyclableInvoiceNumber: IRecyclableInvoiceNumber): IRecyclableInvoiceNumber {
    return Object.assign({}, recyclableInvoiceNumber, {
      creationDate: recyclableInvoiceNumber.creationDate?.isValid() ? recyclableInvoiceNumber.creationDate.toJSON() : undefined,
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
      res.body.forEach((recyclableInvoiceNumber: IRecyclableInvoiceNumber) => {
        recyclableInvoiceNumber.creationDate = recyclableInvoiceNumber.creationDate
          ? dayjs(recyclableInvoiceNumber.creationDate)
          : undefined;
      });
    }
    return res;
  }
}
