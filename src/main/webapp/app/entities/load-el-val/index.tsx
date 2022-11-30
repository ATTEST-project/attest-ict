import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import LoadElVal from './load-el-val';
import LoadElValDetail from './load-el-val-detail';
import LoadElValUpdate from './load-el-val-update';
import LoadElValDeleteDialog from './load-el-val-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LoadElValUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LoadElValUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LoadElValDetail} />
      <ErrorBoundaryRoute path={match.url} component={LoadElVal} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={LoadElValDeleteDialog} />
  </>
);

export default Routes;
