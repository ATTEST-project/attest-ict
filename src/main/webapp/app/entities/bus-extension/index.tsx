import React from 'react';
import { Switch } from 'react-router-dom';

import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import { AUTHORITIES } from 'app/config/constants';

import BusExtension from './bus-extension';
import BusExtensionDetail from './bus-extension-detail';
import BusExtensionUpdate from './bus-extension-update';
import BusExtensionDeleteDialog from './bus-extension-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <PrivateRoute exact path={`${match.url}/new`} component={BusExtensionUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BusExtensionUpdate} />

      <PrivateRoute exact path={`${match.url}/:id/edit`} component={BusExtensionUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BusExtensionUpdate} />

      <PrivateRoute exact path={`${match.url}/:id`} component={BusExtensionDetail} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BusExtensionDetail} />
      <ErrorBoundaryRoute path={match.url} component={BusExtension} />
    </Switch>
    <PrivateRoute exact path={`${match.url}/:id/delete`} component={BusExtensionDeleteDialog} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BusExtensionDeleteDialog} />
  </>
);

export default Routes;
