import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FlexProfile from './flex-profile';
import FlexProfileDetail from './flex-profile-detail';
import FlexProfileUpdate from './flex-profile-update';
import FlexProfileDeleteDialog from './flex-profile-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FlexProfileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FlexProfileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FlexProfileDetail} />
      <ErrorBoundaryRoute path={match.url} component={FlexProfile} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FlexProfileDeleteDialog} />
  </>
);

export default Routes;
