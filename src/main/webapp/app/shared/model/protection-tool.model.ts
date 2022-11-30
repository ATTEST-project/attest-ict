import { IBranch } from 'app/shared/model/branch.model';
import { IBus } from 'app/shared/model/bus.model';

export interface IProtectionTool {
  id?: number;
  type?: string | null;
  branch?: IBranch | null;
  bus?: IBus | null;
}

export const defaultValue: Readonly<IProtectionTool> = {};
