import React from 'react';
import { Switch } from 'react-router-dom';

import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import { AUTHORITIES } from 'app/config/constants';

import BillingDer from './billing-der';
import BillingDerDetail from './billing-der-detail';
import BillingDerUpdate from './billing-der-update';
import BillingDerDeleteDialog from './billing-der-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <PrivateRoute exact path={`${match.url}/new`} component={BillingDerUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BillingDerUpdate} />

      <PrivateRoute exact path={`${match.url}/:id/edit`} component={BillingDerUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BillingDerUpdate} />

      <PrivateRoute exact path={`${match.url}/:id`} component={BillingDerDetail} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BillingDerDetail} />
      <ErrorBoundaryRoute path={match.url} component={BillingDer} />
    </Switch>
    <PrivateRoute exact path={`${match.url}/:id/delete`} component={BillingDerDeleteDialog} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BillingDerDeleteDialog} />
  </>
);

export default Routes;
