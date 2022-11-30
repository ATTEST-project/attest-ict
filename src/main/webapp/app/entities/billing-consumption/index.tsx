import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BillingConsumption from './billing-consumption';
import BillingConsumptionDetail from './billing-consumption-detail';
import BillingConsumptionUpdate from './billing-consumption-update';
import BillingConsumptionDeleteDialog from './billing-consumption-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BillingConsumptionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BillingConsumptionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BillingConsumptionDetail} />
      <ErrorBoundaryRoute path={match.url} component={BillingConsumption} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BillingConsumptionDeleteDialog} />
  </>
);

export default Routes;
