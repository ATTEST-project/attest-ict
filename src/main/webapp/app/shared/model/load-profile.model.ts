import dayjs from 'dayjs';
import { IInputFile } from 'app/shared/model/input-file.model';
import { ILoadElVal } from 'app/shared/model/load-el-val.model';
import { INetwork } from 'app/shared/model/network.model';

export interface ILoadProfile {
  id?: number;
  season?: string | null;
  typicalDay?: string | null;
  mode?: number | null;
  timeInterval?: number | null;
  uploadDateTime?: string | null;
  inputFile?: IInputFile | null;
  loadElVals?: ILoadElVal[] | null;
  network?: INetwork | null;
}

export const defaultValue: Readonly<ILoadProfile> = {};
