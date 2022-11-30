import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { INetwork } from 'app/shared/model/network.model';
import { getEntities as getNetworks } from 'app/entities/network/network.reducer';
import { getEntity, updateEntity, createEntity, reset } from './asset-ug-cable.reducer';
import { IAssetUGCable } from 'app/shared/model/asset-ug-cable.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AssetUGCableUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const networks = useAppSelector(state => state.network.entities);
  const assetUGCableEntity = useAppSelector(state => state.assetUGCable.entity);
  const loading = useAppSelector(state => state.assetUGCable.loading);
  const updating = useAppSelector(state => state.assetUGCable.updating);
  const updateSuccess = useAppSelector(state => state.assetUGCable.updateSuccess);
  const handleClose = () => {
    props.history.push('/asset-ug-cable' + props.location.search);
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
      ...assetUGCableEntity,
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
          ...assetUGCableEntity,
          network: assetUGCableEntity?.network?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.assetUGCable.home.createOrEditLabel" data-cy="AssetUGCableCreateUpdateHeading">
            Create or edit a AssetUGCable
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
                <ValidatedField name="id" required readOnly id="asset-ug-cable-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Section Label"
                id="asset-ug-cable-sectionLabel"
                name="sectionLabel"
                data-cy="sectionLabel"
                type="text"
              />
              <ValidatedField label="Circuit Id" id="asset-ug-cable-circuitId" name="circuitId" data-cy="circuitId" type="text" />
              <ValidatedField
                label="Conductor Cross Sectional Area"
                id="asset-ug-cable-conductorCrossSectionalArea"
                name="conductorCrossSectionalArea"
                data-cy="conductorCrossSectionalArea"
                type="text"
              />
              <ValidatedField
                label="Sheath Material"
                id="asset-ug-cable-sheathMaterial"
                name="sheathMaterial"
                data-cy="sheathMaterial"
                type="text"
              />
              <ValidatedField
                label="Design Voltage"
                id="asset-ug-cable-designVoltage"
                name="designVoltage"
                data-cy="designVoltage"
                type="text"
              />
              <ValidatedField
                label="Operating Voltage"
                id="asset-ug-cable-operatingVoltage"
                name="operatingVoltage"
                data-cy="operatingVoltage"
                type="text"
              />
              <ValidatedField
                label="Insulation Type Sheath"
                id="asset-ug-cable-insulationTypeSheath"
                name="insulationTypeSheath"
                data-cy="insulationTypeSheath"
                type="text"
              />
              <ValidatedField
                label="Conductor Material"
                id="asset-ug-cable-conductorMaterial"
                name="conductorMaterial"
                data-cy="conductorMaterial"
                type="text"
              />
              <ValidatedField label="Age" id="asset-ug-cable-age" name="age" data-cy="age" type="text" />
              <ValidatedField
                label="Fault History"
                id="asset-ug-cable-faultHistory"
                name="faultHistory"
                data-cy="faultHistory"
                type="text"
              />
              <ValidatedField
                label="Length Of Cable Section Meters"
                id="asset-ug-cable-lengthOfCableSectionMeters"
                name="lengthOfCableSectionMeters"
                data-cy="lengthOfCableSectionMeters"
                type="text"
              />
              <ValidatedField
                label="Section Rating"
                id="asset-ug-cable-sectionRating"
                name="sectionRating"
                data-cy="sectionRating"
                type="text"
              />
              <ValidatedField label="Type" id="asset-ug-cable-type" name="type" data-cy="type" type="text" />
              <ValidatedField
                label="Number Of Cores"
                id="asset-ug-cable-numberOfCores"
                name="numberOfCores"
                data-cy="numberOfCores"
                type="text"
              />
              <ValidatedField
                label="Net Performance Cost Of Failure Euro"
                id="asset-ug-cable-netPerformanceCostOfFailureEuro"
                name="netPerformanceCostOfFailureEuro"
                data-cy="netPerformanceCostOfFailureEuro"
                type="text"
              />
              <ValidatedField
                label="Repair Time Hour"
                id="asset-ug-cable-repairTimeHour"
                name="repairTimeHour"
                data-cy="repairTimeHour"
                type="text"
              />
              <ValidatedField id="asset-ug-cable-network" name="network" data-cy="network" label="Network" type="select">
                <option value="" key="0" />
                {networks
                  ? networks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/asset-ug-cable" replace color="info">
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

export default AssetUGCableUpdate;
