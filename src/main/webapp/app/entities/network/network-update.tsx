import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText, Input } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IBaseMVA } from 'app/shared/model/base-mva.model';
import { getEntities as getBaseMvas } from 'app/entities/base-mva/base-mva.reducer';
import { IVoltageLevel } from 'app/shared/model/voltage-level.model';
import { getEntities as getVoltageLevels } from 'app/entities/voltage-level/voltage-level.reducer';
import { getEntity, updateEntity, createEntity, reset } from './network.reducer';
import { INetwork } from 'app/shared/model/network.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { useForm } from 'react-hook-form';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';

export const NetworkUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const baseMVAS = useAppSelector(state => state.baseMVA.entities);
  const voltageLevels = useAppSelector(state => state.voltageLevel.entities);
  const networkEntity = useAppSelector(state => state.network.entity);
  const loading = useAppSelector(state => state.network.loading);
  const updating = useAppSelector(state => state.network.updating);
  const updateSuccess = useAppSelector(state => state.network.updateSuccess);
  const handleClose = () => {
    props.history.push('/network' + props.location.search);
  };

  const account = useAppSelector(state => state.authentication.account);
  const isAdmin = hasAnyAuthority(account.authorities, [AUTHORITIES.ADMIN]);

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getBaseMvas({}));
    dispatch(getVoltageLevels({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.networkDate = convertDateTimeToServer(values.networkDate);
    values.creationDateTime = convertDateTimeToServer(values.creationDateTime);
    values.updateDateTime = convertDateTimeToServer(values.updateDateTime);

    const entity = {
      ...networkEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          networkDate: displayDefaultDateTime(),
          creationDateTime: displayDefaultDateTime(),
          updateDateTime: displayDefaultDateTime(),
        }
      : {
          ...networkEntity,
          networkDate: convertDateTimeFromServer(networkEntity.networkDate),
          creationDateTime: convertDateTimeFromServer(networkEntity.creationDateTime),
          updateDateTime: convertDateTimeFromServer(networkEntity.updateDateTime),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.network.home.createOrEditLabel" data-cy="NetworkCreateUpdateHeading">
            Create or edit a Network
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="network-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Name" id="network-name" name="name" data-cy="name" type="text" />
              {isAdmin && <ValidatedField label="Mpc Name" id="network-mpcName" name="mpcName" data-cy="mpcName" type="text" />}
              <ValidatedField
                label="Country"
                id="network-country"
                name="country"
                data-cy="country"
                type="select"
                validate={{ required: true }}
              >
                <option value="" hidden>
                  Select the country...
                </option>
                <option value="ES">Spain</option>
                <option value="HR">Croatia</option>
                <option value="PT">Portugal</option>
                <option value="UK">United Kingdom</option>
              </ValidatedField>
              <ValidatedField label="Type" id="network-type" name="type" data-cy="type" type="select" validate={{ required: true }}>
                <option value="" hidden>
                  Select the type...
                </option>
                <option value="DX">Distribution</option>
                <option value="TX">Transmission</option>
              </ValidatedField>
              <ValidatedField label="Description" id="network-description" name="description" data-cy="description" type="text" />
              <ValidatedField label="Is Deleted" id="network-isDeleted" name="isDeleted" data-cy="isDeleted" check type="checkbox" />
              <ValidatedField
                label="Network Date"
                id="network-networkDate"
                name="networkDate"
                data-cy="networkDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Version" id="network-version" name="version" data-cy="version" type="text" />
              <ValidatedField
                label="Creation Date Time"
                id="network-creationDateTime"
                name="creationDateTime"
                data-cy="creationDateTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Update Date Time"
                id="network-updateDateTime"
                name="updateDateTime"
                data-cy="updateDateTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/network" replace color="info">
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

export default NetworkUpdate;
