import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './wind-data.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WindDataDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const windDataEntity = useAppSelector(state => state.windData.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="windDataDetailsHeading">WindData</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{windDataEntity.id}</dd>
          <dt>
            <span id="windSpeed">Wind Speed</span>
          </dt>
          <dd>{windDataEntity.windSpeed}</dd>
          <dt>
            <span id="hour">Hour</span>
          </dt>
          <dd>{windDataEntity.hour}</dd>
        </dl>
        <Button tag={Link} to="/wind-data" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/wind-data/${windDataEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default WindDataDetail;
