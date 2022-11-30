import dayjs from 'dayjs';
import { ITool } from 'app/shared/model/tool.model';
import { IGenProfile } from 'app/shared/model/gen-profile.model';
import { IFlexProfile } from 'app/shared/model/flex-profile.model';
import { ILoadProfile } from 'app/shared/model/load-profile.model';
import { ITransfProfile } from 'app/shared/model/transf-profile.model';
import { IBranchProfile } from 'app/shared/model/branch-profile.model';
import { INetwork } from 'app/shared/model/network.model';
import { ISimulation } from 'app/shared/model/simulation.model';

export interface IInputFile {
  id?: number;
  fileName?: string | null;
  description?: string | null;
  dataContentType?: string | null;
  data?: string | null;
  uploadTime?: string | null;
  tool?: ITool | null;
  genProfile?: IGenProfile | null;
  flexProfile?: IFlexProfile | null;
  loadProfile?: ILoadProfile | null;
  transfProfile?: ITransfProfile | null;
  branchProfile?: IBranchProfile | null;
  network?: INetwork | null;
  simulations?: ISimulation[] | null;
}

export const defaultValue: Readonly<IInputFile> = {};
