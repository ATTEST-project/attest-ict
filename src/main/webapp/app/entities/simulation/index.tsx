import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Simulation from './simulation';
import SimulationDetail from './simulation-detail';
import SimulationUpdate from './simulation-update';
import SimulationDeleteDialog from './simulation-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SimulationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SimulationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SimulationDetail} />
      <ErrorBoundaryRoute path={match.url} component={Simulation} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SimulationDeleteDialog} />
  </>
);

export default Routes;
