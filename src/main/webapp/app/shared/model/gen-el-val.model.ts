import { IGenProfile } from 'app/shared/model/gen-profile.model';
import { IGenerator } from 'app/shared/model/generator.model';

export interface IGenElVal {
  id?: number;
  hour?: number | null;
  min?: number | null;
  p?: number | null;
  q?: number | null;
  status?: number | null;
  voltageMagnitude?: number | null;
  genIdOnSubst?: number | null;
  nominalVoltage?: string | null;
  genProfile?: IGenProfile | null;
  generator?: IGenerator | null;
}

export const defaultValue: Readonly<IGenElVal> = {};
