import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './weather-forecast.reducer';
import { IWeatherForecast } from 'app/shared/model/weather-forecast.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WeatherForecastUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const weatherForecastEntity = useAppSelector(state => state.weatherForecast.entity);
  const loading = useAppSelector(state => state.weatherForecast.loading);
  const updating = useAppSelector(state => state.weatherForecast.updating);
  const updateSuccess = useAppSelector(state => state.weatherForecast.updateSuccess);
  const handleClose = () => {
    props.history.push('/weather-forecast' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...weatherForecastEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...weatherForecastEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.weatherForecast.home.createOrEditLabel" data-cy="WeatherForecastCreateUpdateHeading">
            Create or edit a WeatherForecast
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="weather-forecast-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Solar Profile"
                id="weather-forecast-solarProfile"
                name="solarProfile"
                data-cy="solarProfile"
                type="text"
              />
              <ValidatedField label="Outside Temp" id="weather-forecast-outsideTemp" name="outsideTemp" data-cy="outsideTemp" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/weather-forecast" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default WeatherForecastUpdate;
