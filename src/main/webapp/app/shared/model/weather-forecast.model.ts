export interface IWeatherForecast {
  id?: number;
  solarProfile?: number | null;
  outsideTemp?: number | null;
}

export const defaultValue: Readonly<IWeatherForecast> = {};
