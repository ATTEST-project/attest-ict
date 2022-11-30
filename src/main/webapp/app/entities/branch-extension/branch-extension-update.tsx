import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IBranch } from 'app/shared/model/branch.model';
import { getEntities as getBranches } from 'app/entities/branch/branch.reducer';
import { getEntity, updateEntity, createEntity, reset } from './branch-extension.reducer';
import { IBranchExtension } from 'app/shared/model/branch-extension.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BranchExtensionUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const branches = useAppSelector(state => state.branch.entities);
  const branchExtensionEntity = useAppSelector(state => state.branchExtension.entity);
  const loading = useAppSelector(state => state.branchExtension.loading);
  const updating = useAppSelector(state => state.branchExtension.updating);
  const updateSuccess = useAppSelector(state => state.branchExtension.updateSuccess);
  const handleClose = () => {
    props.history.push('/branch-extension' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getBranches({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...branchExtensionEntity,
      ...values,
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
          ...branchExtensionEntity,
          branch: branchExtensionEntity?.branch?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.branchExtension.home.createOrEditLabel" data-cy="BranchExtensionCreateUpdateHeading">
            Create or edit a BranchExtension
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
                <ValidatedField name="id" required readOnly id="branch-extension-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Step Size" id="branch-extension-stepSize" name="stepSize" data-cy="stepSize" type="text" />
              <ValidatedField label="Act Tap" id="branch-extension-actTap" name="actTap" data-cy="actTap" type="text" />
              <ValidatedField label="Min Tap" id="branch-extension-minTap" name="minTap" data-cy="minTap" type="text" />
              <ValidatedField label="Max Tap" id="branch-extension-maxTap" name="maxTap" data-cy="maxTap" type="text" />
              <ValidatedField label="Normal Tap" id="branch-extension-normalTap" name="normalTap" data-cy="normalTap" type="text" />
              <ValidatedField
                label="Nominal Ratio"
                id="branch-extension-nominalRatio"
                name="nominalRatio"
                data-cy="nominalRatio"
                type="text"
              />
              <ValidatedField label="R Ip" id="branch-extension-rIp" name="rIp" data-cy="rIp" type="text" />
              <ValidatedField label="R N" id="branch-extension-rN" name="rN" data-cy="rN" type="text" />
              <ValidatedField label="R 0" id="branch-extension-r0" name="r0" data-cy="r0" type="text" />
              <ValidatedField label="X 0" id="branch-extension-x0" name="x0" data-cy="x0" type="text" />
              <ValidatedField label="B 0" id="branch-extension-b0" name="b0" data-cy="b0" type="text" />
              <ValidatedField label="Length" id="branch-extension-length" name="length" data-cy="length" type="text" />
              <ValidatedField label="Norm Stat" id="branch-extension-normStat" name="normStat" data-cy="normStat" type="text" />
              <ValidatedField label="G" id="branch-extension-g" name="g" data-cy="g" type="text" />
              <ValidatedField label="M Rid" id="branch-extension-mRid" name="mRid" data-cy="mRid" type="text" />
              <ValidatedField id="branch-extension-branch" name="branch" data-cy="branch" label="Branch" type="select">
                <option value="" key="0" />
                {branches
                  ? branches.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/branch-extension" replace color="info">
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

export default BranchExtensionUpdate;
