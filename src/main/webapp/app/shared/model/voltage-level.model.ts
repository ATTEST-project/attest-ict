import { INetwork } from 'app/shared/model/network.model';

export interface IVoltageLevel {
  id?: number;
  v1?: number | null;
  v2?: number | null;
  v3?: number | null;
  network?: INetwork | null;
}

export const defaultValue: Readonly<IVoltageLevel> = {};
