import { IGenerator } from 'app/shared/model/generator.model';

export interface IGeneratorExtension {
  id?: number;
  idGen?: number | null;
  statusCurt?: number | null;
  dgType?: number | null;
  generator?: IGenerator | null;
}

export const defaultValue: Readonly<IGeneratorExtension> = {};
