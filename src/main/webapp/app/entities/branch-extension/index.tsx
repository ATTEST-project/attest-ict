import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BranchExtension from './branch-extension';
import BranchExtensionDetail from './branch-extension-detail';
import BranchExtensionUpdate from './branch-extension-update';
import BranchExtensionDeleteDialog from './branch-extension-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BranchExtensionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BranchExtensionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BranchExtensionDetail} />
      <ErrorBoundaryRoute path={match.url} component={BranchExtension} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BranchExtensionDeleteDialog} />
  </>
);

export default Routes;
