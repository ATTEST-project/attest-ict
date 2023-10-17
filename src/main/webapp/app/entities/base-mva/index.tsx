import React from 'react';
import { Switch } from 'react-router-dom';

import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import { AUTHORITIES } from 'app/config/constants';

import BaseMVA from './base-mva';
import BaseMVADetail from './base-mva-detail';
import BaseMVAUpdate from './base-mva-update';
import BaseMVADeleteDialog from './base-mva-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <PrivateRoute exact path={`${match.url}/new`} component={BaseMVAUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BaseMVAUpdate} />

      <PrivateRoute exact path={`${match.url}/:id/edit`} component={BaseMVAUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BaseMVAUpdate} />

      <PrivateRoute exact path={`${match.url}/:id`} component={BaseMVADetail} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BaseMVADetail} />

      <ErrorBoundaryRoute path={match.url} component={BaseMVA} />
    </Switch>

    <PrivateRoute exact path={`${match.url}/:id/delete`} component={BaseMVADeleteDialog} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BaseMVADeleteDialog} />
  </>
);

export default Routes;
