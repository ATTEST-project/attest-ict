import { IBus } from 'app/shared/model/bus.model';

export interface IBusName {
  id?: number;
  busName?: string | null;
  bus?: IBus | null;
}

export const defaultValue: Readonly<IBusName> = {};
