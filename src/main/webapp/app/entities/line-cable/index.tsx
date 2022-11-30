import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import LineCable from './line-cable';
import LineCableDetail from './line-cable-detail';
import LineCableUpdate from './line-cable-update';
import LineCableDeleteDialog from './line-cable-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LineCableUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LineCableUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LineCableDetail} />
      <ErrorBoundaryRoute path={match.url} component={LineCable} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={LineCableDeleteDialog} />
  </>
);

export default Routes;
