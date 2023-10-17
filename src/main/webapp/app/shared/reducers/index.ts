import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';
import authentication from './authentication';
import applicationProfile from './application-profile';
import administration from 'app/modules/administration/administration.reducer';
import userManagement from './user-management';
// prettier-ignore
import network from 'app/entities/network/network.reducer';
// prettier-ignore
import networkEntireSLD from 'app/entities/network/network-sld-entire.reducer';
// prettier-ignore
import networkSubstationsSLD from 'app/entities/network/network-sld-substations.reducer';
// prettier-ignore
import networkSLDChart from 'app/entities/network/network-sld-entire-chart.reducer';
// prettier-ignore
import networkMatpowerUpload from 'app/entities/network/network-matpower-upload.reducer';
// prettier-ignore
import networkExport from 'app/entities/network/network-export.reducer';
// prettier-ignore
import networkCSVProfileUpload from 'app/entities/network/network-csv-profile.reducer';
// prettier-ignore
import networkExcelProfileUpload from 'app/entities/network/network-excel-profile.reducer';
// prettier-ignore
import networkSearch from 'app/entities/network/network-search.reducer';
// prettier-ignore
import baseMVA from 'app/entities/base-mva/base-mva.reducer';
// prettier-ignore
import bus from 'app/entities/bus/bus.reducer';
// prettier-ignore
import busName from 'app/entities/bus-name/bus-name.reducer';
// prettier-ignore
import busCoordinate from 'app/entities/bus-coordinate/bus-coordinate.reducer';
// prettier-ignore
import busExtension from 'app/entities/bus-extension/bus-extension.reducer';
// prettier-ignore
import branch from 'app/entities/branch/branch.reducer';
// prettier-ignore
import loadProfile from 'app/entities/load-profile/load-profile.reducer';
// prettier-ignore
import loadElVal from 'app/entities/load-el-val/load-el-val.reducer';
// prettier-ignore
import generator from 'app/entities/generator/generator.reducer';
// prettier-ignore
import generatorExtension from 'app/entities/generator-extension/generator-extension.reducer';
// prettier-ignore
import genTag from 'app/entities/gen-tag/gen-tag.reducer';
// prettier-ignore
import genProfile from 'app/entities/gen-profile/gen-profile.reducer';
// prettier-ignore
import genElVal from 'app/entities/gen-el-val/gen-el-val.reducer';
// prettier-ignore
import billingDer from 'app/entities/billing-der/billing-der.reducer';
// prettier-ignore
import storage from 'app/entities/storage/storage.reducer';
// prettier-ignore
import storageCost from 'app/entities/storage-cost/storage-cost.reducer';
// prettier-ignore
import toolLogFile from 'app/entities/tool-log-file/tool-log-file.reducer';
// prettier-ignore
import flexProfile from 'app/entities/flex-profile/flex-profile.reducer';
// prettier-ignore
import flexElVal from 'app/entities/flex-el-val/flex-el-val.reducer';
// prettier-ignore
import flexCost from 'app/entities/flex-cost/flex-cost.reducer';
// prettier-ignore
import transfElVal from 'app/entities/transf-el-val/transf-el-val.reducer';
// prettier-ignore
import genCost from 'app/entities/gen-cost/gen-cost.reducer';
// prettier-ignore
import protectionTool from 'app/entities/protection-tool/protection-tool.reducer';
// prettier-ignore
import transformer from 'app/entities/transformer/transformer.reducer';
// prettier-ignore
import weatherForecast from 'app/entities/weather-forecast/weather-forecast.reducer';
// prettier-ignore
import capacitorBankData from 'app/entities/capacitor-bank-data/capacitor-bank-data.reducer';
// prettier-ignore
import lineCable from 'app/entities/line-cable/line-cable.reducer';
// prettier-ignore
import assetTransformer from 'app/entities/asset-transformer/asset-transformer.reducer';
// prettier-ignore
import node from 'app/entities/node/node.reducer';
// prettier-ignore
import solarData from 'app/entities/solar-data/solar-data.reducer';
// prettier-ignore
import price from 'app/entities/price/price.reducer';
// prettier-ignore
import assetUGCable from 'app/entities/asset-ug-cable/asset-ug-cable.reducer';
// prettier-ignore
import topology from 'app/entities/topology/topology.reducer';
// prettier-ignore
import outputFile from 'app/entities/output-file/output-file.reducer';
// prettier-ignore
import topologyBus from 'app/entities/topology-bus/topology-bus.reducer';
// prettier-ignore
import voltageLevel from 'app/entities/voltage-level/voltage-level.reducer';
// prettier-ignore
import transfProfile from 'app/entities/transf-profile/transf-profile.reducer';
// prettier-ignore
import windData from 'app/entities/wind-data/wind-data.reducer';
// prettier-ignore
import billingConsumption from 'app/entities/billing-consumption/billing-consumption.reducer';
// prettier-ignore
import branchExtension from 'app/entities/branch-extension/branch-extension.reducer';
// prettier-ignore
import task from 'app/entities/task/task.reducer';
// prettier-ignore
import inputFile from 'app/entities/input-file/input-file.reducer';
// prettier-ignore
import dsoTsoConnection from 'app/entities/dso-tso-connection/dso-tso-connection.reducer';
// prettier-ignore
import tool from 'app/entities/tool/tool.reducer';
// prettier-ignore
import branchElVal from 'app/entities/branch-el-val/branch-el-val.reducer';
// prettier-ignore
import branchProfile from 'app/entities/branch-profile/branch-profile.reducer';
// prettier-ignore
import simulation from 'app/entities/simulation/simulation.reducer';
// prettier-ignore
import toolParameter from 'app/entities/tool-parameter/tool-parameter.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */
// prettier-ignore
import toolConfig from 'app/shared/reducers/tool-config';
// prettier-ignore
import backButtonDisplay from 'app/shared/reducers/back-button-display';
// prettier-ignore
import chooseSubstation from 'app/shared/reducers/choose-substation';
// prettier-ignore
import toolsExecution from 'app/modules/tools/reducer/tools-execution.reducer';
// prettier-ignore
import toolsResults from 'app/modules/tools/WP4/reducer/tools-results.reducer';
// prettier-ignore
import toolsResultsTable from 'app/modules/tools/WP4/reducer/tools-results-table.reducer';
// prettier-ignore
import t31ToolExecution from 'app/modules/tools/WP3/T31/reducer/tool-execution.reducer';
// prettier-ignore
import t31ToolResults from 'app/modules/tools/WP3/T31/reducer/tool-results.reducer';
// prettier-ignore
import branchLength from 'app/modules/tools/WP3/T31/reducer/branch-length.reducer';
// prettier-ignore
import backButtonPath from 'app/shared/reducers/back-button-path';
// prettier-ignore
import t52ToolExecution from 'app/modules/tools/WP5/T52/reducer/tool-execution.reducer';
// prettier-ignore
import t52FileHeader from 'app/modules/tools/WP5/T52/reducer/tool-file-header.reducer';
// prettier-ignore
import t52ToolTable from 'app/modules/tools/WP5/T52/reducer/tool-table.reducer';
// prettier-ignore
import t52ToolCharts from 'app/modules/tools/WP5/T52/reducer/tool-charts.reducer';
// prettier-ignore
import t511ToolExecution from 'app/modules/tools/WP5/T51/characterization-tool/reducer/tool-execution.reducer';
// prettier-ignore
import t511FileHeader from 'app/modules/tools/WP5/T51/characterization-tool/reducer/tool-file-header.reducer';
// prettier-ignore
import t511ToolTable from 'app/modules/tools/WP5/T51/characterization-tool/reducer/tool-table.reducer';
// prettier-ignore
import t511ToolCharts from 'app/modules/tools/WP5/T51/characterization-tool/reducer/tool-charts.reducer';
// prettier-ignore
import t512ToolExecution from 'app/modules/tools/WP5/T51/monitoring-tool/reducer/tool-execution.reducer';
// prettier-ignore
import t512FileHeader from 'app/modules/tools/WP5/T51/monitoring-tool/reducer/tool-file-header.reducer';
// prettier-ignore
import t512ToolTable from 'app/modules/tools/WP5/T51/monitoring-tool/reducer/tool-table.reducer';
// prettier-ignore
import t512ToolCharts from 'app/modules/tools/WP5/T51/monitoring-tool/reducer/tool-charts.reducer';
// prettier-ignore
import t53ToolExecution from 'app/modules/tools/WP5/T53/reducer/tool-execution.reducer';
// prettier-ignore
import t53ToolTable from 'app/modules/tools/WP5/T53/reducer/tool-table.reducer';
// prettier-ignore
import t53ToolCharts from 'app/modules/tools/WP5/T53/reducer/tool-charts.reducer';
// prettier-ignore
import t32ToolExecution from 'app/modules/tools/WP3/T32/reducer/tool-execution.reducer';
// prettier-ignore
import t32ToolResults from 'app/modules/tools/WP3/T32/reducer/tool-results.reducer';
// prettier-ignore
import t251ToolExecution from 'app/modules/tools/WP2/T251/reducer/tool-execution.reducer';
// prettier-ignore
import t251ToolResults from 'app/modules/tools/WP2/T251/reducer/tool-results.reducer';
// prettier-ignore
import t252ToolExecution from 'app/modules/tools/WP2/T252/reducer/tool-execution.reducer';
// prettier-ignore
import t252ToolResults from 'app/modules/tools/WP2/T252/reducer/tool-results.reducer';
// prettier-ignore
import t26ToolExecution from 'app/modules/tools/WP2/T26/reducer/tool-execution.reducer';
// prettier-ignore
import t26ToolResults from 'app/modules/tools/WP2/T26/reducer/tool-results.reducer';

