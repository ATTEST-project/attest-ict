import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './price.reducer';
import { IPrice } from 'app/shared/model/price.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const PriceUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const priceEntity = useAppSelector(state => state.price.entity);
  const loading = useAppSelector(state => state.price.loading);
  const updating = useAppSelector(state => state.price.updating);
  const updateSuccess = useAppSelector(state => state.price.updateSuccess);
  const handleClose = () => {
    props.history.push('/price' + props.location.search);
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
      ...priceEntity,
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
          ...priceEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.price.home.createOrEditLabel" data-cy="PriceCreateUpdateHeading">
            Create or edit a Price
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="price-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Electricity Energy"
                id="price-electricityEnergy"
                name="electricityEnergy"
                data-cy="electricityEnergy"
                type="text"
              />
              <ValidatedField label="Gas Energy" id="price-gasEnergy" name="gasEnergy" data-cy="gasEnergy" type="text" />
              <ValidatedField label="Secondary Band" id="price-secondaryBand" name="secondaryBand" data-cy="secondaryBand" type="text" />
              <ValidatedField label="Secondary Up" id="price-secondaryUp" name="secondaryUp" data-cy="secondaryUp" type="text" />
              <ValidatedField label="Secondary Down" id="price-secondaryDown" name="secondaryDown" data-cy="secondaryDown" type="text" />
              <ValidatedField
                label="Secondary Ratio Up"
                id="price-secondaryRatioUp"
                name="secondaryRatioUp"
                data-cy="secondaryRatioUp"
                type="text"
              />
              <ValidatedField
                label="Secondary Ratio Down"
                id="price-secondaryRatioDown"
                name="secondaryRatioDown"
                data-cy="secondaryRatioDown"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/price" replace color="info">
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

export default PriceUpdate;
