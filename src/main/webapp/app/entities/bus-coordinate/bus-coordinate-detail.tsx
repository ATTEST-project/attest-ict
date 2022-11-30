import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './bus-coordinate.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const BusCoordinateDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const busCoordinateEntity = useAppSelector(state => state.busCoordinate.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="busCoordinateDetailsHeading">BusCoordinate</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{busCoordinateEntity.id}</dd>
          <dt>
            <span id="x">X</span>
          </dt>
          <dd>{busCoordinateEntity.x}</dd>
          <dt>
            <span id="y">Y</span>
          </dt>
          <dd>{busCoordinateEntity.y}</dd>
          <dt>Bus</dt>
          <dd>{busCoordinateEntity.bus ? busCoordinateEntity.bus.busNum : ''}</dd>
        </dl>
        <Button tag={Link} to="/bus-coordinate" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bus-coordinate/${busCoordinateEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BusCoordinateDetail;
