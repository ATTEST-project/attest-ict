import { ITopology } from 'app/shared/model/topology.model';
import { INetwork } from 'app/shared/model/network.model';

export interface ITopologyBus {
  id?: number;
  powerLineBranch?: string | null;
  busName1?: string | null;
  busName2?: string | null;
  topologies?: ITopology[] | null;
  network?: INetwork | null;
}

export const defaultValue: Readonly<ITopologyBus> = {};
