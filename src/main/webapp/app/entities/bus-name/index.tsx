import React from 'react';
import { Switch } from 'react-router-dom';

import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import { AUTHORITIES } from 'app/config/constants';

import BusName from './bus-name';
import BusNameDetail from './bus-name-detail';
import BusNameUpdate from './bus-name-update';
import BusNameDeleteDialog from './bus-name-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <PrivateRoute exact path={`${match.url}/new`} component={BusNameUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BusNameUpdate} />

      <PrivateRoute exact path={`${match.url}/:id/edit`} component={BusNameUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BusNameUpdate} />

      <PrivateRoute exact path={`${match.url}/:id`} component={BusNameDetail} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BusNameDetail} />

      <ErrorBoundaryRoute path={match.url} component={BusName} />
    </Switch>
    <PrivateRoute exact path={`${match.url}/:id/delete`} component={BusNameDeleteDialog} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BusNameDeleteDialog} />
  </>
);

export default Routes;
