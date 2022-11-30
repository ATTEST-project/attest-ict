import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './voltage-level.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const VoltageLevelDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const voltageLevelEntity = useAppSelector(state => state.voltageLevel.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="voltageLevelDetailsHeading">VoltageLevel</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{voltageLevelEntity.id}</dd>
          <dt>
            <span id="v1">V 1</span>
          </dt>
          <dd>{voltageLevelEntity.v1}</dd>
          <dt>
            <span id="v2">V 2</span>
          </dt>
          <dd>{voltageLevelEntity.v2}</dd>
          <dt>
            <span id="v3">V 3</span>
          </dt>
          <dd>{voltageLevelEntity.v3}</dd>
          <dt>Network</dt>
          <dd>{voltageLevelEntity.network ? voltageLevelEntity.network.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/voltage-level" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/voltage-level/${voltageLevelEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default VoltageLevelDetail;
