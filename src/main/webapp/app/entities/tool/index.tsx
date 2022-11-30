import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Tool from './tool';
import ToolDetail from './tool-detail';
import ToolUpdate from './tool-update';
import ToolDeleteDialog from './tool-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ToolUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ToolUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ToolDetail} />
      <ErrorBoundaryRoute path={match.url} component={Tool} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ToolDeleteDialog} />
  </>
);

export default Routes;
