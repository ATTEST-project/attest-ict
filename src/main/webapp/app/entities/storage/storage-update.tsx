import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { INetwork } from 'app/shared/model/network.model';
import { getEntities as getNetworks } from 'app/entities/network/network.reducer';
import { getEntity, updateEntity, createEntity, reset } from './storage.reducer';
import { IStorage } from 'app/shared/model/storage.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const StorageUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const networks = useAppSelector(state => state.network.entities);
  const storageEntity = useAppSelector(state => state.storage.entity);
  const loading = useAppSelector(state => state.storage.loading);
  const updating = useAppSelector(state => state.storage.updating);
  const updateSuccess = useAppSelector(state => state.storage.updateSuccess);
  const handleClose = () => {
    props.history.push('/storage' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getNetworks({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...storageEntity,
      ...values,
      network: networks.find(it => it.id.toString() === values.network.toString()),
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
          ...storageEntity,
          network: storageEntity?.network?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.storage.home.createOrEditLabel" data-cy="StorageCreateUpdateHeading">
            Create or edit a Storage
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="storage-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Bus Num" id="storage-busNum" name="busNum" data-cy="busNum" type="text" />
              <ValidatedField label="Ps" id="storage-ps" name="ps" data-cy="ps" type="text" />
              <ValidatedField label="Qs" id="storage-qs" name="qs" data-cy="qs" type="text" />
              <ValidatedField label="Energy" id="storage-energy" name="energy" data-cy="energy" type="text" />
              <ValidatedField label="E Rating" id="storage-eRating" name="eRating" data-cy="eRating" type="text" />
              <ValidatedField label="Charge Rating" id="storage-chargeRating" name="chargeRating" data-cy="chargeRating" type="text" />
              <ValidatedField
                label="Discharge Rating"
                id="storage-dischargeRating"
                name="dischargeRating"
                data-cy="dischargeRating"
                type="text"
              />
              <ValidatedField
                label="Charge Efficiency"
                id="storage-chargeEfficiency"
                name="chargeEfficiency"
                data-cy="chargeEfficiency"
                type="text"
              />
              <ValidatedField label="Thermal Rating" id="storage-thermalRating" name="thermalRating" data-cy="thermalRating" type="text" />
              <ValidatedField label="Qmin" id="storage-qmin" name="qmin" data-cy="qmin" type="text" />
              <ValidatedField label="Qmax" id="storage-qmax" name="qmax" data-cy="qmax" type="text" />
              <ValidatedField label="R" id="storage-r" name="r" data-cy="r" type="text" />
              <ValidatedField label="X" id="storage-x" name="x" data-cy="x" type="text" />
              <ValidatedField label="P Loss" id="storage-pLoss" name="pLoss" data-cy="pLoss" type="text" />
              <ValidatedField label="Q Loss" id="storage-qLoss" name="qLoss" data-cy="qLoss" type="text" />
              <ValidatedField label="Status" id="storage-status" name="status" data-cy="status" type="text" />
              <ValidatedField label="Soc Initial" id="storage-socInitial" name="socInitial" data-cy="socInitial" type="text" />
              <ValidatedField label="Soc Min" id="storage-socMin" name="socMin" data-cy="socMin" type="text" />
              <ValidatedField label="Soc Max" id="storage-socMax" name="socMax" data-cy="socMax" type="text" />
              <ValidatedField id="storage-network" name="network" data-cy="network" label="Network" type="select">
                <option value="" key="0" />
                {networks
                  ? networks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/storage" replace color="info">
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

export default StorageUpdate;
