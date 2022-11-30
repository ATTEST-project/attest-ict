import { ITopologyBus } from 'app/shared/model/topology-bus.model';

export interface ITopology {
  id?: number;
  powerLineBranch?: string | null;
  p1?: string | null;
  p2?: string | null;
  powerLineBranchParent?: ITopologyBus | null;
}

export const defaultValue: Readonly<ITopology> = {};
