import React from 'react';
import { Switch } from 'react-router-dom';

import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import { AUTHORITIES } from 'app/config/constants';

import BranchElVal from './branch-el-val';
import BranchElValDetail from './branch-el-val-detail';
import BranchElValUpdate from './branch-el-val-update';
import BranchElValDeleteDialog from './branch-el-val-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <PrivateRoute exact path={`${match.url}/new`} component={BranchElValUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BranchElValUpdate} />

      <PrivateRoute exact path={`${match.url}/:id/edit`} component={BranchElValUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BranchElValUpdate} />

      <PrivateRoute exact path={`${match.url}/:id`} component={BranchElValDetail} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BranchElValDetail} />
      <ErrorBoundaryRoute path={match.url} component={BranchElVal} />
    </Switch>
    <PrivateRoute exact path={`${match.url}/:id/delete`} component={BranchElValDeleteDialog} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BranchElValDeleteDialog} />
  </>
);

export default Routes;
