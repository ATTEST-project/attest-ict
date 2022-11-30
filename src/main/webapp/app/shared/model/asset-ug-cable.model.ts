import { INetwork } from 'app/shared/model/network.model';

export interface IAssetUGCable {
  id?: number;
  sectionLabel?: string | null;
  circuitId?: number | null;
  conductorCrossSectionalArea?: number | null;
  sheathMaterial?: string | null;
  designVoltage?: string | null;
  operatingVoltage?: string | null;
  insulationTypeSheath?: string | null;
  conductorMaterial?: string | null;
  age?: number | null;
  faultHistory?: number | null;
  lengthOfCableSectionMeters?: number | null;
  sectionRating?: number | null;
  type?: string | null;
  numberOfCores?: number | null;
  netPerformanceCostOfFailureEuro?: string | null;
  repairTimeHour?: number | null;
  network?: INetwork | null;
}

export const defaultValue: Readonly<IAssetUGCable> = {};
