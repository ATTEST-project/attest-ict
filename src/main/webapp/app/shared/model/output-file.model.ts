import dayjs from 'dayjs';
import { ITool } from 'app/shared/model/tool.model';
import { INetwork } from 'app/shared/model/network.model';
import { ISimulation } from 'app/shared/model/simulation.model';

export interface IOutputFile {
  id?: number;
  fileName?: string | null;
  description?: string | null;
  dataContentType?: string | null;
  data?: string | null;
  uploadTime?: string | null;
  tool?: ITool | null;
  network?: INetwork | null;
  simulation?: ISimulation | null;
}

export const defaultValue: Readonly<IOutputFile> = {};
