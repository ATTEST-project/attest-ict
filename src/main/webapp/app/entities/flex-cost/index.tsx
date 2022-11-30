import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FlexCost from './flex-cost';
import FlexCostDetail from './flex-cost-detail';
import FlexCostUpdate from './flex-cost-update';
import FlexCostDeleteDialog from './flex-cost-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FlexCostUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FlexCostUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FlexCostDetail} />
      <ErrorBoundaryRoute path={match.url} component={FlexCost} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FlexCostDeleteDialog} />
  </>
);

export default Routes;
