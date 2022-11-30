import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { INetwork } from 'app/shared/model/network.model';
import { getEntities as getNetworks } from 'app/entities/network/network.reducer';
import { getEntity, updateEntity, createEntity, reset } from './capacitor-bank-data.reducer';
import { ICapacitorBankData } from 'app/shared/model/capacitor-bank-data.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const CapacitorBankDataUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const networks = useAppSelector(state => state.network.entities);
  const capacitorBankDataEntity = useAppSelector(state => state.capacitorBankData.entity);
  const loading = useAppSelector(state => state.capacitorBankData.loading);
  const updating = useAppSelector(state => state.capacitorBankData.updating);
  const updateSuccess = useAppSelector(state => state.capacitorBankData.updateSuccess);
  const handleClose = () => {
    props.history.push('/capacitor-bank-data' + props.location.search);
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
      ...capacitorBankDataEntity,
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
          ...capacitorBankDataEntity,
          network: capacitorBankDataEntity?.network?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.capacitorBankData.home.createOrEditLabel" data-cy="CapacitorBankDataCreateUpdateHeading">
            Create or edit a CapacitorBankData
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
                <ValidatedField name="id" required readOnly id="capacitor-bank-data-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Bus Num" id="capacitor-bank-data-busNum" name="busNum" data-cy="busNum" type="text" />
              <ValidatedField label="Node Id" id="capacitor-bank-data-nodeId" name="nodeId" data-cy="nodeId" type="text" />
              <ValidatedField label="Bank Id" id="capacitor-bank-data-bankId" name="bankId" data-cy="bankId" type="text" />
              <ValidatedField label="Qnom" id="capacitor-bank-data-qnom" name="qnom" data-cy="qnom" type="text" />
              <ValidatedField id="capacitor-bank-data-network" name="network" data-cy="network" label="Network" type="select">
                <option value="" key="0" />
                {networks
                  ? networks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/capacitor-bank-data" replace color="info">
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

export default CapacitorBankDataUpdate;
