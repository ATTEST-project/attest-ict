import { IBranch } from 'app/shared/model/branch.model';
import { IBranchProfile } from 'app/shared/model/branch-profile.model';

export interface IBranchElVal {
  id?: number;
  hour?: number | null;
  min?: number | null;
  p?: number | null;
  q?: number | null;
  status?: number | null;
  branchIdOnSubst?: number | null;
  nominalVoltage?: string | null;
  branch?: IBranch | null;
  branchProfile?: IBranchProfile | null;
}

export const defaultValue: Readonly<IBranchElVal> = {};
