import { INetwork } from 'app/shared/model/network.model';

export interface IBillingDer {
  id?: number;
  busNum?: number | null;
  maxPowerKw?: number | null;
  type?: string | null;
  network?: INetwork | null;
}

export const defaultValue: Readonly<IBillingDer> = {};
