import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AssetTransformer from './asset-transformer';
import AssetTransformerDetail from './asset-transformer-detail';
import AssetTransformerUpdate from './asset-transformer-update';
import AssetTransformerDeleteDialog from './asset-transformer-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AssetTransformerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AssetTransformerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AssetTransformerDetail} />
      <ErrorBoundaryRoute path={match.url} component={AssetTransformer} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AssetTransformerDeleteDialog} />
  </>
);

export default Routes;
