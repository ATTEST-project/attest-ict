import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CapacitorBankData from './capacitor-bank-data';
import CapacitorBankDataDetail from './capacitor-bank-data-detail';
import CapacitorBankDataUpdate from './capacitor-bank-data-update';
import CapacitorBankDataDeleteDialog from './capacitor-bank-data-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CapacitorBankDataUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CapacitorBankDataUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CapacitorBankDataDetail} />
      <ErrorBoundaryRoute path={match.url} component={CapacitorBankData} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CapacitorBankDataDeleteDialog} />
  </>
);

export default Routes;
