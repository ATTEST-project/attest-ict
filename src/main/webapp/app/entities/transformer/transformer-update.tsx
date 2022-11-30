import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { INetwork } from 'app/shared/model/network.model';
import { getEntities as getNetworks } from 'app/entities/network/network.reducer';
import { getEntity, updateEntity, createEntity, reset } from './transformer.reducer';
import { ITransformer } from 'app/shared/model/transformer.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TransformerUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const networks = useAppSelector(state => state.network.entities);
  const transformerEntity = useAppSelector(state => state.transformer.entity);
  const loading = useAppSelector(state => state.transformer.loading);
  const updating = useAppSelector(state => state.transformer.updating);
  const updateSuccess = useAppSelector(state => state.transformer.updateSuccess);
  const handleClose = () => {
    props.history.push('/transformer' + props.location.search);
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
      ...transformerEntity,
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
          ...transformerEntity,
          network: transformerEntity?.network?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.transformer.home.createOrEditLabel" data-cy="TransformerCreateUpdateHeading">
            Create or edit a Transformer
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="transformer-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Fbus" id="transformer-fbus" name="fbus" data-cy="fbus" type="text" />
              <ValidatedField label="Tbus" id="transformer-tbus" name="tbus" data-cy="tbus" type="text" />
              <ValidatedField label="Min" id="transformer-min" name="min" data-cy="min" type="text" />
              <ValidatedField label="Max" id="transformer-max" name="max" data-cy="max" type="text" />
              <ValidatedField label="Total Taps" id="transformer-totalTaps" name="totalTaps" data-cy="totalTaps" type="text" />
              <ValidatedField label="Tap" id="transformer-tap" name="tap" data-cy="tap" type="text" />
              <ValidatedField
                label="Manufacture Year"
                id="transformer-manufactureYear"
                name="manufactureYear"
                data-cy="manufactureYear"
                type="text"
              />
              <ValidatedField
                label="Commissioning Year"
                id="transformer-commissioningYear"
                name="commissioningYear"
                data-cy="commissioningYear"
                type="text"
              />
              <ValidatedField id="transformer-network" name="network" data-cy="network" label="Network" type="select">
                <option value="" key="0" />
                {networks
                  ? networks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/transformer" replace color="info">
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

export default TransformerUpdate;
