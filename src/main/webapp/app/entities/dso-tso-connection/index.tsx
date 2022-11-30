import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import DsoTsoConnection from './dso-tso-connection';
import DsoTsoConnectionDetail from './dso-tso-connection-detail';
import DsoTsoConnectionUpdate from './dso-tso-connection-update';
import DsoTsoConnectionDeleteDialog from './dso-tso-connection-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DsoTsoConnectionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DsoTsoConnectionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DsoTsoConnectionDetail} />
      <ErrorBoundaryRoute path={match.url} component={DsoTsoConnection} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DsoTsoConnectionDeleteDialog} />
  </>
);

export default Routes;
