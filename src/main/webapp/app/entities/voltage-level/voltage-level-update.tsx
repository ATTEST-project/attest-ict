import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { INetwork } from 'app/shared/model/network.model';
import { getEntities as getNetworks } from 'app/entities/network/network.reducer';
import { getEntity, updateEntity, createEntity, reset } from './voltage-level.reducer';
import { IVoltageLevel } from 'app/shared/model/voltage-level.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const VoltageLevelUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const networks = useAppSelector(state => state.network.entities);
  const voltageLevelEntity = useAppSelector(state => state.voltageLevel.entity);
  const loading = useAppSelector(state => state.voltageLevel.loading);
  const updating = useAppSelector(state => state.voltageLevel.updating);
  const updateSuccess = useAppSelector(state => state.voltageLevel.updateSuccess);
  const handleClose = () => {
    props.history.push('/voltage-level' + props.location.search);
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
      ...voltageLevelEntity,
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
          ...voltageLevelEntity,
          network: voltageLevelEntity?.network?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.voltageLevel.home.createOrEditLabel" data-cy="VoltageLevelCreateUpdateHeading">
            Create or edit a VoltageLevel
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
                <ValidatedField name="id" required readOnly id="voltage-level-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="V 1" id="voltage-level-v1" name="v1" data-cy="v1" type="text" />
              <ValidatedField label="V 2" id="voltage-level-v2" name="v2" data-cy="v2" type="text" />
              <ValidatedField label="V 3" id="voltage-level-v3" name="v3" data-cy="v3" type="text" />
              <ValidatedField id="voltage-level-network" name="network" data-cy="network" label="Network" type="select">
                <option value="" key="0" />
                {networks
                  ? networks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/voltage-level" replace color="info">
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

export default VoltageLevelUpdate;
