export interface IWindData {
  id?: number;
  windSpeed?: number | null;
  hour?: number | null;
}

export const defaultValue: Readonly<IWindData> = {};
