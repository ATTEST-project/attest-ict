import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProtectionTool from './protection-tool';
import ProtectionToolDetail from './protection-tool-detail';
import ProtectionToolUpdate from './protection-tool-update';
import ProtectionToolDeleteDialog from './protection-tool-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProtectionToolUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProtectionToolUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProtectionToolDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProtectionTool} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProtectionToolDeleteDialog} />
  </>
);

export default Routes;
