import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BranchElVal from './branch-el-val';
import BranchElValDetail from './branch-el-val-detail';
import BranchElValUpdate from './branch-el-val-update';
import BranchElValDeleteDialog from './branch-el-val-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BranchElValUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BranchElValUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BranchElValDetail} />
      <ErrorBoundaryRoute path={match.url} component={BranchElVal} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BranchElValDeleteDialog} />
  </>
);

export default Routes;
