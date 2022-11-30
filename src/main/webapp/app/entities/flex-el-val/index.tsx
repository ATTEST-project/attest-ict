import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FlexElVal from './flex-el-val';
import FlexElValDetail from './flex-el-val-detail';
import FlexElValUpdate from './flex-el-val-update';
import FlexElValDeleteDialog from './flex-el-val-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FlexElValUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FlexElValUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FlexElValDetail} />
      <ErrorBoundaryRoute path={match.url} component={FlexElVal} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FlexElValDeleteDialog} />
  </>
);

export default Routes;
