import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import Tools from './tools';
import T41 from 'app/modules/tools/T41/T41';
import T41Results from 'app/modules/tools/T41/results/T41-results';
import T25DayAhead from 'app/modules/tools/T25/dayahead/T25-dayahead';
import T25RealTime from 'app/modules/tools/T25/realtime/T25-realtime';
import T41TractabilityTool from 'app/modules/tools/WP4/T41/tractability-tool/tractability-tool';
import T41TractabilityResults from 'app/modules/tools/WP4/T41/tractability-tool/results/results';
import ScenarioGenTool from 'app/modules/tools/WP4/scenario-gen-tool/scenario-gen-tool';
import ScenarioGenToolOld from 'app/modules/tools/WP4/scenario-gen-tool/old/scenario-gen-tool';
import ScenarioGenToolResults from 'app/modules/tools/WP4/scenario-gen-tool/results/results';
import SpreadSheet from 'app/shared/components/spreadsheet/spreadsheet';
import T44 from 'app/modules/tools/WP4/T44/T44';
import T44Results from 'app/modules/tools/WP4/T44/results/results';
import T31 from 'app/modules/tools/WP3/T31/T31';
import T31Results from 'app/modules/tools/WP3/T31/results/results';
import T52 from 'app/modules/tools/WP5/T52/T52';
import T52Results from 'app/modules/tools/WP5/T52/results/results';
import T51 from 'app/modules/tools/WP5/T51/T51';
import T51Characterization from 'app/modules/tools/WP5/T51/characterization-tool/characterization-tool';
import T51Monitoring from 'app/modules/tools/WP5/T51/monitoring-tool/monitoring-tool';
import T51CharacterizationResults from 'app/modules/tools/WP5/T51/characterization-tool/results/results';
import T51MonitoringResults from 'app/modules/tools/WP5/T51/monitoring-tool/results/results';
import T53 from 'app/modules/tools/WP5/T53/T53';
import T53Results from 'app/modules/tools/WP5/T53/results/results';
import T32 from 'app/modules/tools/WP3/T32/T32';
import T32Results from 'app/modules/tools/WP3/T32/results/results';
import T251 from 'app/modules/tools/WP2/T251/T251';
import T252 from 'app/modules/tools/WP2/T252/T252';
import T26 from 'app/modules/tools/WP2/T26/T26';

/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      <ErrorBoundaryRoute exact path={match.url + '/t251'} component={T251} />
      <ErrorBoundaryRoute exact path={match.url + '/t252'} component={T252} />
      <ErrorBoundaryRoute exact path={match.url + '/t26'} component={T26} />
      <ErrorBoundaryRoute exact path={match.url + '/t31'} component={T31} />
      <ErrorBoundaryRoute exact path={match.url + '/t31/results'} component={T31Results} />
      <ErrorBoundaryRoute exact path={match.url + '/t32'} component={T32} />
      <ErrorBoundaryRoute exact path={match.url + '/t32/results'} component={T32Results} />
      <ErrorBoundaryRoute exact path={match.url + '/sgt'} component={ScenarioGenTool} />
      <ErrorBoundaryRoute exact path={match.url + '/sgt-old'} component={ScenarioGenToolOld} />
      <ErrorBoundaryRoute path={match.url + '/sgt/results'} component={ScenarioGenToolResults} />
      <ErrorBoundaryRoute exact path={match.url + '/t41'} component={T41TractabilityTool} />
      <ErrorBoundaryRoute exact path={match.url + '/t41/excel'} component={SpreadSheet} />
      <ErrorBoundaryRoute path={match.url + '/t41/results'} component={T41TractabilityResults} />
      <ErrorBoundaryRoute exact path={match.url + '/t44'} component={T44} />
      <ErrorBoundaryRoute path={match.url + '/t44/results'} component={T44Results} />
      <ErrorBoundaryRoute exact path={match.url + '/t51'} component={T51} />
      <ErrorBoundaryRoute exact path={match.url + '/t51/characterization'} component={T51Characterization} />
      <ErrorBoundaryRoute exact path={match.url + '/t51/characterization/results'} component={T51CharacterizationResults} />
      <ErrorBoundaryRoute exact path={match.url + '/t51/monitoring'} component={T51Monitoring} />
      <ErrorBoundaryRoute exact path={match.url + '/t51/monitoring/results'} component={T51MonitoringResults} />
      <ErrorBoundaryRoute exact path={match.url + '/t52'} component={T52} />
      <ErrorBoundaryRoute exact path={match.url + '/t52/results'} component={T52Results} />
      <ErrorBoundaryRoute exact path={match.url + '/t53'} component={T53} />
      <ErrorBoundaryRoute exact path={match.url + '/t53/results'} component={T53Results} />
      <ErrorBoundaryRoute exact path={match.url} component={Tools} />
    </Switch>
  </div>
);

export default Routes;
