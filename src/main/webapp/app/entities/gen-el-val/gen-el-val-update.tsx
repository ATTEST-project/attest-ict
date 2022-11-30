import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IGenProfile } from 'app/shared/model/gen-profile.model';
import { getEntities as getGenProfiles } from 'app/entities/gen-profile/gen-profile.reducer';
import { IGenerator } from 'app/shared/model/generator.model';
import { getEntities as getGenerators } from 'app/entities/generator/generator.reducer';
import { getEntity, updateEntity, createEntity, reset } from './gen-el-val.reducer';
import { IGenElVal } from 'app/shared/model/gen-el-val.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GenElValUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const genProfiles = useAppSelector(state => state.genProfile.entities);
  const generators = useAppSelector(state => state.generator.entities);
  const genElValEntity = useAppSelector(state => state.genElVal.entity);
  const loading = useAppSelector(state => state.genElVal.loading);
  const updating = useAppSelector(state => state.genElVal.updating);
  const updateSuccess = useAppSelector(state => state.genElVal.updateSuccess);
  const handleClose = () => {
    props.history.push('/gen-el-val' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getGenProfiles({}));
    dispatch(getGenerators({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...genElValEntity,
      ...values,
      genProfile: genProfiles.find(it => it.id.toString() === values.genProfile.toString()),
      generator: generators.find(it => it.id.toString() === values.generator.toString()),
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
          ...genElValEntity,
          genProfile: genElValEntity?.genProfile?.id,
          generator: genElValEntity?.generator?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.genElVal.home.createOrEditLabel" data-cy="GenElValCreateUpdateHeading">
            Create or edit a GenElVal
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="gen-el-val-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Hour" id="gen-el-val-hour" name="hour" data-cy="hour" type="text" />
              <ValidatedField label="Min" id="gen-el-val-min" name="min" data-cy="min" type="text" />
              <ValidatedField label="P" id="gen-el-val-p" name="p" data-cy="p" type="text" />
              <ValidatedField label="Q" id="gen-el-val-q" name="q" data-cy="q" type="text" />
              <ValidatedField label="Status" id="gen-el-val-status" name="status" data-cy="status" type="text" />
              <ValidatedField
                label="Voltage Magnitude"
                id="gen-el-val-voltageMagnitude"
                name="voltageMagnitude"
                data-cy="voltageMagnitude"
                type="text"
              />
              <ValidatedField label="Gen Id On Subst" id="gen-el-val-genIdOnSubst" name="genIdOnSubst" data-cy="genIdOnSubst" type="text" />
              <ValidatedField
                label="Nominal Voltage"
                id="gen-el-val-nominalVoltage"
                name="nominalVoltage"
                data-cy="nominalVoltage"
                type="text"
              />
              <ValidatedField id="gen-el-val-genProfile" name="genProfile" data-cy="genProfile" label="Gen Profile" type="select">
                <option value="" key="0" />
                {genProfiles
                  ? genProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="gen-el-val-generator" name="generator" data-cy="generator" label="Generator" type="select">
                <option value="" key="0" />
                {generators
                  ? generators.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/gen-el-val" replace color="info">
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

export default GenElValUpdate;
