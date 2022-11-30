import dayjs from 'dayjs';
import { IInputFile } from 'app/shared/model/input-file.model';
import { IBranchElVal } from 'app/shared/model/branch-el-val.model';
import { INetwork } from 'app/shared/model/network.model';

export interface IBranchProfile {
  id?: number;
  season?: string | null;
  typicalDay?: string | null;
  mode?: number | null;
  timeInterval?: number | null;
  uploadDateTime?: string | null;
  inputFile?: IInputFile | null;
  branchElVals?: IBranchElVal[] | null;
  network?: INetwork | null;
}

export const defaultValue: Readonly<IBranchProfile> = {};
