import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AssetUGCable from './asset-ug-cable';
import AssetUGCableDetail from './asset-ug-cable-detail';
import AssetUGCableUpdate from './asset-ug-cable-update';
import AssetUGCableDeleteDialog from './asset-ug-cable-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AssetUGCableUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AssetUGCableUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AssetUGCableDetail} />
      <ErrorBoundaryRoute path={match.url} component={AssetUGCable} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AssetUGCableDeleteDialog} />
  </>
);

export default Routes;
