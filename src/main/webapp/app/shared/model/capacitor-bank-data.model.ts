import { INetwork } from 'app/shared/model/network.model';

export interface ICapacitorBankData {
  id?: number;
  busNum?: number | null;
  nodeId?: string | null;
  bankId?: string | null;
  qnom?: number | null;
  network?: INetwork | null;
}

export const defaultValue: Readonly<ICapacitorBankData> = {};
