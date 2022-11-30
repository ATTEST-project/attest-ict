import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GenElVal from './gen-el-val';
import GenElValDetail from './gen-el-val-detail';
import GenElValUpdate from './gen-el-val-update';
import GenElValDeleteDialog from './gen-el-val-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GenElValUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GenElValUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GenElValDetail} />
      <ErrorBoundaryRoute path={match.url} component={GenElVal} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GenElValDeleteDialog} />
  </>
);

export default Routes;
