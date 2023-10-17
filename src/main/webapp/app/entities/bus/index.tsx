import React from 'react';
import { Switch } from 'react-router-dom';

import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import { AUTHORITIES } from 'app/config/constants';

import Bus from './bus';
import BusDetail from './bus-detail';
import BusUpdate from './bus-update';
import BusDeleteDialog from './bus-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <PrivateRoute exact path={`${match.url}/new`} component={BusUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BusUpdate} />

      <PrivateRoute exact path={`${match.url}/:id/edit`} component={BusUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BusUpdate} />

      <PrivateRoute exact path={`${match.url}/:id`} component={BusDetail} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BusDetail} />

      <ErrorBoundaryRoute path={match.url} component={Bus} />
    </Switch>

    <PrivateRoute exact path={`${match.url}/:id/delete`} component={BusDeleteDialog} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BusDeleteDialog} />
  </>
);

export default Routes;
