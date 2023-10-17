import React from 'react';
import { Switch } from 'react-router-dom';

import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';
import Generator from './generator';
import GeneratorDetail from './generator-detail';
import GeneratorUpdate from './generator-update';
import GeneratorDeleteDialog from './generator-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <PrivateRoute exact path={`${match.url}/new`} component={GeneratorUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GeneratorUpdate} />

      <PrivateRoute exact path={`${match.url}/:id/edit`} component={GeneratorUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GeneratorUpdate} />

      <PrivateRoute exact path={`${match.url}/:id`} component={GeneratorDetail} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GeneratorDetail} />

      <ErrorBoundaryRoute path={match.url} component={Generator} />
    </Switch>

    <PrivateRoute exact path={`${match.url}/:id/delete`} component={GeneratorDeleteDialog} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GeneratorDeleteDialog} />
  </>
);

export default Routes;
