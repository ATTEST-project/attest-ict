import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IFlexProfile } from 'app/shared/model/flex-profile.model';
import { getEntities as getFlexProfiles } from 'app/entities/flex-profile/flex-profile.reducer';
import { getEntity, updateEntity, createEntity, reset } from './flex-el-val.reducer';
import { IFlexElVal } from 'app/shared/model/flex-el-val.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const FlexElValUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const flexProfiles = useAppSelector(state => state.flexProfile.entities);
  const flexElValEntity = useAppSelector(state => state.flexElVal.entity);
  const loading = useAppSelector(state => state.flexElVal.loading);
  const updating = useAppSelector(state => state.flexElVal.updating);
  const updateSuccess = useAppSelector(state => state.flexElVal.updateSuccess);
  const handleClose = () => {
    props.history.push('/flex-el-val' + props.location.search);
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
      ...flexElValEntity,
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
          ...flexElValEntity,
          flexProfile: flexElValEntity?.flexProfile?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.flexElVal.home.createOrEditLabel" data-cy="FlexElValCreateUpdateHeading">
            Create or edit a FlexElVal
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="flex-el-val-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Bus Num" id="flex-el-val-busNum" name="busNum" data-cy="busNum" type="text" />
              <ValidatedField label="Hour" id="flex-el-val-hour" name="hour" data-cy="hour" type="text" />
              <ValidatedField label="Min" id="flex-el-val-min" name="min" data-cy="min" type="text" />
              <ValidatedField label="Pfmax Up" id="flex-el-val-pfmaxUp" name="pfmaxUp" data-cy="pfmaxUp" type="text" />
              <ValidatedField label="Pfmax Dn" id="flex-el-val-pfmaxDn" name="pfmaxDn" data-cy="pfmaxDn" type="text" />
              <ValidatedField label="Qfmax Up" id="flex-el-val-qfmaxUp" name="qfmaxUp" data-cy="qfmaxUp" type="text" />
              <ValidatedField label="Qfmax Dn" id="flex-el-val-qfmaxDn" name="qfmaxDn" data-cy="qfmaxDn" type="text" />
              <ValidatedField id="flex-el-val-flexProfile" name="flexProfile" data-cy="flexProfile" label="Flex Profile" type="select">
                <option value="" key="0" />
                {flexProfiles
                  ? flexProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/flex-el-val" replace color="info">
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

export default FlexElValUpdate;
