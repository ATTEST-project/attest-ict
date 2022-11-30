import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { INetwork } from 'app/shared/model/network.model';
import { getEntities as getNetworks } from 'app/entities/network/network.reducer';
import { getEntity, updateEntity, createEntity, reset } from './asset-transformer.reducer';
import { IAssetTransformer } from 'app/shared/model/asset-transformer.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AssetTransformerUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const networks = useAppSelector(state => state.network.entities);
  const assetTransformerEntity = useAppSelector(state => state.assetTransformer.entity);
  const loading = useAppSelector(state => state.assetTransformer.loading);
  const updating = useAppSelector(state => state.assetTransformer.updating);
  const updateSuccess = useAppSelector(state => state.assetTransformer.updateSuccess);
  const handleClose = () => {
    props.history.push('/asset-transformer' + props.location.search);
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
      ...assetTransformerEntity,
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
          ...assetTransformerEntity,
          network: assetTransformerEntity?.network?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.assetTransformer.home.createOrEditLabel" data-cy="AssetTransformerCreateUpdateHeading">
            Create or edit a AssetTransformer
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
                <ValidatedField name="id" required readOnly id="asset-transformer-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Bus Num" id="asset-transformer-busNum" name="busNum" data-cy="busNum" type="text" />
              <ValidatedField
                label="Voltage Ratio"
                id="asset-transformer-voltageRatio"
                name="voltageRatio"
                data-cy="voltageRatio"
                type="text"
              />
              <ValidatedField
                label="Insulation Medium"
                id="asset-transformer-insulationMedium"
                name="insulationMedium"
                data-cy="insulationMedium"
                type="text"
              />
              <ValidatedField label="Type" id="asset-transformer-type" name="type" data-cy="type" type="text" />
              <ValidatedField
                label="Indoor Outdoor"
                id="asset-transformer-indoorOutdoor"
                name="indoorOutdoor"
                data-cy="indoorOutdoor"
                type="text"
              />
              <ValidatedField
                label="Annual Max Load Kva"
                id="asset-transformer-annualMaxLoadKva"
                name="annualMaxLoadKva"
                data-cy="annualMaxLoadKva"
                type="text"
              />
              <ValidatedField label="Age" id="asset-transformer-age" name="age" data-cy="age" type="text" />
              <ValidatedField
                label="External Condition"
                id="asset-transformer-externalCondition"
                name="externalCondition"
                data-cy="externalCondition"
                type="text"
              />
              <ValidatedField label="Rating Kva" id="asset-transformer-ratingKva" name="ratingKva" data-cy="ratingKva" type="text" />
              <ValidatedField
                label="Num Connected Customers"
                id="asset-transformer-numConnectedCustomers"
                name="numConnectedCustomers"
                data-cy="numConnectedCustomers"
                type="text"
              />
              <ValidatedField
                label="Num Sensitive Customers"
                id="asset-transformer-numSensitiveCustomers"
                name="numSensitiveCustomers"
                data-cy="numSensitiveCustomers"
                type="text"
              />
              <ValidatedField
                label="Backup Supply"
                id="asset-transformer-backupSupply"
                name="backupSupply"
                data-cy="backupSupply"
                type="text"
              />
              <ValidatedField
                label="Cost Of Failure Euro"
                id="asset-transformer-costOfFailureEuro"
                name="costOfFailureEuro"
                data-cy="costOfFailureEuro"
                type="text"
              />
              <ValidatedField id="asset-transformer-network" name="network" data-cy="network" label="Network" type="select">
                <option value="" key="0" />
                {networks
                  ? networks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/asset-transformer" replace color="info">
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

export default AssetTransformerUpdate;
