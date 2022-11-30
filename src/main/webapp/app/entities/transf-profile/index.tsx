import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TransfProfile from './transf-profile';
import TransfProfileDetail from './transf-profile-detail';
import TransfProfileUpdate from './transf-profile-update';
import TransfProfileDeleteDialog from './transf-profile-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TransfProfileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TransfProfileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TransfProfileDetail} />
      <ErrorBoundaryRoute path={match.url} component={TransfProfile} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TransfProfileDeleteDialog} />
  </>
);

export default Routes;
