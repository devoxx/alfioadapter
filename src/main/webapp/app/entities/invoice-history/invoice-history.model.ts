import dayjs from 'dayjs/esm';

export interface IInvoiceHistory {
  id?: number;
  creationDate?: dayjs.Dayjs | null;
  invoiceNumber?: number | null;
  eventId?: string | null;
  status?: string | null;
}

export class InvoiceHistory implements IInvoiceHistory {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs | null,
    public invoiceNumber?: number | null,
    public eventId?: string | null,
    public status?: string | null
  ) {}
}

export function getInvoiceHistoryIdentifier(invoiceHistory: IInvoiceHistory): number | undefined {
  return invoiceHistory.id;
}
