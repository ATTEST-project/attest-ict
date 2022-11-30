import dayjs from 'dayjs';
import { ITask } from 'app/shared/model/task.model';

export interface IToolLogFile {
  id?: number;
  fileName?: string | null;
  description?: string | null;
  dataContentType?: string | null;
  data?: string | null;
  uploadTime?: string | null;
  task?: ITask | null;
}

export const defaultValue: Readonly<IToolLogFile> = {};
