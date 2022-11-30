import { ILoadProfile } from 'app/shared/model/load-profile.model';
import { IBus } from 'app/shared/model/bus.model';

export interface ILoadElVal {
  id?: number;
  hour?: number | null;
  min?: number | null;
  p?: number | null;
  q?: number | null;
  loadIdOnSubst?: number | null;
  nominalVoltage?: string | null;
  loadProfile?: ILoadProfile | null;
  bus?: IBus | null;
}

export const defaultValue: Readonly<ILoadElVal> = {};
