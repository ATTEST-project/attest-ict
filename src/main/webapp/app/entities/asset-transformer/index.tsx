import React from 'react';
import { Switch } from 'react-router-dom';

import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import { AUTHORITIES } from 'app/config/constants';

import AssetTransformer from './asset-transformer';
import AssetTransformerDetail from './asset-transformer-detail';
import AssetTransformerUpdate from './asset-transformer-update';
import AssetTransformerDeleteDialog from './asset-transformer-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <PrivateRoute exact path={`${match.url}/new`} component={AssetTransformerUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AssetTransformerUpdate} />

      <PrivateRoute exact path={`${match.url}/:id/edit`} component={AssetTransformerUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AssetTransformerUpdate} />

      <PrivateRoute exact path={`${match.url}/:id`} component={AssetTransformerDetail} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AssetTransformerDetail} />
      <ErrorBoundaryRoute path={match.url} component={AssetTransformer} />
    </Switch>
    <PrivateRoute exact path={`${match.url}/:id/delete`} component={AssetTransformerDeleteDialog} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AssetTransformerDeleteDialog} />
  </>
);

export default Routes;
