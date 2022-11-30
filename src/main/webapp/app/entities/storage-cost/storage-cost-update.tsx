import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './storage-cost.reducer';
import { IStorageCost } from 'app/shared/model/storage-cost.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const StorageCostUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const storageCostEntity = useAppSelector(state => state.storageCost.entity);
  const loading = useAppSelector(state => state.storageCost.loading);
  const updating = useAppSelector(state => state.storageCost.updating);
  const updateSuccess = useAppSelector(state => state.storageCost.updateSuccess);
  const handleClose = () => {
    props.history.push('/storage-cost' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...storageCostEntity,
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
      ? {}
      : {
          ...storageCostEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.storageCost.home.createOrEditLabel" data-cy="StorageCostCreateUpdateHeading">
            Create or edit a StorageCost
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="storage-cost-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Bus Num" id="storage-cost-busNum" name="busNum" data-cy="busNum" type="text" />
              <ValidatedField label="Cost A" id="storage-cost-costA" name="costA" data-cy="costA" type="text" />
              <ValidatedField label="Cost B" id="storage-cost-costB" name="costB" data-cy="costB" type="text" />
              <ValidatedField label="Cost C" id="storage-cost-costC" name="costC" data-cy="costC" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/storage-cost" replace color="info">
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

export default StorageCostUpdate;
