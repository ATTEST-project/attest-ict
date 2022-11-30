import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BusCoordinate from './bus-coordinate';
import BusCoordinateDetail from './bus-coordinate-detail';
import BusCoordinateUpdate from './bus-coordinate-update';
import BusCoordinateDeleteDialog from './bus-coordinate-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BusCoordinateUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BusCoordinateUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BusCoordinateDetail} />
      <ErrorBoundaryRoute path={match.url} component={BusCoordinate} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BusCoordinateDeleteDialog} />
  </>
);

export default Routes;
