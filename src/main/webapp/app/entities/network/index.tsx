import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Network from './network';
import NetworkDetail from './network-detail';
import NetworkUpdate from './network-update';
import NetworkDeleteDialog from './network-delete-dialog';
import NetworkUpload from 'app/modules/network/upload/network-upload';
import NetworkExportDialog from 'app/modules/network/export/network-export-dialog';
import NetworkActions from 'app/modules/network/actions/network-actions';
import NetworkData from 'app/modules/network/data/network-data';
import NetworkDiagram from 'app/modules/network/diagram/network-diagram';
import NetworkMap from 'app/modules/network/map/network-map';
import NetworkImportFromCimRepo from 'app/modules/network/cim-repo/network-import-from-cim-repo';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={NetworkUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/import-cim`} component={NetworkImportFromCimRepo} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={NetworkUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={NetworkDetail} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/upload`} component={NetworkUpload} />
      <ErrorBoundaryRoute path={`${match.url}/:id/data`} component={NetworkData} />
      <ErrorBoundaryRoute path={`${match.url}/:id/sld`} component={NetworkDiagram} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/map`} component={NetworkMap} />
      <ErrorBoundaryRoute path={match.url} component={Network} />
    </Switch>

    <ErrorBoundaryRoute exact path={`${match.url}/:id/export`} component={NetworkExportDialog} />
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={NetworkDeleteDialog} />
  </>
);

export default Routes;
