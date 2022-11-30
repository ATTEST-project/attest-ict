import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './transf-el-val.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TransfElValDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const transfElValEntity = useAppSelector(state => state.transfElVal.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="transfElValDetailsHeading">TransfElVal</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{transfElValEntity.id}</dd>
          <dt>
            <span id="hour">Hour</span>
          </dt>
          <dd>{transfElValEntity.hour}</dd>
          <dt>
            <span id="min">Min</span>
          </dt>
          <dd>{transfElValEntity.min}</dd>
          <dt>
            <span id="tapRatio">Tap Ratio</span>
          </dt>
          <dd>{transfElValEntity.tapRatio}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{transfElValEntity.status}</dd>
          <dt>
            <span id="trasfIdOnSubst">Trasf Id On Subst</span>
          </dt>
          <dd>{transfElValEntity.trasfIdOnSubst}</dd>
          <dt>
            <span id="nominalVoltage">Nominal Voltage</span>
          </dt>
          <dd>{transfElValEntity.nominalVoltage}</dd>
          <dt>Transf Profile</dt>
          <dd>{transfElValEntity.transfProfile ? transfElValEntity.transfProfile.id : ''}</dd>
          <dt>Branch</dt>
          <dd>{transfElValEntity.branch ? transfElValEntity.branch.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/transf-el-val" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/transf-el-val/${transfElValEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TransfElValDetail;
