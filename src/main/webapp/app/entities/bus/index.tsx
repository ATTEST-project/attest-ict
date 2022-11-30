import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Bus from './bus';
import BusDetail from './bus-detail';
import BusUpdate from './bus-update';
import BusDeleteDialog from './bus-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BusUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BusUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BusDetail} />
      <ErrorBoundaryRoute path={match.url} component={Bus} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BusDeleteDialog} />
  </>
);

export default Routes;
