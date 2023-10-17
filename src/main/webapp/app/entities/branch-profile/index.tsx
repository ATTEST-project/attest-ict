import React from 'react';
import { Switch } from 'react-router-dom';

import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import { AUTHORITIES } from 'app/config/constants';

import BranchProfile from './branch-profile';
import BranchProfileDetail from './branch-profile-detail';
import BranchProfileUpdate from './branch-profile-update';
import BranchProfileDeleteDialog from './branch-profile-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <PrivateRoute exact path={`${match.url}/new`} component={BranchProfileUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BranchProfileUpdate} />

      <PrivateRoute exact path={`${match.url}/:id/edit`} component={BranchProfileUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BranchProfileUpdate} />

      <PrivateRoute exact path={`${match.url}/:id`} component={BranchProfileDetail} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BranchProfileDetail} />
      <ErrorBoundaryRoute path={match.url} component={BranchProfile} />
    </Switch>
    <PrivateRoute exact path={`${match.url}/:id/delete`} component={BranchProfileUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BranchProfileDeleteDialog} />
  </>
);

export default Routes;
