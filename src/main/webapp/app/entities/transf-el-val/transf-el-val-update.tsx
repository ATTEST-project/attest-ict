import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ITransfProfile } from 'app/shared/model/transf-profile.model';
import { getEntities as getTransfProfiles } from 'app/entities/transf-profile/transf-profile.reducer';
import { IBranch } from 'app/shared/model/branch.model';
import { getEntities as getBranches } from 'app/entities/branch/branch.reducer';
import { getEntity, updateEntity, createEntity, reset } from './transf-el-val.reducer';
import { ITransfElVal } from 'app/shared/model/transf-el-val.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TransfElValUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const transfProfiles = useAppSelector(state => state.transfProfile.entities);
  const branches = useAppSelector(state => state.branch.entities);
  const transfElValEntity = useAppSelector(state => state.transfElVal.entity);
  const loading = useAppSelector(state => state.transfElVal.loading);
  const updating = useAppSelector(state => state.transfElVal.updating);
  const updateSuccess = useAppSelector(state => state.transfElVal.updateSuccess);
  const handleClose = () => {
    props.history.push('/transf-el-val' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getTransfProfiles({}));
    dispatch(getBranches({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...transfElValEntity,
      ...values,
      transfProfile: transfProfiles.find(it => it.id.toString() === values.transfProfile.toString()),
      branch: branches.find(it => it.id.toString() === values.branch.toString()),
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
          ...transfElValEntity,
          transfProfile: transfElValEntity?.transfProfile?.id,
          branch: transfElValEntity?.branch?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.transfElVal.home.createOrEditLabel" data-cy="TransfElValCreateUpdateHeading">
            Create or edit a TransfElVal
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
                <ValidatedField name="id" required readOnly id="transf-el-val-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Hour" id="transf-el-val-hour" name="hour" data-cy="hour" type="text" />
              <ValidatedField label="Min" id="transf-el-val-min" name="min" data-cy="min" type="text" />
              <ValidatedField label="Tap Ratio" id="transf-el-val-tapRatio" name="tapRatio" data-cy="tapRatio" type="text" />
              <ValidatedField label="Status" id="transf-el-val-status" name="status" data-cy="status" type="text" />
              <ValidatedField
                label="Trasf Id On Subst"
                id="transf-el-val-trasfIdOnSubst"
                name="trasfIdOnSubst"
                data-cy="trasfIdOnSubst"
                type="text"
              />
              <ValidatedField
                label="Nominal Voltage"
                id="transf-el-val-nominalVoltage"
                name="nominalVoltage"
                data-cy="nominalVoltage"
                type="text"
              />
              <ValidatedField
                id="transf-el-val-transfProfile"
                name="transfProfile"
                data-cy="transfProfile"
                label="Transf Profile"
                type="select"
              >
                <option value="" key="0" />
                {transfProfiles
                  ? transfProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="transf-el-val-branch" name="branch" data-cy="branch" label="Branch" type="select">
                <option value="" key="0" />
                {branches
                  ? branches.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/transf-el-val" replace color="info">
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

export default TransfElValUpdate;
