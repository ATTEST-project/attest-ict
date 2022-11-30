import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import WeatherForecast from './weather-forecast';
import WeatherForecastDetail from './weather-forecast-detail';
import WeatherForecastUpdate from './weather-forecast-update';
import WeatherForecastDeleteDialog from './weather-forecast-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WeatherForecastUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WeatherForecastUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WeatherForecastDetail} />
      <ErrorBoundaryRoute path={match.url} component={WeatherForecast} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={WeatherForecastDeleteDialog} />
  </>
);

export default Routes;
