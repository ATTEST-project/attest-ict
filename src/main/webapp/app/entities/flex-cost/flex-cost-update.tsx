import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IFlexProfile } from 'app/shared/model/flex-profile.model';
import { getEntities as getFlexProfiles } from 'app/entities/flex-profile/flex-profile.reducer';
import { getEntity, updateEntity, createEntity, reset } from './flex-cost.reducer';
import { IFlexCost } from 'app/shared/model/flex-cost.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const FlexCostUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const flexProfiles = useAppSelector(state => state.flexProfile.entities);
  const flexCostEntity = useAppSelector(state => state.flexCost.entity);
  const loading = useAppSelector(state => state.flexCost.loading);
  const updating = useAppSelector(state => state.flexCost.updating);
  const updateSuccess = useAppSelector(state => state.flexCost.updateSuccess);
  const handleClose = () => {
    props.history.push('/flex-cost' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getFlexProfiles({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...flexCostEntity,
      ...values,
      flexProfile: flexProfiles.find(it => it.id.toString() === values.flexProfile.toString()),
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
          ...flexCostEntity,
          flexProfile: flexCostEntity?.flexProfile?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.flexCost.home.createOrEditLabel" data-cy="FlexCostCreateUpdateHeading">
            Create or edit a FlexCost
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="flex-cost-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Bus Num" id="flex-cost-busNum" name="busNum" data-cy="busNum" type="text" />
              <ValidatedField label="Model" id="flex-cost-model" name="model" data-cy="model" type="text" />
              <ValidatedField label="N Cost" id="flex-cost-nCost" name="nCost" data-cy="nCost" type="text" />
              <ValidatedField label="Cost Pr" id="flex-cost-costPr" name="costPr" data-cy="costPr" type="text" />
              <ValidatedField label="Cost Qr" id="flex-cost-costQr" name="costQr" data-cy="costQr" type="text" />
              <ValidatedField label="Cost Pf" id="flex-cost-costPf" name="costPf" data-cy="costPf" type="text" />
              <ValidatedField label="Cost Qf" id="flex-cost-costQf" name="costQf" data-cy="costQf" type="text" />
              <ValidatedField id="flex-cost-flexProfile" name="flexProfile" data-cy="flexProfile" label="Flex Profile" type="select">
                <option value="" key="0" />
                {flexProfiles
                  ? flexProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/flex-cost" replace color="info">
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

export default FlexCostUpdate;
