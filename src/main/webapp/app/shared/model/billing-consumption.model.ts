import { INetwork } from 'app/shared/model/network.model';

export interface IBillingConsumption {
  id?: number;
  busNum?: number | null;
  type?: string | null;
  totalEnergyConsumption?: number | null;
  unitOfMeasure?: string | null;
  network?: INetwork | null;
}

export const defaultValue: Readonly<IBillingConsumption> = {};
