import dayjs from 'dayjs/esm';

export interface IInvoiceNumber {
  id?: number;
  creationDate?: dayjs.Dayjs | null;
  invoiceNumber?: number | null;
  eventId?: string | null;
}

export class InvoiceNumber implements IInvoiceNumber {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs | null,
    public invoiceNumber?: number | null,
    public eventId?: string | null
  ) {}
}

export function getInvoiceNumberIdentifier(invoiceNumber: IInvoiceNumber): number | undefined {
  return invoiceNumber.id;
}
