import { IBus } from 'app/shared/model/bus.model';

export interface IBusExtension {
  id?: number;
  hasGen?: number | null;
  isLoad?: number | null;
  snomMva?: number | null;
  sx?: number | null;
  sy?: number | null;
  gx?: number | null;
  gy?: number | null;
  status?: number | null;
  incrementCost?: number | null;
  decrementCost?: number | null;
  mRid?: string | null;
  bus?: IBus | null;
}

export const defaultValue: Readonly<IBusExtension> = {};
