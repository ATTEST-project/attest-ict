import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BusExtension from './bus-extension';
import BusExtensionDetail from './bus-extension-detail';
import BusExtensionUpdate from './bus-extension-update';
import BusExtensionDeleteDialog from './bus-extension-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BusExtensionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BusExtensionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BusExtensionDetail} />
      <ErrorBoundaryRoute path={match.url} component={BusExtension} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BusExtensionDeleteDialog} />
  </>
);

export default Routes;
