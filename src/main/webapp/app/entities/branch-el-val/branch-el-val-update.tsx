import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IBranch } from 'app/shared/model/branch.model';
import { getEntities as getBranches } from 'app/entities/branch/branch.reducer';
import { IBranchProfile } from 'app/shared/model/branch-profile.model';
import { getEntities as getBranchProfiles } from 'app/entities/branch-profile/branch-profile.reducer';
import { getEntity, updateEntity, createEntity, reset } from './branch-el-val.reducer';
import { IBranchElVal } from 'app/shared/model/branch-el-val.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BranchElValUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const branches = useAppSelector(state => state.branch.entities);
  const branchProfiles = useAppSelector(state => state.branchProfile.entities);
  const branchElValEntity = useAppSelector(state => state.branchElVal.entity);
  const loading = useAppSelector(state => state.branchElVal.loading);
  const updating = useAppSelector(state => state.branchElVal.updating);
  const updateSuccess = useAppSelector(state => state.branchElVal.updateSuccess);
  const handleClose = () => {
    props.history.push('/branch-el-val' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getBranches({}));
    dispatch(getBranchProfiles({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...branchElValEntity,
      ...values,
      branch: branches.find(it => it.id.toString() === values.branch.toString()),
      branchProfile: branchProfiles.find(it => it.id.toString() === values.branchProfile.toString()),
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
          ...branchElValEntity,
          branch: branchElValEntity?.branch?.id,
          branchProfile: branchElValEntity?.branchProfile?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.branchElVal.home.createOrEditLabel" data-cy="BranchElValCreateUpdateHeading">
            Create or edit a BranchElVal
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
                <ValidatedField name="id" required readOnly id="branch-el-val-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Hour" id="branch-el-val-hour" name="hour" data-cy="hour" type="text" />
              <ValidatedField label="Min" id="branch-el-val-min" name="min" data-cy="min" type="text" />
              <ValidatedField label="P" id="branch-el-val-p" name="p" data-cy="p" type="text" />
              <ValidatedField label="Q" id="branch-el-val-q" name="q" data-cy="q" type="text" />
              <ValidatedField label="Status" id="branch-el-val-status" name="status" data-cy="status" type="text" />
              <ValidatedField
                label="Branch Id On Subst"
                id="branch-el-val-branchIdOnSubst"
                name="branchIdOnSubst"
                data-cy="branchIdOnSubst"
                type="text"
              />
              <ValidatedField
                label="Nominal Voltage"
                id="branch-el-val-nominalVoltage"
                name="nominalVoltage"
                data-cy="nominalVoltage"
                type="text"
              />
              <ValidatedField id="branch-el-val-branch" name="branch" data-cy="branch" label="Branch" type="select">
                <option value="" key="0" />
                {branches
                  ? branches.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="branch-el-val-branchProfile"
                name="branchProfile"
                data-cy="branchProfile"
                label="Branch Profile"
                type="select"
              >
                <option value="" key="0" />
                {branchProfiles
                  ? branchProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/branch-el-val" replace color="info">
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

export default BranchElValUpdate;
