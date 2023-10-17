export interface IToolResult {
  networkId?: number;
  toolId?: number;
  outputFileId?: number;
  fileName?: string | null;
  simulationId?: number | null;
  simulationUuid?: string | null;
  simulationDescription?: string | null;
  taskId?: number;
  dateTimeStart?: string | null;
  dateTimeEnd?: string | null;
  userId?: string | null;
  login?: string | null;
}

export const defaultValue: Readonly<IToolResult> = {};
