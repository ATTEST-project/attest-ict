import { INetwork } from 'app/shared/model/network.model';

export interface IBaseMVA {
  id?: number;
  baseMva?: number | null;
  network?: INetwork | null;
}

export const defaultValue: Readonly<IBaseMVA> = {};
