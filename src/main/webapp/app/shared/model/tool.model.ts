import { IInputFile } from 'app/shared/model/input-file.model';
import { IOutputFile } from 'app/shared/model/output-file.model';
import { ITask } from 'app/shared/model/task.model';
import { IToolParameter } from 'app/shared/model/tool-parameter.model';

export interface ITool {
  id?: number;
  workPackage?: string;
  num?: string;
  name?: string;
  path?: string | null;
  description?: string | null;
  inputFiles?: IInputFile[] | null;
  outputFiles?: IOutputFile[] | null;
  tasks?: ITask[] | null;
  parameters?: IToolParameter[] | null;
}

export const defaultValue: Readonly<ITool> = {};
