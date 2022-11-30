import { IFlexProfile } from 'app/shared/model/flex-profile.model';

export interface IFlexCost {
  id?: number;
  busNum?: number | null;
  model?: number | null;
  nCost?: number | null;
  costPr?: number | null;
  costQr?: number | null;
  costPf?: string | null;
  costQf?: string | null;
  flexProfile?: IFlexProfile | null;
}

export const defaultValue: Readonly<IFlexCost> = {};
