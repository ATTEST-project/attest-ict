export interface ISolarData {
  id?: number;
  p?: number | null;
  hour?: number | null;
}

export const defaultValue: Readonly<ISolarData> = {};
