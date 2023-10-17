import React from 'react';
import { Switch } from 'react-router-dom';

import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import { AUTHORITIES } from 'app/config/constants';

import AssetUGCable from './asset-ug-cable';
import AssetUGCableDetail from './asset-ug-cable-detail';
import AssetUGCableUpdate from './asset-ug-cable-update';
import AssetUGCableDeleteDialog from './asset-ug-cable-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <PrivateRoute exact path={`${match.url}/new`} component={AssetUGCableUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AssetUGCableUpdate} />

      <PrivateRoute exact path={`${match.url}/:id/edit`} component={AssetUGCableUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AssetUGCableUpdate} />

      <PrivateRoute exact path={`${match.url}/:id`} component={AssetUGCableDetail} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AssetUGCableDetail} />

      <ErrorBoundaryRoute path={match.url} component={AssetUGCable} />
    </Switch>

    <PrivateRoute exact path={`${match.url}/:id/delete`} component={AssetUGCableDeleteDialog} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AssetUGCableDeleteDialog} />
  </>
);

export default Routes;
