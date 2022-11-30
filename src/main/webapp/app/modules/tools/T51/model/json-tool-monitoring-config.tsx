export interface JsonToolMonitoringConfig {
  modelpath1: string;
  modelpath2: string;
  filepath2: string;
  item1: string;
  item2: string;
  item3: string;
}

export const defaultValue: Readonly<JsonToolMonitoringConfig> = {
  modelpath1: '',
  modelpath2: '',
  filepath2: '',
  item1: '',
  item2: '',
  item3: '',
};
