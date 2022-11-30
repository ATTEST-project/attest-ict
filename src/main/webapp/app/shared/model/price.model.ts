export interface IPrice {
  id?: number;
  electricityEnergy?: number | null;
  gasEnergy?: number | null;
  secondaryBand?: number | null;
  secondaryUp?: number | null;
  secondaryDown?: number | null;
  secondaryRatioUp?: number | null;
  secondaryRatioDown?: number | null;
}

export const defaultValue: Readonly<IPrice> = {};
