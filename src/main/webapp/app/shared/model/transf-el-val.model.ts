import { ITransfProfile } from 'app/shared/model/transf-profile.model';
import { IBranch } from 'app/shared/model/branch.model';

export interface ITransfElVal {
  id?: number;
  hour?: number | null;
  min?: number | null;
  tapRatio?: number | null;
  status?: number | null;
  trasfIdOnSubst?: number | null;
  nominalVoltage?: string | null;
  transfProfile?: ITransfProfile | null;
  branch?: IBranch | null;
}

export const defaultValue: Readonly<ITransfElVal> = {};
