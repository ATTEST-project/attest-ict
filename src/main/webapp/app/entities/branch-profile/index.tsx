import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BranchProfile from './branch-profile';
import BranchProfileDetail from './branch-profile-detail';
import BranchProfileUpdate from './branch-profile-update';
import BranchProfileDeleteDialog from './branch-profile-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BranchProfileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BranchProfileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BranchProfileDetail} />
      <ErrorBoundaryRoute path={match.url} component={BranchProfile} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BranchProfileDeleteDialog} />
  </>
);

export default Routes;
