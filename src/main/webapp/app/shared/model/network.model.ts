import dayjs from 'dayjs';
import { IBus } from 'app/shared/model/bus.model';
import { IGenerator } from 'app/shared/model/generator.model';
import { IBranch } from 'app/shared/model/branch.model';
import { IStorage } from 'app/shared/model/storage.model';
import { ITransformer } from 'app/shared/model/transformer.model';
import { ICapacitorBankData } from 'app/shared/model/capacitor-bank-data.model';
import { IInputFile } from 'app/shared/model/input-file.model';
import { IAssetUGCable } from 'app/shared/model/asset-ug-cable.model';
import { IAssetTransformer } from 'app/shared/model/asset-transformer.model';
import { IBillingConsumption } from 'app/shared/model/billing-consumption.model';
import { IBillingDer } from 'app/shared/model/billing-der.model';
import { ILineCable } from 'app/shared/model/line-cable.model';
import { IGenProfile } from 'app/shared/model/gen-profile.model';
import { ILoadProfile } from 'app/shared/model/load-profile.model';
import { IFlexProfile } from 'app/shared/model/flex-profile.model';
import { ITransfProfile } from 'app/shared/model/transf-profile.model';
import { IBranchProfile } from 'app/shared/model/branch-profile.model';
import { ITopologyBus } from 'app/shared/model/topology-bus.model';
import { IDsoTsoConnection } from 'app/shared/model/dso-tso-connection.model';
import { IBaseMVA } from 'app/shared/model/base-mva.model';
import { IVoltageLevel } from 'app/shared/model/voltage-level.model';
import { ISimulation } from 'app/shared/model/simulation.model';

export interface INetwork {
  id?: number;
  name?: string | null;
  mpcName?: string | null;
  country?: string | null;
  type?: string | null;
  description?: string | null;
  isDeleted?: boolean;
  networkDate?: string | null;
  version?: number | null;
  creationDateTime?: string | null;
  updateDateTime?: string | null;
  buses?: IBus[] | null;
  generators?: IGenerator[] | null;
  branches?: IBranch[] | null;
  storages?: IStorage[] | null;
  transformers?: ITransformer[] | null;
  capacitors?: ICapacitorBankData[] | null;
  inputFiles?: IInputFile[] | null;
  assetUgCables?: IAssetUGCable[] | null;
  assetTransformers?: IAssetTransformer[] | null;
  billingConsumptions?: IBillingConsumption[] | null;
  billingDers?: IBillingDer[] | null;
  lineCables?: ILineCable[] | null;
  genProfiles?: IGenProfile[] | null;
  loadProfiles?: ILoadProfile[] | null;
  flexProfiles?: IFlexProfile[] | null;
  transfProfiles?: ITransfProfile[] | null;
  branchProfiles?: IBranchProfile[] | null;
  topologyBuses?: ITopologyBus[] | null;
  dsoTsoConnections?: IDsoTsoConnection[] | null;
  baseMVA?: IBaseMVA | null;
  voltageLevel?: IVoltageLevel | null;
  simulations?: ISimulation[] | null;
}

export const defaultValue: Readonly<INetwork> = {
  isDeleted: false,
};
