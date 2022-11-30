import { ILoadElVal } from 'app/shared/model/load-el-val.model';
import { IBusName } from 'app/shared/model/bus-name.model';
import { IBusExtension } from 'app/shared/model/bus-extension.model';
import { IBusCoordinate } from 'app/shared/model/bus-coordinate.model';
import { INetwork } from 'app/shared/model/network.model';

export interface IBus {
  id?: number;
  busNum?: number | null;
  type?: number | null;
  activePower?: number | null;
  reactivePower?: number | null;
  conductance?: number | null;
  susceptance?: number | null;
  area?: number | null;
  vm?: number | null;
  va?: number | null;
  baseKv?: number | null;
  zone?: number | null;
  vmax?: number | null;
  vmin?: number | null;
  loadELVals?: ILoadElVal[] | null;
  busName?: IBusName | null;
  busExtension?: IBusExtension | null;
  busCoordinate?: IBusCoordinate | null;
  network?: INetwork | null;
}

export const defaultValue: Readonly<IBus> = {};