// prettier-ignore
import t33ToolExecution from 'app/modules/tools/WP3/T33/reducer/tool-execution.reducer';

// prettier-ignore
import t33ResultsSelect from 'app/modules/tools/WP3/T33/reducer/tool-results-select.reducer';

// prettier-ignore
import t33ResultsTableCharts from 'app/modules/tools/WP3/T33/reducer/tool-results-table-charts.reducer';

// prettier-ignore
import t33ToolResults from 'app/modules/tools/WP3/T33/reducer/tool-results.reducer';

// prettier-ignore
import t41ResultsSelect from 'app/modules/tools/WP4/T41/tractability-tool/reducer/tool-results-select.reducer';

// prettier-ignore
import t41ResultsTableCharts from 'app/modules/tools/WP4/T41/tractability-tool/reducer/tool-results-table-charts.reducer';

// prettier-ignore
import realTimeToolResultsSelect from 'app/modules/tools/WP4/reducer/tool-results-select.reducer';

// prettier-ignore
import realTimeToolResultsTableCharts from 'app/modules/tools/WP4/reducer/tool-results-table-charts.reducer';

import t44ResultsSelect from 'app/modules/tools/WP4/T44/reducer/tool-results-select.reducer';

import networkImportFromCimRepo from 'app/shared/reducers/network-import-from-cim-repo';

