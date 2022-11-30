import { ITransfElVal } from 'app/shared/model/transf-el-val.model';
import { IBranchElVal } from 'app/shared/model/branch-el-val.model';
import { IBranchExtension } from 'app/shared/model/branch-extension.model';
import { INetwork } from 'app/shared/model/network.model';

export interface IBranch {
  id?: number;
  fbus?: number | null;
  tbus?: number | null;
  r?: number | null;
  x?: number | null;
  b?: number | null;
  ratea?: number | null;
  rateb?: number | null;
  ratec?: number | null;
  tapRatio?: number | null;
  angle?: number | null;
  status?: number | null;
  angmin?: number | null;
  angmax?: number | null;
  transfElVals?: ITransfElVal[] | null;
  branchElVals?: IBranchElVal[] | null;
  branchExtension?: IBranchExtension | null;
  network?: INetwork | null;
}

export const defaultValue: Readonly<IBranch> = {};
