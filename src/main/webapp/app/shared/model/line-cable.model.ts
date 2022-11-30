import { INetwork } from 'app/shared/model/network.model';

export interface ILineCable {
  id?: number;
  fbus?: number | null;
  tbus?: number | null;
  lengthKm?: number | null;
  typeOfInstallation?: string | null;
  network?: INetwork | null;
}

export const defaultValue: Readonly<ILineCable> = {};
