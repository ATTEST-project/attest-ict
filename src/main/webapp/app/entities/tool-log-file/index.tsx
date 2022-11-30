import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ToolLogFile from './tool-log-file';
import ToolLogFileDetail from './tool-log-file-detail';
import ToolLogFileUpdate from './tool-log-file-update';
import ToolLogFileDeleteDialog from './tool-log-file-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ToolLogFileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ToolLogFileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ToolLogFileDetail} />
      <ErrorBoundaryRoute path={match.url} component={ToolLogFile} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ToolLogFileDeleteDialog} />
  </>
);

export default Routes;
