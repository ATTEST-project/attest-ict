import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GenTag from './gen-tag';
import GenTagDetail from './gen-tag-detail';
import GenTagUpdate from './gen-tag-update';
import GenTagDeleteDialog from './gen-tag-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GenTagUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GenTagUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GenTagDetail} />
      <ErrorBoundaryRoute path={match.url} component={GenTag} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GenTagDeleteDialog} />
  </>
);

export default Routes;
