import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ToolParameter from './tool-parameter';
import ToolParameterDetail from './tool-parameter-detail';
import ToolParameterUpdate from './tool-parameter-update';
import ToolParameterDeleteDialog from './tool-parameter-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ToolParameterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ToolParameterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ToolParameterDetail} />
      <ErrorBoundaryRoute path={match.url} component={ToolParameter} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ToolParameterDeleteDialog} />
  </>
);

export default Routes;
