import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { INetwork } from 'app/shared/model/network.model';
import { getEntities as getNetworks } from 'app/entities/network/network.reducer';
import { getEntity, updateEntity, createEntity, reset } from './billing-consumption.reducer';
import { IBillingConsumption } from 'app/shared/model/billing-consumption.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BillingConsumptionUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const networks = useAppSelector(state => state.network.entities);
  const billingConsumptionEntity = useAppSelector(state => state.billingConsumption.entity);
  const loading = useAppSelector(state => state.billingConsumption.loading);
  const updating = useAppSelector(state => state.billingConsumption.updating);
  const updateSuccess = useAppSelector(state => state.billingConsumption.updateSuccess);
  const handleClose = () => {
    props.history.push('/billing-consumption' + props.location.search);
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
      ...billingConsumptionEntity,
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
          ...billingConsumptionEntity,
          network: billingConsumptionEntity?.network?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.billingConsumption.home.createOrEditLabel" data-cy="BillingConsumptionCreateUpdateHeading">
            Create or edit a BillingConsumption
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
                <ValidatedField name="id" required readOnly id="billing-consumption-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Bus Num" id="billing-consumption-busNum" name="busNum" data-cy="busNum" type="text" />
              <ValidatedField label="Type" id="billing-consumption-type" name="type" data-cy="type" type="text" />
              <ValidatedField
                label="Total Energy Consumption"
                id="billing-consumption-totalEnergyConsumption"
                name="totalEnergyConsumption"
                data-cy="totalEnergyConsumption"
                type="text"
              />
              <ValidatedField
                label="Unit Of Measure"
                id="billing-consumption-unitOfMeasure"
                name="unitOfMeasure"
                data-cy="unitOfMeasure"
                type="text"
              />
              <ValidatedField id="billing-consumption-network" name="network" data-cy="network" label="Network" type="select">
                <option value="" key="0" />
                {networks
                  ? networks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/billing-consumption" replace color="info">
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

export default BillingConsumptionUpdate;
