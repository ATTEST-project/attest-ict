import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ILoadProfile } from 'app/shared/model/load-profile.model';
import { getEntities as getLoadProfiles } from 'app/entities/load-profile/load-profile.reducer';
import { IBus } from 'app/shared/model/bus.model';
import { getEntities as getBuses } from 'app/entities/bus/bus.reducer';
import { getEntity, updateEntity, createEntity, reset } from './load-el-val.reducer';
import { ILoadElVal } from 'app/shared/model/load-el-val.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const LoadElValUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const loadProfiles = useAppSelector(state => state.loadProfile.entities);
  const buses = useAppSelector(state => state.bus.entities);
  const loadElValEntity = useAppSelector(state => state.loadElVal.entity);
  const loading = useAppSelector(state => state.loadElVal.loading);
  const updating = useAppSelector(state => state.loadElVal.updating);
  const updateSuccess = useAppSelector(state => state.loadElVal.updateSuccess);
  const handleClose = () => {
    props.history.push('/load-el-val' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getLoadProfiles({}));
    dispatch(getBuses({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...loadElValEntity,
      ...values,
      loadProfile: loadProfiles.find(it => it.id.toString() === values.loadProfile.toString()),
      bus: buses.find(it => it.id.toString() === values.bus.toString()),
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
          ...loadElValEntity,
          loadProfile: loadElValEntity?.loadProfile?.id,
          bus: loadElValEntity?.bus?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.loadElVal.home.createOrEditLabel" data-cy="LoadElValCreateUpdateHeading">
            Create or edit a LoadElVal
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="load-el-val-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Hour" id="load-el-val-hour" name="hour" data-cy="hour" type="text" />
              <ValidatedField label="Min" id="load-el-val-min" name="min" data-cy="min" type="text" />
              <ValidatedField label="P" id="load-el-val-p" name="p" data-cy="p" type="text" />
              <ValidatedField label="Q" id="load-el-val-q" name="q" data-cy="q" type="text" />
              <ValidatedField
                label="Load Id On Subst"
                id="load-el-val-loadIdOnSubst"
                name="loadIdOnSubst"
                data-cy="loadIdOnSubst"
                type="text"
              />
              <ValidatedField
                label="Nominal Voltage"
                id="load-el-val-nominalVoltage"
                name="nominalVoltage"
                data-cy="nominalVoltage"
                type="text"
              />
              <ValidatedField id="load-el-val-loadProfile" name="loadProfile" data-cy="loadProfile" label="Load Profile" type="select">
                <option value="" key="0" />
                {loadProfiles
                  ? loadProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="load-el-val-bus" name="bus" data-cy="bus" label="Bus" type="select">
                <option value="" key="0" />
                {buses
                  ? buses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/load-el-val" replace color="info">
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

export default LoadElValUpdate;
