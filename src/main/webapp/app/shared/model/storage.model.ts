import { INetwork } from 'app/shared/model/network.model';

export interface IStorage {
  id?: number;
  busNum?: number | null;
  ps?: number | null;
  qs?: number | null;
  energy?: number | null;
  eRating?: number | null;
  chargeRating?: number | null;
  dischargeRating?: number | null;
  chargeEfficiency?: number | null;
  thermalRating?: number | null;
  qmin?: number | null;
  qmax?: number | null;
  r?: number | null;
  x?: number | null;
  pLoss?: number | null;
  qLoss?: number | null;
  status?: number | null;
  socInitial?: number | null;
  socMin?: number | null;
  socMax?: number | null;
  network?: INetwork | null;
}

export const defaultValue: Readonly<IStorage> = {};
