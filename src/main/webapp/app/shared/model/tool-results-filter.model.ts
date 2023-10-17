export interface IToolResultsFilter {
  networkId?: number;
  toolId?: number;
  dateTimeEnd?: string | null;
  fileName?: string | null;
}

export const defaultValue: Readonly<IToolResultsFilter> = {};
