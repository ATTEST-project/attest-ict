import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import NetworkData from 'app/modules/network/data/network-data';
import NetworkDiagram from 'app/modules/network/diagram/network-diagram';
import NetworkMap from 'app/modules/network/map/network-map';
import Network from 'app/entities/network';
import NetworkActions from 'app/modules/network/actions/network-actions';
import NetworkUpload from 'app/modules/network/upload/network-upload';
import NetworkExportDialog from 'app/modules/network/export/network-export-dialog';
import Search from 'app/modules/network/search/search';

/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      <ErrorBoundaryRoute exact path={`${match.url}/search`} component={Search} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/actions`} component={NetworkActions} />
      <ErrorBoundaryRoute path={match.url} component={Network} />
    </Switch>
  </div>
);

export default Routes;
