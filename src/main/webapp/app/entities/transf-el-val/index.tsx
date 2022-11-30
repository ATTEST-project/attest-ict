import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TransfElVal from './transf-el-val';
import TransfElValDetail from './transf-el-val-detail';
import TransfElValUpdate from './transf-el-val-update';
import TransfElValDeleteDialog from './transf-el-val-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TransfElValUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TransfElValUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TransfElValDetail} />
      <ErrorBoundaryRoute path={match.url} component={TransfElVal} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TransfElValDeleteDialog} />
  </>
);

export default Routes;
