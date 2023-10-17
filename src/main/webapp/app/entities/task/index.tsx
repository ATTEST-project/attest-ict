import React from 'react';
import { Switch } from 'react-router-dom';
import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

import Task from './task';
import TaskDetail from './task-detail';
import TaskUpdate from './task-update';
import TaskDeleteDialog from './task-delete-dialog';
import { TaskLogFileDlDialog } from 'app/entities/task/task-logfiledl-dialog';
import TaskResultsDlDialog from 'app/entities/task/task-resultsdl-dialog';
import TaskResults from 'app/entities/task/task-results';
import TaskKillDialog from 'app/entities/task/task-kill-dialog';

import { AUTHORITIES } from 'app/config/constants';

const Routes = ({ match }) => (
  <>
    <Switch>
      <PrivateRoute exact path={`${match.url}/new`} component={TaskUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TaskUpdate} />

      <PrivateRoute exact path={`${match.url}/:id/edit`} component={TaskUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TaskUpdate} />

      <PrivateRoute exact path={`${match.url}/:id`} component={TaskDetail} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TaskDetail} />

      <ErrorBoundaryRoute path={`${match.url}/:id/results`} component={TaskResults} />
      <ErrorBoundaryRoute path={match.url} component={Task} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/task-log-dl`} component={TaskLogFileDlDialog} />
    <ErrorBoundaryRoute exact path={`${match.url}/:id/task-results-dl`} component={TaskResultsDlDialog} />
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TaskDeleteDialog} />
    <ErrorBoundaryRoute exact path={`${match.url}/:id/stop`} component={TaskKillDialog} />
  </>
);

export default Routes;
