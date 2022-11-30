import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TopologyBus from './topology-bus';
import TopologyBusDetail from './topology-bus-detail';
import TopologyBusUpdate from './topology-bus-update';
import TopologyBusDeleteDialog from './topology-bus-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TopologyBusUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TopologyBusUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TopologyBusDetail} />
      <ErrorBoundaryRoute path={match.url} component={TopologyBus} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TopologyBusDeleteDialog} />
  </>
);

export default Routes;
