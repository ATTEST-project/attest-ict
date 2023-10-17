import dayjs from 'dayjs';
import { IToolLogFile } from 'app/shared/model/tool-log-file.model';
import { ISimulation } from 'app/shared/model/simulation.model';
import { ITool } from 'app/shared/model/tool.model';
import { IUser } from 'app/shared/model/user.model';

export interface ITask {
  id?: number;
  taskStatus?: string | null;
  info?: string | null;
  dateTimeStart?: string | null;
  dateTimeEnd?: string | null;
  toolLogFile?: IToolLogFile | null;
  simulation?: ISimulation | null;
  toolLogFileId?: number;
  toolLogFileName?: string | null;
  simulationId?: number;
  simulationUuid?: string;
  simulationConfigFile?: string;
  networkId?: number;
  tool?: ITool | null;
  user?: IUser | null;
  simulationDescr?: string;
  networkName?: string;
  toolNum?: string;
}

export const defaultValue: Readonly<ITask> = {};

export enum TaskStatus {
  ONGOING = 'ONGOING',
  PASSED = 'PASSED',
  FAILURE = 'FAILURE',
  KILLED = 'KILLED',
}
