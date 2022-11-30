import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { INetwork } from 'app/shared/model/network.model';
import { getEntities as getNetworks } from 'app/entities/network/network.reducer';
import { getEntity, updateEntity, createEntity, reset } from './dso-tso-connection.reducer';
import { IDsoTsoConnection } from 'app/shared/model/dso-tso-connection.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const DsoTsoConnectionUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const networks = useAppSelector(state => state.network.entities);
  const dsoTsoConnectionEntity = useAppSelector(state => state.dsoTsoConnection.entity);
  const loading = useAppSelector(state => state.dsoTsoConnection.loading);
  const updating = useAppSelector(state => state.dsoTsoConnection.updating);
  const updateSuccess = useAppSelector(state => state.dsoTsoConnection.updateSuccess);
  const handleClose = () => {
    props.history.push('/dso-tso-connection' + props.location.search);
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
      ...dsoTsoConnectionEntity,
      ...values,
      dsoNetwork: networks.find(it => it.id.toString() === values.dsoNetwork.toString()),
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
          ...dsoTsoConnectionEntity,
          dsoNetwork: dsoTsoConnectionEntity?.dsoNetwork?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.dsoTsoConnection.home.createOrEditLabel" data-cy="DsoTsoConnectionCreateUpdateHeading">
            Create or edit a DsoTsoConnection
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
                <ValidatedField name="id" required readOnly id="dso-tso-connection-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Tso Network Name"
                id="dso-tso-connection-tsoNetworkName"
                name="tsoNetworkName"
                data-cy="tsoNetworkName"
                type="text"
              />
              <ValidatedField label="Dso Bus Num" id="dso-tso-connection-dsoBusNum" name="dsoBusNum" data-cy="dsoBusNum" type="text" />
              <ValidatedField label="Tso Bus Num" id="dso-tso-connection-tsoBusNum" name="tsoBusNum" data-cy="tsoBusNum" type="text" />
              <ValidatedField id="dso-tso-connection-dsoNetwork" name="dsoNetwork" data-cy="dsoNetwork" label="Dso Network" type="select">
                <option value="" key="0" />
                {networks
                  ? networks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/dso-tso-connection" replace color="info">
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

export default DsoTsoConnectionUpdate;
