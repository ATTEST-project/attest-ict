import { IGenerator } from 'app/shared/model/generator.model';

export interface IGenCost {
  id?: number;
  model?: number | null;
  startup?: number | null;
  shutdown?: number | null;
  nCost?: number | null;
  costPF?: string | null;
  costQF?: string | null;
  generator?: IGenerator | null;
}

export const defaultValue: Readonly<IGenCost> = {};
