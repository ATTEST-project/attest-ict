export interface IStorageCost {
  id?: number;
  busNum?: number | null;
  costA?: number | null;
  costB?: number | null;
  costC?: number | null;
}

export const defaultValue: Readonly<IStorageCost> = {};
