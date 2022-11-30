import { IBranch } from 'app/shared/model/branch.model';

export interface IBranchExtension {
  id?: number;
  stepSize?: number | null;
  actTap?: number | null;
  minTap?: number | null;
  maxTap?: number | null;
  normalTap?: number | null;
  nominalRatio?: number | null;
  rIp?: number | null;
  rN?: number | null;
  r0?: number | null;
  x0?: number | null;
  b0?: number | null;
  length?: number | null;
  normStat?: number | null;
  g?: number | null;
  mRid?: string | null;
  branch?: IBranch | null;
}

export const defaultValue: Readonly<IBranchExtension> = {};
