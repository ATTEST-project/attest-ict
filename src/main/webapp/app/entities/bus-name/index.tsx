import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BusName from './bus-name';
import BusNameDetail from './bus-name-detail';
import BusNameUpdate from './bus-name-update';
import BusNameDeleteDialog from './bus-name-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BusNameUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BusNameUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BusNameDetail} />
      <ErrorBoundaryRoute path={match.url} component={BusName} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BusNameDeleteDialog} />
  </>
);

export default Routes;
