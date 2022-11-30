import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Topology from './topology';
import TopologyDetail from './topology-detail';
import TopologyUpdate from './topology-update';
import TopologyDeleteDialog from './topology-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TopologyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TopologyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TopologyDetail} />
      <ErrorBoundaryRoute path={match.url} component={Topology} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TopologyDeleteDialog} />
  </>
);

export default Routes;
