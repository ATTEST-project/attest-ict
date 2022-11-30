export interface NetworkEntireSLDProps {
  ids?: string[];
  svg?: {
    metadata: string;
    svg: string;
  };
}

export interface NetworkSubstationsSLDProps {
  [sub_id: string]: {
    metadata: string;
    svg: string;
  };
}

export const defaultValueEntire: Readonly<NetworkEntireSLDProps> = {};
export const defaultValueSubstations: Readonly<NetworkSubstationsSLDProps> = {};
