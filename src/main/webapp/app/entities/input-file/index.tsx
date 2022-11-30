import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import InputFile from './input-file';
import InputFileDetail from './input-file-detail';
import InputFileUpdate from './input-file-update';
import InputFileDeleteDialog from './input-file-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={InputFileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={InputFileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={InputFileDetail} />
      <ErrorBoundaryRoute path={match.url} component={InputFile} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={InputFileDeleteDialog} />
  </>
);

export default Routes;
