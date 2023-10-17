import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IToolLogFile } from 'app/shared/model/tool-log-file.model';
import { getEntities as getToolLogFiles } from 'app/entities/tool-log-file/tool-log-file.reducer';
import { ISimulation } from 'app/shared/model/simulation.model';
import { getEntities as getSimulations } from 'app/entities/simulation/simulation.reducer';
import { ITool } from 'app/shared/model/tool.model';
import { getEntities as getTools } from 'app/entities/tool/tool.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/shared/reducers/user-management';
import { getEntity, updateEntity, createEntity, reset } from './task.reducer';
import { ITask } from 'app/shared/model/task.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TaskUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const toolLogFiles = useAppSelector(state => state.toolLogFile.entities);
  const simulations = useAppSelector(state => state.simulation.entities);
  const tools = useAppSelector(state => state.tool.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const taskEntity = useAppSelector(state => state.task.entity);
  const loading = useAppSelector(state => state.task.loading);
  const updating = useAppSelector(state => state.task.updating);
  const updateSuccess = useAppSelector(state => state.task.updateSuccess);

  const handleClose = () => {
    props.history.push('/task' + props.location.search);
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
    values.dateTimeStart = convertDateTimeToServer(values.dateTimeStart);
    values.dateTimeEnd = convertDateTimeToServer(values.dateTimeEnd);

    const entity = {
      ...taskEntity,
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
          dateTimeStart: displayDefaultDateTime(),
          dateTimeEnd: displayDefaultDateTime(),
        }
      : {
          ...taskEntity,
          dateTimeStart: convertDateTimeFromServer(taskEntity.dateTimeStart),
          dateTimeEnd: convertDateTimeFromServer(taskEntity.dateTimeEnd),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="attestApp.task.home.createOrEditLabel" data-cy="TaskCreateUpdateHeading">
            Create or edit a Task
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="task-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Task Status" id="task-taskStatus" name="taskStatus" data-cy="taskStatus" type="text" />
              <ValidatedField label="Info" id="task-info" name="info" data-cy="info" type="text" />
              <ValidatedField
                label="Date Time Start"
                id="task-dateTimeStart"
                name="dateTimeStart"
                data-cy="dateTimeStart"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Date Time End"
                id="task-dateTimeEnd"
                name="dateTimeEnd"
                data-cy="dateTimeEnd"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <dt>Tool Log File</dt>
              <dd>{taskEntity.toolLogFileId ? taskEntity.toolLogFileId : ''}</dd>
              <dt>Simulation</dt>
              <dd>{taskEntity.simulationUuid ? taskEntity.simulationUuid : ''}</dd>
              <dt>Tool</dt>
              <dd>{taskEntity.tool ? taskEntity.tool.num : ''}</dd>
              <dt>User</dt>
              <dd>{taskEntity.user ? taskEntity.user.login : ''}</dd>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/task" replace color="info">
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

export default TaskUpdate;
