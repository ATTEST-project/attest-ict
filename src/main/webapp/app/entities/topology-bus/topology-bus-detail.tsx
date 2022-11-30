import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './topology-bus.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TopologyBusDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const topologyBusEntity = useAppSelector(state => state.topologyBus.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="topologyBusDetailsHeading">TopologyBus</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{topologyBusEntity.id}</dd>
          <dt>
            <span id="powerLineBranch">Power Line Branch</span>
          </dt>
          <dd>{topologyBusEntity.powerLineBranch}</dd>
          <dt>
            <span id="busName1">Bus Name 1</span>
          </dt>
          <dd>{topologyBusEntity.busName1}</dd>
          <dt>
            <span id="busName2">Bus Name 2</span>
          </dt>
          <dd>{topologyBusEntity.busName2}</dd>
          <dt>Network</dt>
          <dd>{topologyBusEntity.network ? topologyBusEntity.network.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/topology-bus" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/topology-bus/${topologyBusEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TopologyBusDetail;
