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

    dispatch(getToolLogFiles({}));
    dispatch(getSimulations({}));
    dispatch(getTools({}));
    dispatch(getUsers({}));
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
      toolLogFile: toolLogFiles.find(it => it.id.toString() === values.toolLogFile.toString()),
      simulation: simulations.find(it => it.id.toString() === values.simulation.toString()),
      tool: tools.find(it => it.id.toString() === values.tool.toString()),
      user: users.find(it => it.id.toString() === values.user.toString()),
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
          // toolLogFileId: taskEntity?.toolLogFileId,
          // simulationUuid: taskEntity?.simulation?.id,
          // tool: taskEntity?.tool?.id,
          // user: taskEntity?.user?.id,
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
              {/*
              <ValidatedField id="task-toolLogFile" name="toolLogFile" data-cy="toolLogFile" label="Tool Log File" type="select">
                <option value="" key="0" />
                {toolLogFiles
                  ? toolLogFiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="task-simulation" name="simulation" data-cy="simulation" label="Simulation" type="select">
                <option value="" key="0" />
                {simulations
                  ? simulations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.uuid}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="task-tool" name="tool" data-cy="tool" label="Tool" type="select">
                <option value="" key="0" />
                {tools
                  ? tools.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="task-user" name="user" data-cy="user" label="User" type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
*/}
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
