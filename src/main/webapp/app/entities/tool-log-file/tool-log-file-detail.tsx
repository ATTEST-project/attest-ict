import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './tool-log-file.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ToolLogFileDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const toolLogFileEntity = useAppSelector(state => state.toolLogFile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="toolLogFileDetailsHeading">ToolLogFile</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{toolLogFileEntity.id}</dd>
          <dt>
            <span id="fileName">File Name</span>
          </dt>
          <dd>{toolLogFileEntity.fileName}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{toolLogFileEntity.description}</dd>
          <dt>
            <span id="data">Data</span>
          </dt>
          <dd>
            {toolLogFileEntity.data ? (
              <div>
                {toolLogFileEntity.dataContentType ? (
                  <a onClick={openFile(toolLogFileEntity.dataContentType, toolLogFileEntity.data)}>Open&nbsp;</a>
                ) : null}
                <span>
                  {toolLogFileEntity.dataContentType}, {byteSize(toolLogFileEntity.data)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="uploadTime">Upload Time</span>
          </dt>
          <dd>
            {toolLogFileEntity.uploadTime ? <TextFormat value={toolLogFileEntity.uploadTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/tool-log-file" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tool-log-file/${toolLogFileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ToolLogFileDetail;
