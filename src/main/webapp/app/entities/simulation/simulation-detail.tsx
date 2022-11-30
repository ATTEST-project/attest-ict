import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './simulation.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const SimulationDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const simulationEntity = useAppSelector(state => state.simulation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="simulationDetailsHeading">Simulation</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{simulationEntity.id}</dd>
          <dt>
            <span id="uuid">Uuid</span>
          </dt>
          <dd>{simulationEntity.uuid}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{simulationEntity.description}</dd>
          <dt>
            <span id="configFile">Config File</span>
          </dt>
          <dd>
            {simulationEntity.configFile ? (
              <div>
                {simulationEntity.configFileContentType ? (
                  <a onClick={openFile(simulationEntity.configFileContentType, simulationEntity.configFile)}>Open&nbsp;</a>
                ) : null}
                <span>
                  {simulationEntity.configFileContentType}, {byteSize(simulationEntity.configFile)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>Network</dt>
          <dd>{simulationEntity.network ? simulationEntity.network.id : ''}</dd>
          <dt>Input File</dt>
          <dd>
            {simulationEntity.inputFiles
              ? simulationEntity.inputFiles.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.fileName}</a>
                    {simulationEntity.inputFiles && i === simulationEntity.inputFiles.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/simulation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/simulation/${simulationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SimulationDetail;
