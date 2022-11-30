import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IBus } from 'app/shared/model/bus.model';
import { getEntities as getBuses } from 'app/entities/bus/bus.reducer';
import { getEntity, updateEntity, createEntity, reset } from './bus-coordinate.reducer';
import { IBusCoordinate } from 'app/shared/model/bus-coordinate.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BusCoordinateUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const buses = useAppSelector(state => state.bus.entities);
  const busCoordinateEntity = useAppSelector(state => state.busCoordinate.entity);
  const loading = useAppSelector(state => state.busCoordinate.loading);
  const updating = useAppSelector(state => state.busCoordinate.updating);
  const updateSuccess = useAppSelector(state => state.busCoordinate.updateSuccess);
  const handleClose = () => {
    props.history.push('/bus-coordinate' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getBuses({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...busCoordinateEntity,
      ...values,
      bus: buses.find(it => it.id.toString() === values.bus.toString()),
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
          ...busCoordinateEntity,
          bus: busCoordinateEntity?.bus?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.busCoordinate.home.createOrEditLabel" data-cy="BusCoordinateCreateUpdateHeading">
            Create or edit a BusCoordinate
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
                <ValidatedField name="id" required readOnly id="bus-coordinate-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="X" id="bus-coordinate-x" name="x" data-cy="x" type="text" />
              <ValidatedField label="Y" id="bus-coordinate-y" name="y" data-cy="y" type="text" />
              <ValidatedField id="bus-coordinate-bus" name="bus" data-cy="bus" label="Bus" type="select">
                <option value="" key="0" />
                {buses
                  ? buses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.busNum}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/bus-coordinate" replace color="info">
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

export default BusCoordinateUpdate;
