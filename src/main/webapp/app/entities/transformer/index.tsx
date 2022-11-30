import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Transformer from './transformer';
import TransformerDetail from './transformer-detail';
import TransformerUpdate from './transformer-update';
import TransformerDeleteDialog from './transformer-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TransformerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TransformerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TransformerDetail} />
      <ErrorBoundaryRoute path={match.url} component={Transformer} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TransformerDeleteDialog} />
  </>
);

export default Routes;
