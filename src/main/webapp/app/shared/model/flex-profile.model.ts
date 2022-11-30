import dayjs from 'dayjs';
import { IInputFile } from 'app/shared/model/input-file.model';
import { IFlexElVal } from 'app/shared/model/flex-el-val.model';
import { IFlexCost } from 'app/shared/model/flex-cost.model';
import { INetwork } from 'app/shared/model/network.model';

export interface IFlexProfile {
  id?: number;
  season?: string | null;
  typicalDay?: string | null;
  mode?: number | null;
  timeInterval?: number | null;
  uploadDateTime?: string | null;
  inputFile?: IInputFile | null;
  flexElVals?: IFlexElVal[] | null;
  flexCosts?: IFlexCost[] | null;
  network?: INetwork | null;
}

export const defaultValue: Readonly<IFlexProfile> = {};
