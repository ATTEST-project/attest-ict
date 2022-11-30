import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import StorageCost from './storage-cost';
import StorageCostDetail from './storage-cost-detail';
import StorageCostUpdate from './storage-cost-update';
import StorageCostDeleteDialog from './storage-cost-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={StorageCostUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={StorageCostUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={StorageCostDetail} />
      <ErrorBoundaryRoute path={match.url} component={StorageCost} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={StorageCostDeleteDialog} />
  </>
);

export default Routes;
