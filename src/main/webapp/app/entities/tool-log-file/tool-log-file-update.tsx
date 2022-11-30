import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ITask } from 'app/shared/model/task.model';
import { getEntities as getTasks } from 'app/entities/task/task.reducer';
import { getEntity, updateEntity, createEntity, reset } from './tool-log-file.reducer';
import { IToolLogFile } from 'app/shared/model/tool-log-file.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ToolLogFileUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const tasks = useAppSelector(state => state.task.entities);
  const toolLogFileEntity = useAppSelector(state => state.toolLogFile.entity);
  const loading = useAppSelector(state => state.toolLogFile.loading);
  const updating = useAppSelector(state => state.toolLogFile.updating);
  const updateSuccess = useAppSelector(state => state.toolLogFile.updateSuccess);
  const handleClose = () => {
    props.history.push('/tool-log-file' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getTasks({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.uploadTime = convertDateTimeToServer(values.uploadTime);

    const entity = {
      ...toolLogFileEntity,
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
      ? {
          uploadTime: displayDefaultDateTime(),
        }
      : {
          ...toolLogFileEntity,
          uploadTime: convertDateTimeFromServer(toolLogFileEntity.uploadTime),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.toolLogFile.home.createOrEditLabel" data-cy="ToolLogFileCreateUpdateHeading">
            Create or edit a ToolLogFile
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
                <ValidatedField name="id" required readOnly id="tool-log-file-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="File Name" id="tool-log-file-fileName" name="fileName" data-cy="fileName" type="text" />
              <ValidatedField label="Description" id="tool-log-file-description" name="description" data-cy="description" type="text" />
              <ValidatedBlobField label="Data" id="tool-log-file-data" name="data" data-cy="data" openActionLabel="Open" />
              <ValidatedField
                label="Upload Time"
                id="tool-log-file-uploadTime"
                name="uploadTime"
                data-cy="uploadTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/tool-log-file" replace color="info">
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

export default ToolLogFileUpdate;
