import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SolarData from './solar-data';
import SolarDataDetail from './solar-data-detail';
import SolarDataUpdate from './solar-data-update';
import SolarDataDeleteDialog from './solar-data-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SolarDataUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SolarDataUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SolarDataDetail} />
      <ErrorBoundaryRoute path={match.url} component={SolarData} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SolarDataDeleteDialog} />
  </>
);

export default Routes;
