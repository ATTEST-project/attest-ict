import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Network from 'app/modules/network';
import BaseMVA from './base-mva';
import Bus from './bus';
import BusName from './bus-name';
import BusCoordinate from './bus-coordinate';
import BusExtension from './bus-extension';
import Branch from './branch';
import LoadProfile from './load-profile';
import LoadElVal from './load-el-val';
import Generator from './generator';
import GeneratorExtension from './generator-extension';
import GenTag from './gen-tag';
import GenProfile from './gen-profile';
import GenElVal from './gen-el-val';
import BillingDer from './billing-der';
import Storage from './storage';
import StorageCost from './storage-cost';
import ToolLogFile from './tool-log-file';
import FlexProfile from './flex-profile';
import FlexElVal from './flex-el-val';
import FlexCost from './flex-cost';
import TransfElVal from './transf-el-val';
import GenCost from './gen-cost';
import ProtectionTool from './protection-tool';
import Transformer from './transformer';
import WeatherForecast from './weather-forecast';
import CapacitorBankData from './capacitor-bank-data';
import LineCable from './line-cable';
import AssetTransformer from './asset-transformer';
import Node from './node';
import SolarData from './solar-data';
import Price from './price';
import AssetUGCable from './asset-ug-cable';
import Topology from './topology';
import OutputFile from './output-file';
import TopologyBus from './topology-bus';
import VoltageLevel from './voltage-level';
import TransfProfile from './transf-profile';
import WindData from './wind-data';
import BillingConsumption from './billing-consumption';
import BranchExtension from './branch-extension';
import Task from './task';
// import Network from './network';
import InputFile from './input-file';
import DsoTsoConnection from './dso-tso-connection';
import Tool from './tool';
import BranchElVal from './branch-el-val';
import BranchProfile from './branch-profile';
import Simulation from './simulation';
import ToolParameter from './tool-parameter';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}network`} component={Network} />
      <ErrorBoundaryRoute path={`${match.url}base-mva`} component={BaseMVA} />
      <ErrorBoundaryRoute path={`${match.url}bus`} component={Bus} />
      <ErrorBoundaryRoute path={`${match.url}bus-name`} component={BusName} />
      <ErrorBoundaryRoute path={`${match.url}bus-coordinate`} component={BusCoordinate} />
      <ErrorBoundaryRoute path={`${match.url}bus-extension`} component={BusExtension} />
      <ErrorBoundaryRoute path={`${match.url}branch`} component={Branch} />
      <ErrorBoundaryRoute path={`${match.url}load-profile`} component={LoadProfile} />
      <ErrorBoundaryRoute path={`${match.url}load-el-val`} component={LoadElVal} />
      <ErrorBoundaryRoute path={`${match.url}generator`} component={Generator} />
      <ErrorBoundaryRoute path={`${match.url}generator-extension`} component={GeneratorExtension} />
      <ErrorBoundaryRoute path={`${match.url}gen-tag`} component={GenTag} />
      <ErrorBoundaryRoute path={`${match.url}gen-profile`} component={GenProfile} />
      <ErrorBoundaryRoute path={`${match.url}gen-el-val`} component={GenElVal} />
      <ErrorBoundaryRoute path={`${match.url}billing-der`} component={BillingDer} />
      <ErrorBoundaryRoute path={`${match.url}storage`} component={Storage} />
      <ErrorBoundaryRoute path={`${match.url}storage-cost`} component={StorageCost} />
      <ErrorBoundaryRoute path={`${match.url}tool-log-file`} component={ToolLogFile} />
      <ErrorBoundaryRoute path={`${match.url}flex-profile`} component={FlexProfile} />
      <ErrorBoundaryRoute path={`${match.url}flex-el-val`} component={FlexElVal} />
      <ErrorBoundaryRoute path={`${match.url}flex-cost`} component={FlexCost} />
      <ErrorBoundaryRoute path={`${match.url}transf-el-val`} component={TransfElVal} />
      <ErrorBoundaryRoute path={`${match.url}gen-cost`} component={GenCost} />
      <ErrorBoundaryRoute path={`${match.url}protection-tool`} component={ProtectionTool} />
      <ErrorBoundaryRoute path={`${match.url}transformer`} component={Transformer} />
      <ErrorBoundaryRoute path={`${match.url}weather-forecast`} component={WeatherForecast} />
      <ErrorBoundaryRoute path={`${match.url}capacitor-bank-data`} component={CapacitorBankData} />
      <ErrorBoundaryRoute path={`${match.url}line-cable`} component={LineCable} />
      <ErrorBoundaryRoute path={`${match.url}asset-transformer`} component={AssetTransformer} />
      <ErrorBoundaryRoute path={`${match.url}node`} component={Node} />
      <ErrorBoundaryRoute path={`${match.url}solar-data`} component={SolarData} />
      <ErrorBoundaryRoute path={`${match.url}price`} component={Price} />
      <ErrorBoundaryRoute path={`${match.url}asset-ug-cable`} component={AssetUGCable} />
      <ErrorBoundaryRoute path={`${match.url}topology`} component={Topology} />
      <ErrorBoundaryRoute path={`${match.url}output-file`} component={OutputFile} />
      <ErrorBoundaryRoute path={`${match.url}topology-bus`} component={TopologyBus} />
      <ErrorBoundaryRoute path={`${match.url}voltage-level`} component={VoltageLevel} />
      <ErrorBoundaryRoute path={`${match.url}transf-profile`} component={TransfProfile} />
      <ErrorBoundaryRoute path={`${match.url}wind-data`} component={WindData} />
      <ErrorBoundaryRoute path={`${match.url}billing-consumption`} component={BillingConsumption} />
      <ErrorBoundaryRoute path={`${match.url}branch-extension`} component={BranchExtension} />
      <ErrorBoundaryRoute path={`${match.url}task`} component={Task} />
      <ErrorBoundaryRoute path={`${match.url}input-file`} component={InputFile} />
      <ErrorBoundaryRoute path={`${match.url}dso-tso-connection`} component={DsoTsoConnection} />
      <ErrorBoundaryRoute path={`${match.url}tool`} component={Tool} />
      <ErrorBoundaryRoute path={`${match.url}branch-el-val`} component={BranchElVal} />
      <ErrorBoundaryRoute path={`${match.url}branch-profile`} component={BranchProfile} />
      <ErrorBoundaryRoute path={`${match.url}simulation`} component={Simulation} />
      <ErrorBoundaryRoute path={`${match.url}tool-parameter`} component={ToolParameter} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
