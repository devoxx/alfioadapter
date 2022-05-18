import dayjs from 'dayjs/esm';

export interface IRecyclableInvoiceNumber {
  id?: number;
  creationDate?: dayjs.Dayjs | null;
  invoiceNumber?: number | null;
  eventId?: string | null;
}

export class RecyclableInvoiceNumber implements IRecyclableInvoiceNumber {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs | null,
    public invoiceNumber?: number | null,
    public eventId?: string | null
  ) {}
}

export function getRecyclableInvoiceNumberIdentifier(recyclableInvoiceNumber: IRecyclableInvoiceNumber): number | undefined {
  return recyclableInvoiceNumber.id;
}
