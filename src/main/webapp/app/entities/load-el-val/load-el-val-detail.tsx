import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './load-el-val.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const LoadElValDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const loadElValEntity = useAppSelector(state => state.loadElVal.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="loadElValDetailsHeading">LoadElVal</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{loadElValEntity.id}</dd>
          <dt>
            <span id="hour">Hour</span>
          </dt>
          <dd>{loadElValEntity.hour}</dd>
          <dt>
            <span id="min">Min</span>
          </dt>
          <dd>{loadElValEntity.min}</dd>
          <dt>
            <span id="p">P</span>
          </dt>
          <dd>{loadElValEntity.p}</dd>
          <dt>
            <span id="q">Q</span>
          </dt>
          <dd>{loadElValEntity.q}</dd>
          <dt>
            <span id="loadIdOnSubst">Load Id On Subst</span>
          </dt>
          <dd>{loadElValEntity.loadIdOnSubst}</dd>
          <dt>
            <span id="nominalVoltage">Nominal Voltage</span>
          </dt>
          <dd>{loadElValEntity.nominalVoltage}</dd>
          <dt>Load Profile</dt>
          <dd>{loadElValEntity.loadProfile ? loadElValEntity.loadProfile.id : ''}</dd>
          <dt>Bus</dt>
          <dd>{loadElValEntity.bus ? loadElValEntity.bus.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/load-el-val" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/load-el-val/${loadElValEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default LoadElValDetail;
