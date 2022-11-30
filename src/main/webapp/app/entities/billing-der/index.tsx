import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BillingDer from './billing-der';
import BillingDerDetail from './billing-der-detail';
import BillingDerUpdate from './billing-der-update';
import BillingDerDeleteDialog from './billing-der-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BillingDerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BillingDerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BillingDerDetail} />
      <ErrorBoundaryRoute path={match.url} component={BillingDer} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BillingDerDeleteDialog} />
  </>
);

export default Routes;
