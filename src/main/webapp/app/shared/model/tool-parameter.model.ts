import dayjs from 'dayjs';
import { ITool } from 'app/shared/model/tool.model';

export interface IToolParameter {
  id?: number;
  name?: string;
  defaultValue?: string;
  isEnabled?: boolean;
  description?: string | null;
  lastUpdate?: string | null;
  tool?: ITool | null;
}

export const defaultValue: Readonly<IToolParameter> = {
  isEnabled: false,
};
