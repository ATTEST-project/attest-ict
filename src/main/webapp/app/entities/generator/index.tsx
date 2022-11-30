import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Generator from './generator';
import GeneratorDetail from './generator-detail';
import GeneratorUpdate from './generator-update';
import GeneratorDeleteDialog from './generator-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GeneratorUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GeneratorUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GeneratorDetail} />
      <ErrorBoundaryRoute path={match.url} component={Generator} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GeneratorDeleteDialog} />
  </>
);

export default Routes;
