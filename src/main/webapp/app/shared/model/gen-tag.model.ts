import { IGenerator } from 'app/shared/model/generator.model';

export interface IGenTag {
  id?: number;
  genTag?: string | null;
  generator?: IGenerator | null;
}

export const defaultValue: Readonly<IGenTag> = {};
