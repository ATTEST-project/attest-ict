import { INetwork } from 'app/shared/model/network.model';

export interface IAssetTransformer {
  id?: number;
  busNum?: number | null;
  voltageRatio?: string | null;
  insulationMedium?: string | null;
  type?: string | null;
  indoorOutdoor?: string | null;
  annualMaxLoadKva?: number | null;
  age?: number | null;
  externalCondition?: string | null;
  ratingKva?: number | null;
  numConnectedCustomers?: number | null;
  numSensitiveCustomers?: number | null;
  backupSupply?: string | null;
  costOfFailureEuro?: number | null;
  network?: INetwork | null;
}

export const defaultValue: Readonly<IAssetTransformer> = {};
