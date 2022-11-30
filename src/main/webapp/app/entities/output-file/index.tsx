import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import OutputFile from './output-file';
import OutputFileDetail from './output-file-detail';
import OutputFileUpdate from './output-file-update';
import OutputFileDeleteDialog from './output-file-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OutputFileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OutputFileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OutputFileDetail} />
      <ErrorBoundaryRoute path={match.url} component={OutputFile} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={OutputFileDeleteDialog} />
  </>
);

export default Routes;
