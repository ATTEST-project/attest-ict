import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import LoadProfile from './load-profile';
import LoadProfileDetail from './load-profile-detail';
import LoadProfileUpdate from './load-profile-update';
import LoadProfileDeleteDialog from './load-profile-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LoadProfileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LoadProfileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LoadProfileDetail} />
      <ErrorBoundaryRoute path={match.url} component={LoadProfile} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={LoadProfileDeleteDialog} />
  </>
);

export default Routes;
