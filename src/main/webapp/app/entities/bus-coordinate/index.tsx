import React from 'react';
import { Switch } from 'react-router-dom';

import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import { AUTHORITIES } from 'app/config/constants';

import BusCoordinate from './bus-coordinate';
import BusCoordinateDetail from './bus-coordinate-detail';
import BusCoordinateUpdate from './bus-coordinate-update';
import BusCoordinateDeleteDialog from './bus-coordinate-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <PrivateRoute exact path={`${match.url}/new`} component={BusCoordinateUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BusCoordinateUpdate} />

      <PrivateRoute exact path={`${match.url}/:id/edit`} component={BusCoordinateUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BusCoordinateUpdate} />

      <PrivateRoute exact path={`${match.url}/:id`} component={BusCoordinateDetail} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BusCoordinateDetail} />

      <ErrorBoundaryRoute path={match.url} component={BusCoordinate} />
    </Switch>

    <PrivateRoute exact path={`${match.url}/:id/delete`} component={BusCoordinateDeleteDialog} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BusCoordinateDeleteDialog} />
  </>
);

export default Routes;
