import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Storage from './storage';
import StorageDetail from './storage-detail';
import StorageUpdate from './storage-update';
import StorageDeleteDialog from './storage-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={StorageUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={StorageUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={StorageDetail} />
      <ErrorBoundaryRoute path={match.url} component={Storage} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={StorageDeleteDialog} />
  </>
);

export default Routes;
