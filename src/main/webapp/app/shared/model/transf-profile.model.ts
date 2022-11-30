import dayjs from 'dayjs';
import { IInputFile } from 'app/shared/model/input-file.model';
import { ITransfElVal } from 'app/shared/model/transf-el-val.model';
import { INetwork } from 'app/shared/model/network.model';

export interface ITransfProfile {
  id?: number;
  season?: string | null;
  typicalDay?: string | null;
  mode?: number | null;
  timeInterval?: number | null;
  uploadDateTime?: string | null;
  inputFile?: IInputFile | null;
  transfElVals?: ITransfElVal[] | null;
  network?: INetwork | null;
}

export const defaultValue: Readonly<ITransfProfile> = {};
