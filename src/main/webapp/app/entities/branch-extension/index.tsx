import React from 'react';
import { Switch } from 'react-router-dom';

import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import { AUTHORITIES } from 'app/config/constants';

import BranchExtension from './branch-extension';
import BranchExtensionDetail from './branch-extension-detail';
import BranchExtensionUpdate from './branch-extension-update';
import BranchExtensionDeleteDialog from './branch-extension-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <PrivateRoute exact path={`${match.url}/new`} component={BranchExtensionUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BranchExtensionUpdate} />

      <PrivateRoute exact path={`${match.url}/:id/edit`} component={BranchExtensionUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BranchExtensionUpdate} />

      <PrivateRoute exact path={`${match.url}/:id`} component={BranchExtensionDetail} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BranchExtensionDetail} />
      <ErrorBoundaryRoute path={match.url} component={BranchExtension} />
    </Switch>

    <PrivateRoute exact path={`${match.url}/:id/delete`} component={BranchExtensionDeleteDialog} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BranchExtensionDeleteDialog} />
  </>
);

export default Routes;
