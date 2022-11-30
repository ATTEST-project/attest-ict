import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IBusName } from 'app/shared/model/bus-name.model';
import { getEntities as getBusNames } from 'app/entities/bus-name/bus-name.reducer';
import { IBusExtension } from 'app/shared/model/bus-extension.model';
import { getEntities as getBusExtensions } from 'app/entities/bus-extension/bus-extension.reducer';
import { IBusCoordinate } from 'app/shared/model/bus-coordinate.model';
import { getEntities as getBusCoordinates } from 'app/entities/bus-coordinate/bus-coordinate.reducer';
import { INetwork } from 'app/shared/model/network.model';
import { getEntities as getNetworks } from 'app/entities/network/network.reducer';
import { getEntity, updateEntity, createEntity, reset } from './bus.reducer';
import { IBus } from 'app/shared/model/bus.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { displayButton } from 'app/shared/reducers/back-button-display';

export const BusUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const busUrl = props.match.url.replace(/bus(\/.+)$/, 'bus');

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const busNames = useAppSelector(state => state.busName.entities);
  const busExtensions = useAppSelector(state => state.busExtension.entities);
  const busCoordinates = useAppSelector(state => state.busCoordinate.entities);
  const networks = useAppSelector(state => state.network.entities);
  const busEntity = useAppSelector(state => state.bus.entity);
  const loading = useAppSelector(state => state.bus.loading);
  const updating = useAppSelector(state => state.bus.updating);
  const updateSuccess = useAppSelector(state => state.bus.updateSuccess);
  const handleClose = () => {
    props.history.push(busUrl + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getBusNames({}));
    dispatch(getBusExtensions({}));
    dispatch(getBusCoordinates({}));
    dispatch(getNetworks({}));
    dispatch(displayButton(false));
    return () => {
      dispatch(displayButton(true));
    };
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...busEntity,
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
          ...busEntity,
          network: busEntity?.network?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.bus.home.createOrEditLabel" data-cy="BusCreateUpdateHeading">
            Create or edit a Bus
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="bus-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Bus Num" id="bus-busNum" name="busNum" data-cy="busNum" type="text" />
              <ValidatedField label="Type" id="bus-type" name="type" data-cy="type" type="text" />
              <ValidatedField label="Active Power" id="bus-activePower" name="activePower" data-cy="activePower" type="text" />
              <ValidatedField label="Reactive Power" id="bus-reactivePower" name="reactivePower" data-cy="reactivePower" type="text" />
              <ValidatedField label="Conductance" id="bus-conductance" name="conductance" data-cy="conductance" type="text" />
              <ValidatedField label="Susceptance" id="bus-susceptance" name="susceptance" data-cy="susceptance" type="text" />
              <ValidatedField label="Area" id="bus-area" name="area" data-cy="area" type="text" />
              <ValidatedField label="Vm" id="bus-vm" name="vm" data-cy="vm" type="text" />
              <ValidatedField label="Va" id="bus-va" name="va" data-cy="va" type="text" />
              <ValidatedField label="Base Kv" id="bus-baseKv" name="baseKv" data-cy="baseKv" type="text" />
              <ValidatedField label="Zone" id="bus-zone" name="zone" data-cy="zone" type="text" />
              <ValidatedField label="Vmax" id="bus-vmax" name="vmax" data-cy="vmax" type="text" />
              <ValidatedField label="Vmin" id="bus-vmin" name="vmin" data-cy="vmin" type="text" />
              <ValidatedField id="bus-network" name="network" data-cy="network" label="Network" type="select">
                <option value="" key="0" />
                {networks
                  ? networks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to={busUrl} replace color="info">
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

export default BusUpdate;