import toolResultsSearch from 'app/shared/reducers/tool-results-search';

const rootReducer = {
  authentication,
  applicationProfile,
  administration,
  userManagement,
  network,
  networkEntireSLD,
  networkSubstationsSLD,
  networkSLDChart,
  networkMatpowerUpload,
  networkExport,
  networkCSVProfileUpload,
  networkExcelProfileUpload,
  networkSearch,
  baseMVA,
  bus,
  busName,
  busCoordinate,
  busExtension,
  branch,
  loadProfile,
  loadElVal,
  generator,
  generatorExtension,
  genTag,
  genProfile,
  genElVal,
  billingDer,
  storage,
  storageCost,
  toolLogFile,
  flexProfile,
  flexElVal,
  flexCost,
  transfElVal,
  genCost,
  protectionTool,
  transformer,
  weatherForecast,
  capacitorBankData,
  lineCable,
  assetTransformer,
  node,
  solarData,
  price,
  assetUGCable,
  topology,
  outputFile,
  topologyBus,
  voltageLevel,
  transfProfile,
  windData,
  billingConsumption,
  branchExtension,
  task,
  inputFile,
  dsoTsoConnection,
  tool,
  branchElVal,
  branchProfile,
  simulation,
  toolParameter,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
  toolConfig,
  backButtonDisplay,
  chooseSubstation,
  toolsExecution,
  toolsResults,
  toolsResultsTable,
  t31ToolExecution,
  t31ToolResults,
  branchLength,
  backButtonPath,
  t52ToolExecution,
  t52FileHeader,
  t52ToolTable,
  t52ToolCharts,
  t511ToolExecution,
  t511FileHeader,
  t511ToolTable,
  t511ToolCharts,
  t512ToolExecution,
  t512FileHeader,
  t512ToolTable,
  t512ToolCharts,
  t53ToolExecution,
  t53ToolTable,
  t53ToolCharts,
  t32ToolExecution,
  t32ToolResults,
  t251ToolExecution,
  t251ToolResults,
  t252ToolExecution,
  t252ToolResults,
  t26ToolExecution,
  t26ToolResults,
  t33ToolExecution,
  t33ResultsSelect,
  t33ResultsTableCharts,
  t33ToolResults,
  t41ResultsSelect,
  t41ResultsTableCharts,
  realTimeToolResultsSelect,
  realTimeToolResultsTableCharts,
  t44ResultsSelect,
  networkImportFromCimRepo,
  toolResultsSearch,
};

export default rootReducer;
