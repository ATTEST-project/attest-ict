import { IFlexProfile } from 'app/shared/model/flex-profile.model';

export interface IFlexElVal {
  id?: number;
  busNum?: number | null;
  hour?: number | null;
  min?: number | null;
  pfmaxUp?: number | null;
  pfmaxDn?: number | null;
  qfmaxUp?: number | null;
  qfmaxDn?: number | null;
  flexProfile?: IFlexProfile | null;
}

export const defaultValue: Readonly<IFlexElVal> = {};
