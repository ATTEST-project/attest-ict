import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './dso-tso-connection.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const DsoTsoConnectionDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const dsoTsoConnectionEntity = useAppSelector(state => state.dsoTsoConnection.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="dsoTsoConnectionDetailsHeading">DsoTsoConnection</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{dsoTsoConnectionEntity.id}</dd>
          <dt>
            <span id="tsoNetworkName">Tso Network Name</span>
          </dt>
          <dd>{dsoTsoConnectionEntity.tsoNetworkName}</dd>
          <dt>
            <span id="dsoBusNum">Dso Bus Num</span>
          </dt>
          <dd>{dsoTsoConnectionEntity.dsoBusNum}</dd>
          <dt>
            <span id="tsoBusNum">Tso Bus Num</span>
          </dt>
          <dd>{dsoTsoConnectionEntity.tsoBusNum}</dd>
          <dt>Dso Network</dt>
          <dd>{dsoTsoConnectionEntity.dsoNetwork ? dsoTsoConnectionEntity.dsoNetwork.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/dso-tso-connection" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/dso-tso-connection/${dsoTsoConnectionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default DsoTsoConnectionDetail;
