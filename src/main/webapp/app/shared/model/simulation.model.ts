import { INetwork } from 'app/shared/model/network.model';
import { IInputFile } from 'app/shared/model/input-file.model';
import { ITask } from 'app/shared/model/task.model';
import { IOutputFile } from 'app/shared/model/output-file.model';

export interface ISimulation {
  id?: number;
  uuid?: string;
  description?: string | null;
  configFileContentType?: string;
  configFile?: string;
  network?: INetwork | null;
  inputFiles?: IInputFile[] | null;
  task?: ITask | null;
  outputFiles?: IOutputFile[] | null;
}

export const defaultValue: Readonly<ISimulation> = {};
