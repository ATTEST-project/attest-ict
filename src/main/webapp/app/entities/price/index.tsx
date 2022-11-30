import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Price from './price';
import PriceDetail from './price-detail';
import PriceUpdate from './price-update';
import PriceDeleteDialog from './price-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PriceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PriceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PriceDetail} />
      <ErrorBoundaryRoute path={match.url} component={Price} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PriceDeleteDialog} />
  </>
);

export default Routes;
