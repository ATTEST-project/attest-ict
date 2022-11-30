import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IBus } from 'app/shared/model/bus.model';
import { getEntities as getBuses } from 'app/entities/bus/bus.reducer';
import { getEntity, updateEntity, createEntity, reset } from './bus-name.reducer';
import { IBusName } from 'app/shared/model/bus-name.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BusNameUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const buses = useAppSelector(state => state.bus.entities);
  const busNameEntity = useAppSelector(state => state.busName.entity);
  const loading = useAppSelector(state => state.busName.loading);
  const updating = useAppSelector(state => state.busName.updating);
  const updateSuccess = useAppSelector(state => state.busName.updateSuccess);
  const handleClose = () => {
    props.history.push('/bus-name' + props.location.search);
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
      ...busNameEntity,
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
          ...busNameEntity,
          bus: busNameEntity?.bus?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.busName.home.createOrEditLabel" data-cy="BusNameCreateUpdateHeading">
            Create or edit a BusName
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="bus-name-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Bus Name" id="bus-name-busName" name="busName" data-cy="busName" type="text" />
              <ValidatedField id="bus-name-bus" name="bus" data-cy="bus" label="Bus" type="select">
                <option value="" key="0" />
                {buses
                  ? buses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.busNum}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/bus-name" replace color="info">
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

export default BusNameUpdate;
