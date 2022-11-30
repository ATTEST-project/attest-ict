import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './storage-cost.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const StorageCostDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const storageCostEntity = useAppSelector(state => state.storageCost.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="storageCostDetailsHeading">StorageCost</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{storageCostEntity.id}</dd>
          <dt>
            <span id="busNum">Bus Num</span>
          </dt>
          <dd>{storageCostEntity.busNum}</dd>
          <dt>
            <span id="costA">Cost A</span>
          </dt>
          <dd>{storageCostEntity.costA}</dd>
          <dt>
            <span id="costB">Cost B</span>
          </dt>
          <dd>{storageCostEntity.costB}</dd>
          <dt>
            <span id="costC">Cost C</span>
          </dt>
          <dd>{storageCostEntity.costC}</dd>
        </dl>
        <Button tag={Link} to="/storage-cost" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/storage-cost/${storageCostEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default StorageCostDetail;
