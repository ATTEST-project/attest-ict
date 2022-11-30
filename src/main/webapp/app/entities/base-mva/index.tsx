import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import BaseMVA from './base-mva';
import BaseMVADetail from './base-mva-detail';
import BaseMVAUpdate from './base-mva-update';
import BaseMVADeleteDialog from './base-mva-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BaseMVAUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BaseMVAUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BaseMVADetail} />
      <ErrorBoundaryRoute path={match.url} component={BaseMVA} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BaseMVADeleteDialog} />
  </>
);

export default Routes;
