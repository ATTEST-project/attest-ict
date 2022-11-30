import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './node.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const NodeDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const nodeEntity = useAppSelector(state => state.node.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="nodeDetailsHeading">Node</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{nodeEntity.id}</dd>
          <dt>
            <span id="networkId">Network Id</span>
          </dt>
          <dd>{nodeEntity.networkId}</dd>
          <dt>
            <span id="loadId">Load Id</span>
          </dt>
          <dd>{nodeEntity.loadId}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{nodeEntity.name}</dd>
        </dl>
        <Button tag={Link} to="/node" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/node/${nodeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default NodeDetail;
