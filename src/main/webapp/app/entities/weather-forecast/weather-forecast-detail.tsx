import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './weather-forecast.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WeatherForecastDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const weatherForecastEntity = useAppSelector(state => state.weatherForecast.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="weatherForecastDetailsHeading">WeatherForecast</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{weatherForecastEntity.id}</dd>
          <dt>
            <span id="solarProfile">Solar Profile</span>
          </dt>
          <dd>{weatherForecastEntity.solarProfile}</dd>
          <dt>
            <span id="outsideTemp">Outside Temp</span>
          </dt>
          <dd>{weatherForecastEntity.outsideTemp}</dd>
        </dl>
        <Button tag={Link} to="/weather-forecast" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/weather-forecast/${weatherForecastEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default WeatherForecastDetail;
