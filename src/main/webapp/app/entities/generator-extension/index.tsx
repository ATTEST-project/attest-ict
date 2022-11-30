import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GeneratorExtension from './generator-extension';
import GeneratorExtensionDetail from './generator-extension-detail';
import GeneratorExtensionUpdate from './generator-extension-update';
import GeneratorExtensionDeleteDialog from './generator-extension-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GeneratorExtensionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GeneratorExtensionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GeneratorExtensionDetail} />
      <ErrorBoundaryRoute path={match.url} component={GeneratorExtension} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GeneratorExtensionDeleteDialog} />
  </>
);

export default Routes;
