import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GenProfile from './gen-profile';
import GenProfileDetail from './gen-profile-detail';
import GenProfileUpdate from './gen-profile-update';
import GenProfileDeleteDialog from './gen-profile-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GenProfileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GenProfileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GenProfileDetail} />
      <ErrorBoundaryRoute path={match.url} component={GenProfile} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GenProfileDeleteDialog} />
  </>
);

export default Routes;
