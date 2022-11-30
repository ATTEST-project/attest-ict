import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './line-cable.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const LineCableDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const lineCableEntity = useAppSelector(state => state.lineCable.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="lineCableDetailsHeading">LineCable</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{lineCableEntity.id}</dd>
          <dt>
            <span id="fbus">Fbus</span>
          </dt>
          <dd>{lineCableEntity.fbus}</dd>
          <dt>
            <span id="tbus">Tbus</span>
          </dt>
          <dd>{lineCableEntity.tbus}</dd>
          <dt>
            <span id="lengthKm">Length Km</span>
          </dt>
          <dd>{lineCableEntity.lengthKm}</dd>
          <dt>
            <span id="typeOfInstallation">Type Of Installation</span>
          </dt>
          <dd>{lineCableEntity.typeOfInstallation}</dd>
          <dt>Network</dt>
          <dd>{lineCableEntity.network ? lineCableEntity.network.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/line-cable" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/line-cable/${lineCableEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default LineCableDetail;
