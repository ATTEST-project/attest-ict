import { INetwork } from 'app/shared/model/network.model';

export interface ITransformer {
  id?: number;
  fbus?: number | null;
  tbus?: number | null;
  min?: number | null;
  max?: number | null;
  totalTaps?: number | null;
  tap?: number | null;
  manufactureYear?: number | null;
  commissioningYear?: number | null;
  network?: INetwork | null;
}

export const defaultValue: Readonly<ITransformer> = {};
