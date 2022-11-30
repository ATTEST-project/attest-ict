import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import VoltageLevel from './voltage-level';
import VoltageLevelDetail from './voltage-level-detail';
import VoltageLevelUpdate from './voltage-level-update';
import VoltageLevelDeleteDialog from './voltage-level-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VoltageLevelUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VoltageLevelUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VoltageLevelDetail} />
      <ErrorBoundaryRoute path={match.url} component={VoltageLevel} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={VoltageLevelDeleteDialog} />
  </>
);

export default Routes;
