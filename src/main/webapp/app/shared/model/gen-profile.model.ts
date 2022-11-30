import dayjs from 'dayjs';
import { IInputFile } from 'app/shared/model/input-file.model';
import { IGenElVal } from 'app/shared/model/gen-el-val.model';
import { INetwork } from 'app/shared/model/network.model';

export interface IGenProfile {
  id?: number;
  season?: string | null;
  typicalDay?: string | null;
  mode?: number | null;
  timeInterval?: number | null;
  uploadDateTime?: string | null;
  inputFile?: IInputFile | null;
  genElVals?: IGenElVal[] | null;
  network?: INetwork | null;
}

export const defaultValue: Readonly<IGenProfile> = {};
