export interface ICimRepoNetwork {
  networkId?: number;
  networkName?: string | null;
  type?: string | null;
  country?: string | null;
}

export const defaultValue: Readonly<ICimRepoNetwork> = {};
