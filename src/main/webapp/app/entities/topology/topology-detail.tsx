import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './topology.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TopologyDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const topologyEntity = useAppSelector(state => state.topology.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="topologyDetailsHeading">Topology</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{topologyEntity.id}</dd>
          <dt>
            <span id="powerLineBranch">Power Line Branch</span>
          </dt>
          <dd>{topologyEntity.powerLineBranch}</dd>
          <dt>
            <span id="p1">P 1</span>
          </dt>
          <dd>{topologyEntity.p1}</dd>
          <dt>
            <span id="p2">P 2</span>
          </dt>
          <dd>{topologyEntity.p2}</dd>
          <dt>Power Line Branch Parent</dt>
          <dd>{topologyEntity.powerLineBranchParent ? topologyEntity.powerLineBranchParent.powerLineBranch : ''}</dd>
        </dl>
        <Button tag={Link} to="/topology" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/topology/${topologyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TopologyDetail;
