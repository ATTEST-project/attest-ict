import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IBranch } from 'app/shared/model/branch.model';
import { getEntities as getBranches } from 'app/entities/branch/branch.reducer';
import { IBus } from 'app/shared/model/bus.model';
import { getEntities as getBuses } from 'app/entities/bus/bus.reducer';
import { getEntity, updateEntity, createEntity, reset } from './protection-tool.reducer';
import { IProtectionTool } from 'app/shared/model/protection-tool.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ProtectionToolUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const branches = useAppSelector(state => state.branch.entities);
  const buses = useAppSelector(state => state.bus.entities);
  const protectionToolEntity = useAppSelector(state => state.protectionTool.entity);
  const loading = useAppSelector(state => state.protectionTool.loading);
  const updating = useAppSelector(state => state.protectionTool.updating);
  const updateSuccess = useAppSelector(state => state.protectionTool.updateSuccess);
  const handleClose = () => {
    props.history.push('/protection-tool' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getBranches({}));
    dispatch(getBuses({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...protectionToolEntity,
      ...values,
      branch: branches.find(it => it.id.toString() === values.branch.toString()),
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
          ...protectionToolEntity,
          branch: protectionToolEntity?.branch?.id,
          bus: protectionToolEntity?.bus?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.protectionTool.home.createOrEditLabel" data-cy="ProtectionToolCreateUpdateHeading">
            Create or edit a ProtectionTool
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
                <ValidatedField name="id" required readOnly id="protection-tool-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Type" id="protection-tool-type" name="type" data-cy="type" type="text" />
              <ValidatedField id="protection-tool-branch" name="branch" data-cy="branch" label="Branch" type="select">
                <option value="" key="0" />
                {branches
                  ? branches.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="protection-tool-bus" name="bus" data-cy="bus" label="Bus" type="select">
                <option value="" key="0" />
                {buses
                  ? buses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/protection-tool" replace color="info">
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

export default ProtectionToolUpdate;
