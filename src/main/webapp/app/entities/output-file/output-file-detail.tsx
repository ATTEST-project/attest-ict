import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './output-file.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const OutputFileDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const outputFileEntity = useAppSelector(state => state.outputFile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="outputFileDetailsHeading">OutputFile</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{outputFileEntity.id}</dd>
          <dt>
            <span id="fileName">File Name</span>
          </dt>
          <dd>{outputFileEntity.fileName}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{outputFileEntity.description}</dd>
          <dt>
            <span id="data">Data</span>
          </dt>
          <dd>
            {outputFileEntity.data ? (
              <div>
                {outputFileEntity.dataContentType ? (
                  <a onClick={openFile(outputFileEntity.dataContentType, outputFileEntity.data)}>Open&nbsp;</a>
                ) : null}
                <span>
                  {outputFileEntity.dataContentType}, {byteSize(outputFileEntity.data)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="uploadTime">Upload Time</span>
          </dt>
          <dd>
            {outputFileEntity.uploadTime ? <TextFormat value={outputFileEntity.uploadTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>Tool</dt>
          <dd>{outputFileEntity.tool ? outputFileEntity.tool.id : ''}</dd>
          <dt>Network</dt>
          <dd>{outputFileEntity.network ? outputFileEntity.network.id : ''}</dd>
          <dt>Simulation</dt>
          <dd>{outputFileEntity.simulation ? outputFileEntity.simulation.uuid : ''}</dd>
        </dl>
        <Button tag={Link} to="/output-file" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/output-file/${outputFileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default OutputFileDetail;
