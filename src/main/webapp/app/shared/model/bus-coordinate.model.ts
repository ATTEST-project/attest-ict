import { IBus } from 'app/shared/model/bus.model';

export interface IBusCoordinate {
  id?: number;
  x?: number | null;
  y?: number | null;
  bus?: IBus | null;
}

export const defaultValue: Readonly<IBusCoordinate> = {};
