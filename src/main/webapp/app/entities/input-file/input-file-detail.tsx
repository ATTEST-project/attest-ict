import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './input-file.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const InputFileDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const inputFileEntity = useAppSelector(state => state.inputFile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="inputFileDetailsHeading">InputFile</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{inputFileEntity.id}</dd>
          <dt>
            <span id="fileName">File Name</span>
          </dt>
          <dd>{inputFileEntity.fileName}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{inputFileEntity.description}</dd>
          <dt>
            <span id="data">Data</span>
          </dt>
          <dd>
            {inputFileEntity.data ? (
              <div>
                {inputFileEntity.dataContentType ? (
                  <a onClick={openFile(inputFileEntity.dataContentType, inputFileEntity.data)}>Open&nbsp;</a>
                ) : null}
                <span>
                  {inputFileEntity.dataContentType}, {byteSize(inputFileEntity.data)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="uploadTime">Upload Time</span>
          </dt>
          <dd>
            {inputFileEntity.uploadTime ? <TextFormat value={inputFileEntity.uploadTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>Tool</dt>
          <dd>{inputFileEntity.tool ? inputFileEntity.tool.id : ''}</dd>
          <dt>Network</dt>
          <dd>{inputFileEntity.network ? inputFileEntity.network.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/input-file" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/input-file/${inputFileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default InputFileDetail;
