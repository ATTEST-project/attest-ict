import React from 'react';
import { Switch } from 'react-router-dom';

import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import { AUTHORITIES } from 'app/config/constants';

import BillingConsumption from './billing-consumption';
import BillingConsumptionDetail from './billing-consumption-detail';
import BillingConsumptionUpdate from './billing-consumption-update';
import BillingConsumptionDeleteDialog from './billing-consumption-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <PrivateRoute exact path={`${match.url}/new`} component={BillingConsumptionUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BillingConsumptionUpdate} />

      <PrivateRoute exact path={`${match.url}/:id/edit`} component={BillingConsumptionUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BillingConsumptionUpdate} />

      <PrivateRoute exact path={`${match.url}/:id`} component={BillingConsumptionDetail} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BillingConsumptionDetail} />

      <ErrorBoundaryRoute path={match.url} component={BillingConsumption} />
    </Switch>
    <PrivateRoute
      exact
      path={`${match.url}/:id/delete`}
      component={BillingConsumptionDeleteDialog}
      hasAnyAuthorities={[AUTHORITIES.ADMIN]}
    />
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BillingConsumptionDeleteDialog} />
  </>
);

export default Routes;
