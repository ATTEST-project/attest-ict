import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GenCost from './gen-cost';
import GenCostDetail from './gen-cost-detail';
import GenCostUpdate from './gen-cost-update';
import GenCostDeleteDialog from './gen-cost-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GenCostUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GenCostUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GenCostDetail} />
      <ErrorBoundaryRoute path={match.url} component={GenCost} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GenCostDeleteDialog} />
  </>
);

export default Routes;
