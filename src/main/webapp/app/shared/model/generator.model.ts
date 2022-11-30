import { IGenElVal } from 'app/shared/model/gen-el-val.model';
import { IGeneratorExtension } from 'app/shared/model/generator-extension.model';
import { IGenTag } from 'app/shared/model/gen-tag.model';
import { IGenCost } from 'app/shared/model/gen-cost.model';
import { INetwork } from 'app/shared/model/network.model';

export interface IGenerator {
  id?: number;
  busNum?: number | null;
  pg?: number | null;
  qg?: number | null;
  qmax?: number | null;
  qmin?: number | null;
  vg?: number | null;
  mBase?: number | null;
  status?: number | null;
  pmax?: number | null;
  pmin?: number | null;
  pc1?: number | null;
  pc2?: number | null;
  qc1min?: number | null;
  qc1max?: number | null;
  qc2min?: number | null;
  qc2max?: number | null;
  rampAgc?: number | null;
  ramp10?: number | null;
  ramp30?: number | null;
  rampQ?: number | null;
  apf?: number | null;
  genElVals?: IGenElVal[] | null;
  generatorExtension?: IGeneratorExtension | null;
  genTag?: IGenTag | null;
  genCost?: IGenCost | null;
  network?: INetwork | null;
}

export const defaultValue: Readonly<IGenerator> = {};
