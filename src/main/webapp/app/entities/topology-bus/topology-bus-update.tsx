import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { INetwork } from 'app/shared/model/network.model';
import { getEntities as getNetworks } from 'app/entities/network/network.reducer';
import { getEntity, updateEntity, createEntity, reset } from './topology-bus.reducer';
import { ITopologyBus } from 'app/shared/model/topology-bus.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TopologyBusUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const networks = useAppSelector(state => state.network.entities);
  const topologyBusEntity = useAppSelector(state => state.topologyBus.entity);
  const loading = useAppSelector(state => state.topologyBus.loading);
  const updating = useAppSelector(state => state.topologyBus.updating);
  const updateSuccess = useAppSelector(state => state.topologyBus.updateSuccess);
  const handleClose = () => {
    props.history.push('/topology-bus' + props.location.search);
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
      ...topologyBusEntity,
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
          ...topologyBusEntity,
          network: topologyBusEntity?.network?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.topologyBus.home.createOrEditLabel" data-cy="TopologyBusCreateUpdateHeading">
            Create or edit a TopologyBus
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="topology-bus-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Power Line Branch"
                id="topology-bus-powerLineBranch"
                name="powerLineBranch"
                data-cy="powerLineBranch"
                type="text"
              />
              <ValidatedField label="Bus Name 1" id="topology-bus-busName1" name="busName1" data-cy="busName1" type="text" />
              <ValidatedField label="Bus Name 2" id="topology-bus-busName2" name="busName2" data-cy="busName2" type="text" />
              <ValidatedField id="topology-bus-network" name="network" data-cy="network" label="Network" type="select">
                <option value="" key="0" />
                {networks
                  ? networks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/topology-bus" replace color="info">
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

export default TopologyBusUpdate;
