import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './bus-name.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BusNameDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const busNameEntity = useAppSelector(state => state.busName.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="busNameDetailsHeading">BusName</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{busNameEntity.id}</dd>
          <dt>
            <span id="busName">Bus Name</span>
          </dt>
          <dd>{busNameEntity.busName}</dd>
          <dt>Bus</dt>
          <dd>{busNameEntity.bus ? busNameEntity.bus.busNum : ''}</dd>
        </dl>
        <Button tag={Link} to="/bus-name" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bus-name/${busNameEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BusNameDetail;
