import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './task.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { hasAuthorityForUpdateTask, shouldFilterTask } from 'app/shared/util/authorizationUtils';

export const TaskDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();
  const currentUser = useAppSelector(state => state.authentication.account);
  const shouldUpdate = hasAuthorityForUpdateTask(currentUser.authorities);

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const taskEntity = useAppSelector(state => state.task.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="taskDetailsHeading">Task</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{taskEntity.id}</dd>
          <dt>
            <span id="taskStatus">Task Status</span>
          </dt>
          <dd>{taskEntity.taskStatus}</dd>
          <dt>
            <span id="info">Info</span>
          </dt>
          <dd>{taskEntity.info}</dd>
          <dt>
            <span id="dateTimeStart">Date Time Start</span>
          </dt>
          <dd>{taskEntity.dateTimeStart ? <TextFormat value={taskEntity.dateTimeStart} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="dateTimeEnd">Date Time End</span>
          </dt>
          <dd>{taskEntity.dateTimeEnd ? <TextFormat value={taskEntity.dateTimeEnd} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>Tool Log File ID </dt>
          <dd>{taskEntity.toolLogFileId ? taskEntity.toolLogFileId : ''}</dd>
          <dt>Network </dt>
          <dd>{'ID: ' + taskEntity.networkId + ', Name: ' + taskEntity.networkName}</dd>
          <dt>Simulation</dt>
          <dd>{taskEntity.simulationUuid ? taskEntity.simulationUuid : ''}</dd>
          {/*
           <dd>
                      {taskEntity.simulation.configFile ? (
                        <div>
                          {taskEntity.simulation.configFileContentType ? (
                            <a onClick={openFile(taskEntity.simulation.configFileContentType, taskEntity.simulation.configFile)}>Open&nbsp;</a>
                          ) : null}
                          <span>
                            {taskEntity.simulation.configFileContentType}, {byteSize(taskEntity.simulation.configFile)}
                          </span>
                        </div>
                      ) : null}
                    </dd>
*/}
          <dt>Tool</dt>
          <dd>{taskEntity.tool ? 'ID: ' + taskEntity.tool.id + ', Num:' + taskEntity.toolNum : ''}</dd>
          <dt>User</dt>
          <dd>{taskEntity.user ? taskEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/task" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        {shouldUpdate && (
          <Button tag={Link} to={`/task/${taskEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        )}
      </Col>
    </Row>
  );
};

export default TaskDetail;
