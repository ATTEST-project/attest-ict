import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IBus } from 'app/shared/model/bus.model';
import { getEntities as getBuses } from 'app/entities/bus/bus.reducer';
import { getEntity, updateEntity, createEntity, reset } from './bus-extension.reducer';
import { IBusExtension } from 'app/shared/model/bus-extension.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BusExtensionUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const buses = useAppSelector(state => state.bus.entities);
  const busExtensionEntity = useAppSelector(state => state.busExtension.entity);
  const loading = useAppSelector(state => state.busExtension.loading);
  const updating = useAppSelector(state => state.busExtension.updating);
  const updateSuccess = useAppSelector(state => state.busExtension.updateSuccess);
  const handleClose = () => {
    props.history.push('/bus-extension' + props.location.search);
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
      ...busExtensionEntity,
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
          ...busExtensionEntity,
          bus: busExtensionEntity?.bus?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.busExtension.home.createOrEditLabel" data-cy="BusExtensionCreateUpdateHeading">
            Create or edit a BusExtension
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
                <ValidatedField name="id" required readOnly id="bus-extension-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Has Gen" id="bus-extension-hasGen" name="hasGen" data-cy="hasGen" type="text" />
              <ValidatedField label="Is Load" id="bus-extension-isLoad" name="isLoad" data-cy="isLoad" type="text" />
              <ValidatedField label="Snom Mva" id="bus-extension-snomMva" name="snomMva" data-cy="snomMva" type="text" />
              <ValidatedField label="Sx" id="bus-extension-sx" name="sx" data-cy="sx" type="text" />
              <ValidatedField label="Sy" id="bus-extension-sy" name="sy" data-cy="sy" type="text" />
              <ValidatedField label="Gx" id="bus-extension-gx" name="gx" data-cy="gx" type="text" />
              <ValidatedField label="Gy" id="bus-extension-gy" name="gy" data-cy="gy" type="text" />
              <ValidatedField label="Status" id="bus-extension-status" name="status" data-cy="status" type="text" />
              <ValidatedField
                label="Increment Cost"
                id="bus-extension-incrementCost"
                name="incrementCost"
                data-cy="incrementCost"
                type="text"
              />
              <ValidatedField
                label="Decrement Cost"
                id="bus-extension-decrementCost"
                name="decrementCost"
                data-cy="decrementCost"
                type="text"
              />
              <ValidatedField label="M Rid" id="bus-extension-mRid" name="mRid" data-cy="mRid" type="text" />
              <ValidatedField id="bus-extension-bus" name="bus" data-cy="bus" label="Bus" type="select">
                <option value="" key="0" />
                {buses
                  ? buses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.busNum}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/bus-extension" replace color="info">
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

export default BusExtensionUpdate;
