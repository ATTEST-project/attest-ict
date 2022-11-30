import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import WindData from './wind-data';
import WindDataDetail from './wind-data-detail';
import WindDataUpdate from './wind-data-update';
import WindDataDeleteDialog from './wind-data-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WindDataUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WindDataUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WindDataDetail} />
      <ErrorBoundaryRoute path={match.url} component={WindData} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={WindDataDeleteDialog} />
  </>
);

export default Routes;
