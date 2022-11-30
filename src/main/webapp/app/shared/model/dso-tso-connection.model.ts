import { INetwork } from 'app/shared/model/network.model';

export interface IDsoTsoConnection {
  id?: number;
  tsoNetworkName?: string | null;
  dsoBusNum?: number | null;
  tsoBusNum?: number | null;
  dsoNetwork?: INetwork | null;
}

export const defaultValue: Readonly<IDsoTsoConnection> = {};
